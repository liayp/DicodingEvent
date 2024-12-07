package com.example.dicodingevent.ui.detail

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.*
import com.example.dicodingevent.data.local.FavoriteEntity
import com.example.dicodingevent.data.repository.FavoriteRepository
import com.example.dicodingevent.data.response.DetailResponse
import com.example.dicodingevent.data.response.Event
import com.example.dicodingevent.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Response

class DetailViewModel(private val favoriteRepository: FavoriteRepository) : ViewModel() {

    private val _eventDetails = MutableLiveData<Event?>()
    val eventDetails: LiveData<Event?> get() = _eventDetails

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    fun getEventDetails(eventId: Int) {
        viewModelScope.launch {
            try {
                val response: Response<DetailResponse> = ApiConfig.getApiService().getEventDetails(eventId)

                if (response.isSuccessful) {
                    val event = response.body()?.event
                    _eventDetails.value = event
                    if (event != null) {
                        checkIfFavorite(event.id ?: -1)
                    }
                }
            } catch (e: Exception) {
                // Handle error if necessary
            }
        }
    }

    fun checkIfFavorite(eventId: Int) {
        viewModelScope.launch {
            favoriteRepository.getFavoriteById(eventId).observeForever { favoriteEntity ->
                _isFavorite.value = favoriteEntity != null
            }
        }
    }

    fun toggleFavorite(event: Event, context: Context) {
        viewModelScope.launch {
            val newStatus = !(_isFavorite.value ?: false)
            val eventId = event.id ?: return@launch
            val favoriteEntity = FavoriteEntity(eventId, event.name ?: "", event.ownerName ?: "", event.mediaCover ?: "")

            try {
                if (newStatus) {
                    favoriteRepository.addToFavorite(favoriteEntity)
                    _isFavorite.value = true
                    showToast(context, "Event added to favorites: ${event.name}")
                } else {
                    favoriteRepository.removeFromFavorite(favoriteEntity)
                    _isFavorite.value = false
                    showToast(context, "Event removed from favorites: ${event.name}")
                }
            } catch (e: Exception) {
                if (newStatus) {
                    showToast(context, "Error adding event to favorites: ${e.message}")
                } else {
                    showToast(context, "Error removing event from favorites: ${e.message}")
                }
            }
        }
    }

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
