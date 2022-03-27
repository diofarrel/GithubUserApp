package com.example.githubuserapp.ui.favorite

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.data.local.FavoriteUser
import com.example.githubuserapp.databinding.ActivityFavoriteBinding
import com.example.githubuserapp.ui.detail.DetailActivity

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var favAdapter: FavoriteUserAdapter
    private lateinit var favoriteViewModel: FavoriteViewModel

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        favAdapter = FavoriteUserAdapter()
        favAdapter.notifyDataSetChanged()

        favoriteViewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]

        favAdapter.setOnItemClickCallback(object : FavoriteUserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: FavoriteUser) {
                Intent(this@FavoriteActivity, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.EXTRA_USER, data.login)
                    it.putExtra(DetailActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailActivity.EXTRA_AVATAR, data.avatar_url)
                    startActivity(it)
                }
            }
        })

        binding.apply {
            rvFavorite.setHasFixedSize(true)
            rvFavorite.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            rvFavorite.adapter = favAdapter
        }

        favoriteViewModel.getFavoriteUser()?.observe(this@FavoriteActivity) {
            if (it.isNotEmpty()) {
                binding.icFavoritePeople.visibility = View.GONE
                val list = mapList(it)
                favAdapter.setList(list)
            } else {
                binding.icFavoritePeople.visibility = View.VISIBLE
                binding.rvFavorite.visibility = View.GONE
            }
        }

        supportActionBar?.let {
            it.title = "Your Favorite"
        }
    }

    private fun mapList(users: List<FavoriteUser>): ArrayList<FavoriteUser> {
        val listUsers = ArrayList<FavoriteUser>()
        for (user in users) {
            val userMapped = FavoriteUser(
                user.id,
                user.login,
                user.avatar_url
            )
            listUsers.add(userMapped)
        }
        return listUsers
    }
}