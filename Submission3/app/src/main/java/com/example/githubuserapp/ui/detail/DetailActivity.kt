package com.example.githubuserapp.ui.detail

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuserapp.R
import com.example.githubuserapp.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.StringBuilder

class DetailActivity : AppCompatActivity() {

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        detailViewModel = ViewModelProvider(this)[DetailViewModel::class.java]

        val user = intent.getStringExtra(EXTRA_USER)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatar = intent.getStringExtra(EXTRA_AVATAR)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = user
        }

        detailViewModel.detailUser.observe(this) { detailResponse ->
            Glide.with(this)
                .load(detailResponse.avatarUrl)
                .circleCrop()
                .into(binding.detailImage)

            binding.apply {
                detailName.text = detailResponse.name
                detailUsername.text = StringBuilder("@").append(detailResponse.login)
                detailRepository.text = StringBuilder().append(detailResponse.publicRepos).append(" Repository")
                detailLocation.text = detailResponse.location
                detailCompany.text = detailResponse.company
                detailFollowers.text = detailResponse.followers.toString()
                detailFollowing.text = detailResponse.following.toString()
            }

        }

        if (user != null) {
            detailViewModel.setDetailUser(user)
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.btnShare.setOnClickListener {
            val sendName: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "His name is $user")
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendName, null)
            startActivity(shareIntent)
        }

        var isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = detailViewModel.checkUser(id)
            withContext(Dispatchers.Main) {
                if (count != null) {
                    if (count>0) {
                        binding.detailFabFavorite.imageTintList = ColorStateList.valueOf(Color.rgb(247, 106, 123))
                        isChecked = true
                    } else {
                        binding.detailFabFavorite.imageTintList = ColorStateList.valueOf(Color.rgb(255, 255, 255))
                        isChecked = false
                    }
                }
            }
        }

        binding.detailFabFavorite.setOnClickListener {
            isChecked = !isChecked
            if (isChecked) {
                if (user != null) {
                    if (avatar != null) {
                        detailViewModel.addToFavorite(id, user, avatar)
                        binding.detailFabFavorite.imageTintList = ColorStateList.valueOf(Color.rgb(247, 106, 123))
                    }
                }
            } else {
                detailViewModel.removeFromFavorite(id)
                binding.detailFabFavorite.imageTintList = ColorStateList.valueOf(Color.rgb(255, 255, 255))
            }
        }

        setupViewPager()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setupViewPager() {
        val sectionsPagerAdapter = ViewPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_AVATAR = "extra_avatar"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}