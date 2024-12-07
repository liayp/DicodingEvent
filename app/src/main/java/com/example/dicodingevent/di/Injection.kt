package com.example.dicodingevent.di

import android.content.Context
import com.example.dicodingevent.data.local.EventDatabase
import com.example.dicodingevent.data.repository.FavoriteRepository

object Injection {
    fun provideRepository(context: Context): FavoriteRepository {
        val database = EventDatabase.getInstance(context)
        return FavoriteRepository(database.favoriteDao())
    }

}
