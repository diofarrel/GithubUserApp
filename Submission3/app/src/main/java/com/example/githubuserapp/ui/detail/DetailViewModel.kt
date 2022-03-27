package com.example.githubuserapp.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.githubuserapp.data.local.FavoriteUser
import com.example.githubuserapp.data.local.FavoriteUserDAO
import com.example.githubuserapp.data.local.FavoriteUserRoomDatabase
import com.example.githubuserapp.data.remote.DetailResponse
import com.example.githubuserapp.network.ApiConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application): AndroidViewModel(application) {
    private var favUserDAO: FavoriteUserDAO?
    private var favUserDb: FavoriteUserRoomDatabase? = FavoriteUserRoomDatabase.getDatabase(application)

    init {
        favUserDAO = favUserDb?.favoriteUserDAO()
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _detailUser = MutableLiveData<DetailResponse>()
    val detailUser : LiveData<DetailResponse> = _detailUser

    fun setDetailUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailUser.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun addToFavorite(id: Int, username: String, avatar: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = FavoriteUser(id, username, avatar)
            favUserDAO?.addFav(user)
        }
    }

    suspend fun checkUser(id: Int) = favUserDAO?.checkUser(id)

    fun removeFromFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            favUserDAO?.removeFav(id)
        }
    }

    companion object {
        private const val TAG = "DetailViewModel"
    }
}