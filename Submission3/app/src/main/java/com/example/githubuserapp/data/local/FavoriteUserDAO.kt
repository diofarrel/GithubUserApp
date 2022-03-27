package com.example.githubuserapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteUserDAO {
    @Insert
    suspend fun addFav(favoriteUser: FavoriteUser)

    @Query("SELECT * FROM favorite")
    fun getFav(): LiveData<List<FavoriteUser>>

    @Query("SELECT count(*) FROM favorite WHERE favorite.id = :id")
    suspend fun checkUser(id: Int): Int

    @Query("DELETE FROM favorite WHERE favorite.id = :id")
    suspend fun removeFav(id: Int): Int

}