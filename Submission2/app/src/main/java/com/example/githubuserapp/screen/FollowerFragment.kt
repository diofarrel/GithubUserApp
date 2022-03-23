package com.example.githubuserapp.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuserapp.DetailActivity
import com.example.githubuserapp.viewModel.FolllowerAdapter
import com.example.githubuserapp.data.FollowersResponseItem
import com.example.githubuserapp.databinding.FragmentFollowerBinding
import com.example.githubuserapp.viewModel.FollowerViewModel

class FollowerFragment : Fragment() {

    private lateinit var followerViewModel: FollowerViewModel
    private lateinit var rvUser: RecyclerView

    private var _binding: FragmentFollowerBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvUser = binding.rvFollowers
        rvUser.setHasFixedSize(true)

        followerViewModel = ViewModelProvider(this)[FollowerViewModel::class.java]

        followerViewModel.listFollowers.observe(viewLifecycleOwner) { listFollowers ->
            showRecyclerList(listFollowers)
        }

        val user = requireActivity().intent.getStringExtra(DetailActivity.EXTRA_USER)
        if (user != null) {
            followerViewModel.findFollowers(user)
        }

        followerViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowerBinding.inflate(inflater, container, false)
        return (binding.root)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showRecyclerList(listUser : List<FollowersResponseItem>) {
        rvUser.layoutManager = LinearLayoutManager(context)
        val gitFollowerAdapter = FolllowerAdapter(listUser)
        rvUser.adapter = gitFollowerAdapter

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}