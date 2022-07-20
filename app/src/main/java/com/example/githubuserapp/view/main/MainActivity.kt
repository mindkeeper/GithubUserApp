package com.example.githubuserapp.view.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.R
import com.example.githubuserapp.apiresponse.DetailUserResponse
import com.example.githubuserapp.apiresponse.UserDetail
import com.example.githubuserapp.databinding.ActivityMainBinding
import com.example.githubuserapp.view.detailuser.DetailUserActivity
import com.example.githubuserapp.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        subscribe()
        mainViewModel.getAllUsers()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    mainViewModel.getSearchedUsers(it)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }

    private fun subscribe(){
        mainViewModel.isLoading.observe(this){
            showloading(it)
        }

        mainViewModel.searchedUserDetail.observe(this){
            showRecycleList(it)
        }
    }

    private fun showRecycleList(response: List<DetailUserResponse>) {

        val listUserAdapter = ListUserAdapter(response)
        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback{
            override fun onItemClick(user: DetailUserResponse) {
                val userDetails = UserDetail().apply {
                    name = user.name
                    login = user.login
                    followers = user.followers
                    following = user.following
                    repository = user.reposUrl
                    company = user.company
                    avatarUrl = user.avatarUrl
                    location = user.location
                }
                val detailIntent = Intent(this@MainActivity, DetailUserActivity::class.java)
                detailIntent.putExtra(DetailUserActivity.EXTRA_USER, userDetails)
                startActivity(detailIntent)
            }
        })
        binding.rvUserList.layoutManager = LinearLayoutManager(this)
        binding.rvUserList.adapter = listUserAdapter

    }


    private fun showloading(isLoading : Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}