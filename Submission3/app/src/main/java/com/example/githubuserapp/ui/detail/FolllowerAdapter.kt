package com.example.githubuserapp.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserapp.R
import com.example.githubuserapp.data.remote.FollowersResponseItem
import java.lang.StringBuilder

class FolllowerAdapter(private val listFollower: List<FollowersResponseItem>) :
    RecyclerView.Adapter<FolllowerAdapter.ListViewHolder>() {

    class ListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var imgPhoto: ImageView = itemView.findViewById(R.id.iv_user)
        var tvUsername: TextView = itemView.findViewById(R.id.tv_username)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_user, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val dataFollowers = listFollower[position]
        Glide.with(holder.itemView.context)
            .load(dataFollowers.avatarUrl)
            .circleCrop()
            .into(holder.imgPhoto)
        holder.tvUsername.text = StringBuilder("@").append(dataFollowers.login)
    }

    override fun getItemCount(): Int = listFollower.size
}