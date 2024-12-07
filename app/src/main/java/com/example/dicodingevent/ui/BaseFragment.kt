package com.example.dicodingevent.ui

import android.net.ConnectivityManager
import android.net.Network
import android.content.Context
import android.net.NetworkRequest
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

open class BaseFragment : Fragment() {

    private var currentToast: Toast? = null
    private val _isInternetAvailable = MutableLiveData<Boolean>()
    val isInternetAvailable: LiveData<Boolean> get() = _isInternetAvailable

    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback

    private fun monitorInternetConnection() {
        connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val builder = NetworkRequest.Builder()

        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                _isInternetAvailable.postValue(true)
            }

            override fun onLost(network: Network) {
                _isInternetAvailable.postValue(false)
                showNoInternetToast() // Tampilkan Toast ketika koneksi internet hilang
            }
        }

        connectivityManager.registerNetworkCallback(builder.build(), networkCallback)
    }

    private fun showNoInternetToast() {
        currentToast?.cancel()
        currentToast = Toast.makeText(requireContext(), "No internet connection. Please check your network.", Toast.LENGTH_LONG)
        currentToast?.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        monitorInternetConnection()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        currentToast?.cancel()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}
