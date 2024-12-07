package com.example.dicodingevent.ui.favorite

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingevent.data.repository.FavoriteRepository
import com.example.dicodingevent.di.Injection

@Suppress("UNCHECKED_CAST")
class FavoriteViewModelFactory(private val repository: FavoriteRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        fun getInstance(context: Context): FavoriteViewModelFactory {
            val repository = Injection.provideRepository(context)
            return FavoriteViewModelFactory(repository)
        }
    }
}
