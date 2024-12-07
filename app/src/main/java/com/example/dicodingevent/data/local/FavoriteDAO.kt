package com.example.dicodingevent.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: FavoriteEntity)

    @Delete
    suspend fun delete(event: FavoriteEntity)

    @Query("SELECT * FROM favorite_event")
    fun getAllFavorites(): LiveData<List<FavoriteEntity>>

    @Query("SELECT * FROM favorite_event WHERE id = :id")
    fun getFavoriteById(id: Int): LiveData<FavoriteEntity?>
}
