package com.example.githubuserapp.view.detailuser

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubuserapp.R

class DetailPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    private val tab = listOf(
        activity.getString(R.string.follower), activity.getString(R.string.following)
    )

    override fun getItemCount(): Int {
        return tab.size
    }

    override fun createFragment(position: Int): Fragment {
        return FollowListFragment.newInstance(tab[position])
    }
}
