package com.example.dicodingevent.data.repository

import androidx.lifecycle.LiveData
import com.example.dicodingevent.data.local.FavoriteDAO
import com.example.dicodingevent.data.local.FavoriteEntity

class FavoriteRepository(private val dao: FavoriteDAO) {

    fun getAllFavorites(): LiveData<List<FavoriteEntity>> = dao.getAllFavorites()

    fun getFavoriteById(id: Int): LiveData<FavoriteEntity?> = dao.getFavoriteById(id)

    suspend fun addToFavorite(event: FavoriteEntity) = dao.insert(event)

    suspend fun removeFromFavorite(event: FavoriteEntity) = dao.delete(event)
}
