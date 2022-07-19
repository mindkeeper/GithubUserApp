package com.example.githubuserapp.view.detailuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.githubuserapp.R

class DetailUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)
    }

    companion object{
        const val EXTRA_USER = "extra_user"
    }
}