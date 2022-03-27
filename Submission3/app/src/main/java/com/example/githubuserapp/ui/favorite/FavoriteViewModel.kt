package com.example.githubuserapp.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.githubuserapp.data.local.FavoriteUser
import com.example.githubuserapp.data.local.FavoriteUserDAO
import com.example.githubuserapp.data.local.FavoriteUserRoomDatabase

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private var favUserDAO: FavoriteUserDAO?
    private var favUserDb: FavoriteUserRoomDatabase? = FavoriteUserRoomDatabase.getDatabase(application)

    init {
        favUserDAO = favUserDb?.favoriteUserDAO()
    }

    fun getFavoriteUser(): LiveData<List<FavoriteUser>>? {
        return favUserDAO?.getFav()
    }
}