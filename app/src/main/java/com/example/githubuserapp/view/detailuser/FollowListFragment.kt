package com.example.githubuserapp.view.detailuser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.databinding.FragmentFollowListBinding
import com.example.githubuserapp.view.main.ListUserAdapter
import com.google.android.material.snackbar.Snackbar

class FollowListFragment : Fragment() {


    private var _binding : FragmentFollowListBinding? = null
    val binding get() = _binding!!
    lateinit var state : DetailPagerAdapterState

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribe()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    private fun subscribe() {
        val parent = activity as DetailUserActivity
        binding.rvUserList.layoutManager = LinearLayoutManager(parent)

        when(state){
            DetailPagerAdapterState.FOLLOWER -> {
                parent.detailViewModel.listFollower.observe(parent){
                    binding.rvUserList.adapter = ListUserAdapter(it)
                }
            }
            DetailPagerAdapterState.FOLLOWING -> {
                parent.detailViewModel.listFollowing.observe(parent){
                    binding.rvUserList.adapter = ListUserAdapter(it)
                }
            }
        }
        parent.detailViewModel.isLoading.observe(parent){
            with(binding){
                if (it) progressBar.visibility = View.VISIBLE
                else progressBar.visibility = View.GONE
            }
        }

        parent.detailViewModel.isError.observe(parent){
            if (it){
                parent.detailViewModel.isSnackbarShown.getContentIfNotHandled()?.let {
                    Snackbar.make(binding.root, parent.detailViewModel.errorMessage, Snackbar.LENGTH_SHORT).show()
                }
            }
        }

    }


}