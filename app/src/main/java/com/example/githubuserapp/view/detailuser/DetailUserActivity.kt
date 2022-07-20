package com.example.githubuserapp.view.detailuser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuserapp.R
import com.example.githubuserapp.apiresponse.UserDetail
import com.example.githubuserapp.databinding.ActivityDetailUserBinding
import com.example.githubuserapp.viewmodel.DetailViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    val detailViewModel by viewModels<DetailViewModel>()
    private lateinit var sectionPager: DetailPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.getParcelableExtra<UserDetail>(EXTRA_USER) as UserDetail
        sectionPagerAdapter()

        detailViewModel.isSnackbarShown.hasBeenHandled = false
        detailViewModel.getUserFollower(user.login!!)
        detailViewModel.getUserFollowing(user.login!!)
        supportActionBar?.title = user.login
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
    private fun sectionPagerAdapter() {
        sectionPager = DetailPagerAdapter(this)
        val viewPager : ViewPager2 = binding.viewPager
        viewPager.adapter = sectionPager

        val tabs : TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager){tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])

        }.attach()
        supportActionBar?.elevation = 0f
    }

    private fun dataBinding(user : UserDetail){
        with(binding){
            with(user){
                tvRealName.text = user.name
                tvUsername.text = StringBuilder("@").append(user.login).toString()
                tvLocation.text = user.location
                tvCompany.text = user.location
                tvRepository.text = user.repository
                Glide.with(applicationContext)
                    .load(user.avatarUrl)
                    .into(imgUser)
            }
        }
    }

    companion object{
        const val EXTRA_USER = "extra_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_title_1,
            R.string.tab_title_2
        )
    }
}