package com.example.dicodingevent.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.FinishedAdapter
import com.example.dicodingevent.R
import com.example.dicodingevent.UpcomingAdapter
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.databinding.FragmentHomeBinding
import com.example.dicodingevent.ui.BaseFragment

class HomeFragment : BaseFragment(), FinishedAdapter.OnItemClickListener, UpcomingAdapter.OnItemClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var upcomingAdapter: UpcomingAdapter
    private lateinit var finishedAdapter: FinishedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()
        observeViewModel()
        loadData()

        isInternetAvailable.observe(viewLifecycleOwner) { isAvailable ->
            if (isAvailable) {
                loadData()
            }
        }
    }

    private fun setupRecyclerViews() {
        upcomingAdapter = UpcomingAdapter(this)
        binding.rvUpcoming.apply {
            adapter = upcomingAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }

        finishedAdapter = FinishedAdapter(this)
        binding.rvFinished.apply {
            adapter = finishedAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun observeViewModel() {
        homeViewModel.isLoadingUpcoming.observe(viewLifecycleOwner) { isLoading ->
            showLoadingUpcoming(isLoading)
        }

        homeViewModel.isLoadingFinished.observe(viewLifecycleOwner) { isLoading ->
            showLoadingFinished(isLoading)
        }

        homeViewModel.upcomingEvents.observe(viewLifecycleOwner) { events ->
            handleUpcomingEvents(events)
        }

        homeViewModel.finishedEvents.observe(viewLifecycleOwner) { events ->
            handleFinishedEvents(events)
        }

        homeViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                showError(it)
            }
        }
    }

    private fun showLoadingUpcoming(isLoading: Boolean) {
        binding.progressBarUpcoming.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.rvUpcoming.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun showLoadingFinished(isLoading: Boolean) {
        binding.progressBarFinished.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.rvFinished.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun handleUpcomingEvents(events: List<ListEventsItem>) {
        upcomingAdapter.submitList(events)
        binding.rvUpcoming.visibility = if (events.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun handleFinishedEvents(events: List<ListEventsItem>) {
        finishedAdapter.submitList(events)
        binding.rvFinished.visibility = if (events.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun loadData() {
        homeViewModel.getUpcomingEvents()
        homeViewModel.getFinishedEvents()
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
            findNavController().navigate(R.id.action_home_to_detail, bundle)
        }
    }
}
