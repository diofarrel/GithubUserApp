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
import com.example.githubuserapp.data.FollowingResponseItem
import com.example.githubuserapp.databinding.FragmentFollowingBinding
import com.example.githubuserapp.viewModel.FollowingAdapter
import com.example.githubuserapp.viewModel.FollowingViewModel

class FollowingFragment : Fragment() {

    private lateinit var followingViewModel: FollowingViewModel
    private lateinit var rvUser: RecyclerView

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvUser = binding.rvFollowing
        rvUser.setHasFixedSize(true)

        followingViewModel = ViewModelProvider(this)[FollowingViewModel::class.java]

        followingViewModel.listFollowing.observe(viewLifecycleOwner) { listFollowing ->
            showRecyclerList(listFollowing)
        }

        val user = requireActivity().intent.getStringExtra(DetailActivity.EXTRA_USER)
        if (user != null) {
            followingViewModel.findFollowing(user)
        }

        followingViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return (binding.root)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showRecyclerList(listUser : List<FollowingResponseItem>) {
        rvUser.layoutManager = LinearLayoutManager(context)
        val gitFollowingAdapter = FollowingAdapter(listUser)
        rvUser.adapter = gitFollowingAdapter

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}