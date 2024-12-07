package com.example.dicodingevent.ui.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.FavoriteAdapter
import com.example.dicodingevent.R
import com.example.dicodingevent.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment(), FavoriteAdapter.OnItemClickListener {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val favoriteViewModel: FavoriteViewModel by viewModels {
        FavoriteViewModelFactory.getInstance(requireContext())
    }

    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        favoriteAdapter = FavoriteAdapter(this)
        binding.rvFavorite.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavorite.setHasFixedSize(true)
        binding.rvFavorite.adapter = favoriteAdapter
    }

    private fun observeViewModel() {
        favoriteViewModel.favoriteList.observe(viewLifecycleOwner) { favorites ->
            favoriteAdapter.submitList(favorites)
            binding.rvFavorite.visibility = if (favorites.isEmpty()) View.GONE else View.VISIBLE
        }

        favoriteViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Log.e("FavoriteFragment", "Error: $it")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(eventId: Int?) {
        eventId?.let {
            Log.d("FavoriteFragment", "Clicked event ID: $it")
            val bundle = Bundle().apply {
                putInt("eventId", it)
            }
            findNavController().navigate(R.id.action_favorite_to_detail, bundle)
        }
    }
}
