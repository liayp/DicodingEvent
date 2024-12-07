package com.example.dicodingevent.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.dicodingevent.R
import com.example.dicodingevent.data.response.Event
import com.example.dicodingevent.databinding.FragmentDetailBinding
import com.example.dicodingevent.ui.BaseFragment

@Suppress("DEPRECATION")
class DetailFragment : BaseFragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val detailViewModel: DetailViewModel by viewModels {
        DetailViewModelFactory.getInstance(requireContext())
    }
    private var isFavorite: Boolean = false

    companion object {
        private const val EVENT_ID_KEY = "eventId"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.detail_nav_menu, menu)

        val favoriteIcon = if (isFavorite) R.drawable.ic_heart_solid else R.drawable.ic_heart_regular
        menu.findItem(R.id.action_favorite)?.icon = ResourcesCompat.getDrawable(resources, favoriteIcon, requireContext().theme)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_favorite -> {
                detailViewModel.eventDetails.value?.let { event ->
                    detailViewModel.toggleFavorite(event, requireContext()) // Pass context here
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val eventId = arguments?.getInt(EVENT_ID_KEY) ?: return

        binding.progressBar.visibility = View.VISIBLE
        binding.registerButton.visibility = View.GONE

        // Memuat detail acara saat fragment muncul
        detailViewModel.getEventDetails(eventId)

        detailViewModel.eventDetails.observe(viewLifecycleOwner) { eventDetails ->
            binding.progressBar.visibility = View.GONE
            eventDetails?.let {
                updateUI(it)
                detailViewModel.checkIfFavorite(it.id ?: 0)
            }
        }

        detailViewModel.isFavorite.observe(viewLifecycleOwner) { isFavorite ->
            updateFavoriteIcon(isFavorite)
        }
    }

    private fun updateUI(eventDetails: Event) {
        binding.tvEventName.text = eventDetails.name
        binding.tvEventDescription.text = HtmlCompat.fromHtml(
            eventDetails.description ?: "",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        binding.tvEventOwner.text = eventDetails.ownerName
        binding.tvEventTime.text = eventDetails.beginTime

        val sisaQuota = (eventDetails.quota ?: 0) - (eventDetails.registrants ?: 0)
        binding.tvEventQuota.text = buildString {
            append("Sisa Quota: ")
            append(sisaQuota)
        }

        binding.imgEventLogo.loadImage(eventDetails.imageLogo)

        binding.registerButton.visibility = View.VISIBLE
        binding.registerButton.setOnClickListener {
            eventDetails.link?.let { url ->
                openSearchInBrowser(url)
            }
        }
    }

    private fun openSearchInBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            setPackage("com.android.chrome")
        }

        try {
            startActivity(intent)
        } catch (e: Exception) {
            intent.setPackage(null)
            startActivity(intent)
        }
    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {
        this.isFavorite = isFavorite
        requireActivity().invalidateOptionsMenu()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

fun ImageView.loadImage(url: String?) {
    Glide.with(this.context)
        .load(url)
        .centerCrop()
        .into(this)
}
