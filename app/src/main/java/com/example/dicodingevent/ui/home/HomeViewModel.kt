package com.example.dicodingevent.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.response.EventResponse
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _upcomingEvents = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvents: LiveData<List<ListEventsItem>> get() = _upcomingEvents

    private val _finishedEvents = MutableLiveData<List<ListEventsItem>>()
    val finishedEvents: LiveData<List<ListEventsItem>> get() = _finishedEvents

    private val _isLoadingUpcoming = MutableLiveData<Boolean>()
    val isLoadingUpcoming: LiveData<Boolean> get() = _isLoadingUpcoming

    private val _isLoadingFinished = MutableLiveData<Boolean>()
    val isLoadingFinished: LiveData<Boolean> get() = _isLoadingFinished

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun getFinishedEvents() {
        _isLoadingFinished.value = true
        viewModelScope.launch {
            try {
                val response: Response<EventResponse> = ApiConfig.getApiService().getCompletedEvents()
                _isLoadingFinished.value = false
                if (response.isSuccessful) {
                    _finishedEvents.value = response.body()?.listEvents ?: emptyList()
                } else {
                    _errorMessage.value = "Gagal mendapatkan data: ${response.message()}"
                }
            } catch (e: Exception) {
                _isLoadingFinished.value = false
                _errorMessage.value = "Terjadi kesalahan: ${e.message}"
            }
        }
    }

    fun getUpcomingEvents() {
        _isLoadingUpcoming.value = true
        viewModelScope.launch {
            try {
                val response: Response<EventResponse> = ApiConfig.getApiService().getUpcomingEvents()
                _isLoadingUpcoming.value = false
                if (response.isSuccessful) {
                    _upcomingEvents.value = response.body()?.listEvents ?: emptyList()
                } else {
                    _errorMessage.value = "Gagal mendapatkan data: ${response.message()}"
                }
            } catch (e: Exception) {
                _isLoadingUpcoming.value = false
                _errorMessage.value = "Terjadi kesalahan: ${e.message}"
            }
        }
    }

}
