package com.example.githubuserapp.viewModel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserapp.R
import com.example.githubuserapp.data.FollowingResponseItem
import java.lang.StringBuilder

class FollowingAdapter(private val listFollowing: List<FollowingResponseItem>) :
    RecyclerView.Adapter<FollowingAdapter.ListViewHolder>() {
    class ListViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
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
        val dataFollowing = listFollowing[position]
        Glide.with(holder.itemView.context)
            .load(dataFollowing.avatarUrl)
            .circleCrop()
            .into(holder.imgPhoto)
        holder.tvUsername.text = StringBuilder("@").append(dataFollowing.login)
    }

    override fun getItemCount(): Int = listFollowing.size
}