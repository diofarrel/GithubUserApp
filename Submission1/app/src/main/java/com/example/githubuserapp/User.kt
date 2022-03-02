package com.example.githubuserapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    var name: String,
    var username: String,
    var image: Int,
    var company: String,
    var location: String,
    var repository: String,
    var followers: String,
    var following: String
    ) : Parcelable