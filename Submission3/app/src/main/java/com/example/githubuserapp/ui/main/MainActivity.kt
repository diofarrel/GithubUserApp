package com.example.githubuserapp.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuserapp.R
import com.example.githubuserapp.ui.setting.SettingActivity
import com.example.githubuserapp.data.remote.ItemsItem
import com.example.githubuserapp.databinding.ActivityMainBinding
import com.example.githubuserapp.ui.detail.DetailActivity
import com.example.githubuserapp.ui.favorite.FavoriteActivity
import com.example.githubuserapp.ui.setting.SettingPreferences
import com.example.githubuserapp.ui.setting.SettingViewModel
import com.example.githubuserapp.ui.setting.SettingViewModelFactory
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private val dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var rvUser: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvUser = binding.rvUser
        rvUser.setHasFixedSize(true)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        mainViewModel.listUser.observe(this) { listUser ->
            if (listUser.isEmpty()) {
                Snackbar.make(
                    binding.root,
                    R.string.notfound,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            showRecyclerList(listUser)
        }

        mainViewModel.findUser("")

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val pref = SettingPreferences.getInstance(dataStore)
        val setupViewModel = ViewModelProvider(this,
            SettingViewModelFactory(pref))[SettingViewModel::class.java]

        Handler(Looper.getMainLooper()).postDelayed({
            setupViewModel.getThemeSettings().observe(this
            ) { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }, 500)

        supportActionBar?.let {
            it.title = "Github User"
        }
    }

    private fun showRecyclerList(listUser : List<ItemsItem>) {
        rvUser.visibility = View.VISIBLE
        binding.icSearchPeople.visibility = View.GONE

        rvUser.layoutManager = LinearLayoutManager(this)
        val gitUserAdapter = UserAdapter(listUser)
        rvUser.adapter = gitUserAdapter

        gitUserAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(user: ItemsItem) {
        Intent(this, DetailActivity::class.java).also {
            it.putExtra(DetailActivity.EXTRA_USER, user.login)
            it.putExtra(DetailActivity.EXTRA_ID, user.id)
            it.putExtra(DetailActivity.EXTRA_AVATAR, user.avatarUrl)
            startActivity(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        val searchIcon = menu.findItem(R.id.search)
        val settingIcon = menu.findItem(R.id.setting)
        val favoriteIcon = menu.findItem(R.id.favorite)

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.findUser(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        searchIcon.setOnActionExpandListener(object : MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(p0: MenuItem): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem): Boolean {
                mainViewModel.findUser("")
                rvUser.visibility = View.GONE
                binding.icSearchPeople.visibility = View.VISIBLE
                return true
            }
        })

        settingIcon.setOnMenuItemClickListener { item ->
            when (item?.itemId) {
                R.id.setting -> {
                    val setting = Intent(this@MainActivity, SettingActivity::class.java)
                    startActivity(setting)
                    true
                }
                else -> false
            }
        }

        favoriteIcon.setOnMenuItemClickListener { item ->
            when (item?.itemId) {
                R.id.favorite -> {
                    val favorite = Intent(this@MainActivity, FavoriteActivity::class.java)
                    startActivity(favorite)
                    true
                }
                else -> false
            }
        }

        return true
    }
}