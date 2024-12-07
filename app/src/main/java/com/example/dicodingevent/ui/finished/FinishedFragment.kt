package com.example.dicodingevent.ui.finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.dicodingevent.FinishedAdapter
import com.example.dicodingevent.R
import com.example.dicodingevent.databinding.FragmentFinishedBinding
import com.example.dicodingevent.ui.BaseFragment

class FinishedFragment : BaseFragment(), FinishedAdapter.OnItemClickListener {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!
    private val finishedViewModel: FinishedViewModel by viewModels()
    private lateinit var adapter: FinishedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
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
        adapter = FinishedAdapter(this)
        binding.rvFinished.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rvFinished.setHasFixedSize(true)
        binding.rvFinished.adapter = adapter
    }

    private fun observeViewModel() {
        finishedViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        finishedViewModel.eventList.observe(viewLifecycleOwner) { events ->
            adapter.submitList(events)
            binding.rvFinished.visibility = if (events.isEmpty()) View.GONE else View.VISIBLE
        }

        finishedViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                showError(it)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.rvFinished.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun loadData() {
        finishedViewModel.getFinishedEvents()
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
            findNavController().navigate(R.id.action_finished_to_detail, bundle)
        }
    }
}
