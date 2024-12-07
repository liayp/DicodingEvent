package com.example.dicodingevent.ui.favorite

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.local.FavoriteEntity
import com.example.dicodingevent.data.repository.FavoriteRepository

class FavoriteViewModel(favoriteRepository: FavoriteRepository) : ViewModel() {

    val favoriteList: LiveData<List<FavoriteEntity>> = favoriteRepository.getAllFavorites()

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    companion object {
        private const val TAG = "FavoriteViewModel"
    }

    init {
        fetchFavorites()
    }

    private fun fetchFavorites() {
        try {
            // Tidak ada operasi yang diperlukan di sini karena data sudah otomatis ter-update
        } catch (e: Exception) {
            _errorMessage.value = e.message
            Log.e(TAG, "Error fetching favorites: ${e.message}")
        }
    }
}
