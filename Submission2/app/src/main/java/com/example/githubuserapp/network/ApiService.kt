package com.example.githubuserapp.network

import com.example.githubuserapp.BuildConfig
import com.example.githubuserapp.data.*
import retrofit2.http.*
import retrofit2.Call

interface ApiService {

    @GET("search/users")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    fun getUser (
        @Query("q") q : String
    ) : Call<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    fun getDetailUser (
        @Path("username") username: String
    ) : Call<DetailResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    fun getFollowers (
        @Path("username") username: String
    ) : Call<List<FollowersResponseItem>>

    @GET("users/{username}/following")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    fun getFollowing (
        @Path("username") username: String
    ) : Call<List<FollowingResponseItem>>
}