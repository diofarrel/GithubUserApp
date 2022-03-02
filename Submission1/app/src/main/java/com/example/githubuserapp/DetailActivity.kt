package com.example.githubuserapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.githubuserapp.databinding.ActivityDetailBinding
import java.lang.StringBuilder

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = "User Profile"
        }

        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User

        Glide.with(this)
            .load(user.image)
            .circleCrop()
            .into(binding.detailImage)
        binding.detailName.text = user.name
        binding.detailUsername.text = StringBuilder("@").append(user.username)
        binding.detailRepository.text = user.repository + " Repository"
        binding.detailLocation.text = user.location
        binding.detailCompany.text = user.company
        binding.detailFollowers.text = user.followers
        binding.detailFollowing.text = user.following

        binding.btnShare.setOnClickListener {
            val sendName: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "His name is ${user.name}")
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendName, null)
            startActivity(shareIntent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val EXTRA_USER = "extra_user"
    }
}