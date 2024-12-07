package com.example.dicodingevent.ui.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.UpcomingAdapter
import com.example.dicodingevent.R
import com.example.dicodingevent.databinding.FragmentUpcomingBinding
import com.example.dicodingevent.ui.BaseFragment

class UpcomingFragment : BaseFragment(), UpcomingAdapter.OnItemClickListener {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!
    private val upcomingViewModel: UpcomingViewModel by viewModels()
    private lateinit var adapter: UpcomingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
        loadData()

        isInternetAvailable.observe(viewLifecycleOwner) { isAvailable ->
            if (isAvailable) {
                loadData()
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = UpcomingAdapter(this)
        binding.rvUpcoming.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUpcoming.setHasFixedSize(true)
        binding.rvUpcoming.adapter = adapter
    }

    private fun observeViewModel() {
        upcomingViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        upcomingViewModel.eventList.observe(viewLifecycleOwner) { events ->
            adapter.submitList(events)
            binding.rvUpcoming.visibility = if (events.isEmpty()) View.GONE else View.VISIBLE
        }

        upcomingViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                showError(it)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.rvUpcoming.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun loadData() {
        upcomingViewModel.getUpcomingEvents()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(eventId: Int?) {
        eventId?.let {
            val bundle = Bundle().apply {
                putInt("eventId", it)
            }
            findNavController().navigate(R.id.action_upcoming_to_detail, bundle)
        }
    }
}
