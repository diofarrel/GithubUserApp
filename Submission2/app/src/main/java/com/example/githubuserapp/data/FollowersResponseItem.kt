package com.example.githubuserapp.data

import com.google.gson.annotations.SerializedName

data class FollowersResponseItem(

	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

)