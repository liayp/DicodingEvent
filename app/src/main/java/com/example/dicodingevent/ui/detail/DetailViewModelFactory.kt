package com.example.dicodingevent.ui.detail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingevent.data.repository.FavoriteRepository
import com.example.dicodingevent.di.Injection

@Suppress("UNCHECKED_CAST")
class DetailViewModelFactory(private val repository: FavoriteRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        fun getInstance(context: Context): DetailViewModelFactory {
            val repository = Injection.provideRepository(context)
            return DetailViewModelFactory(repository)
        }
    }
}
