package com.example.githubuserapp.data.remote

import com.google.gson.annotations.SerializedName

data class FollowingResponseItem(

	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

)