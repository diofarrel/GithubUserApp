package com.example.githubuserapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuserapp.databinding.ActivityDetailBinding
import com.example.githubuserapp.viewModel.MainViewModel
import com.example.githubuserapp.viewModel.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.lang.StringBuilder

class DetailActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val user = intent.getStringExtra(EXTRA_USER)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = user
        }

        mainViewModel.detailUser.observe(this) { detailResponse ->
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
            mainViewModel.setDetailUser(user)
        }

        mainViewModel.isLoading.observe(this) {
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
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}