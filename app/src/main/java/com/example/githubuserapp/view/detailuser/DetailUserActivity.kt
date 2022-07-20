package com.example.githubuserapp.view.detailuser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.StringRes
import com.bumptech.glide.Glide
import com.example.githubuserapp.R
import com.example.githubuserapp.apiresponse.UserDetail
import com.example.githubuserapp.databinding.ActivityDetailUserBinding
import com.example.githubuserapp.viewmodel.DetailViewModel

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    val detailViewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.getParcelableExtra<UserDetail>(EXTRA_USER) as UserDetail
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