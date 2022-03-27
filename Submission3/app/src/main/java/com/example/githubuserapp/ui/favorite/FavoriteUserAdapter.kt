package com.example.githubuserapp.ui.favorite

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserapp.R
import com.example.githubuserapp.data.local.FavoriteUser
import java.lang.StringBuilder

class FavoriteUserAdapter : RecyclerView.Adapter<FavoriteUserAdapter.ListViewHolder>() {

    private val listFavorites = ArrayList<FavoriteUser>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
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
        val dataUser = listFavorites[position]
        Glide.with(holder.itemView.context)
            .load(dataUser.avatar_url)
            .circleCrop()
            .into(holder.imgPhoto)
        holder.tvUsername.text = StringBuilder("@").append(dataUser.login)

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listFavorites[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = listFavorites.size

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: ArrayList<FavoriteUser>) {
        listFavorites.clear()
        listFavorites.addAll(list)
        notifyDataSetChanged()
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: FavoriteUser)
    }
}