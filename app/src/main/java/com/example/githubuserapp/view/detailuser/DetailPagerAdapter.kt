package com.example.githubuserapp.view.detailuser

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class DetailPagerAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity){
    var fragment = FollowListFragment()
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        fragment = FollowListFragment()
        when(position){
            1 -> DetailPagerAdapterState.FOLLOWER
            2 -> DetailPagerAdapterState.FOLLOWING
        }
        return fragment
    }
}

enum class DetailPagerAdapterState{
    FOLLOWING,
    FOLLOWER
}