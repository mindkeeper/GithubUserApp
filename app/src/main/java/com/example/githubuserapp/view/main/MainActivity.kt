package com.example.githubuserapp.view.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.R
import com.example.githubuserapp.apiresponse.DetailUserResponse
import com.example.githubuserapp.apiresponse.ListUsersResponseItem
import com.example.githubuserapp.apiresponse.UserDetail
import com.example.githubuserapp.databinding.ActivityMainBinding
import com.example.githubuserapp.view.detailuser.DetailUserActivity
import com.example.githubuserapp.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.emptyText.visibility = View.GONE
        subscribe()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
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

    private fun subscribe() {
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        mainViewModel.isError.observe(this) {
            if (it) {
                binding.emptyText.visibility = View.VISIBLE
                showError()
            }
        }

        mainViewModel.allUsers.observe(this){
            showRecycleList(it)
        }

        mainViewModel.getAllusers()
    }

    private fun showRecycleList(response: List<ListUsersResponseItem>) {

        val listUserAdapter = ListUserAdapter(response)
        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback{
            override fun onItemClick(user: String?) {
                if (user == null){
                    Log.d("MainActivity",  "null : $user")
                }else Log.d("MainActivity",  user)

                val detailIntent = Intent(this@MainActivity, DetailUserActivity::class.java)
                detailIntent.putExtra("username", user.toString())
                startActivity(detailIntent)
            }
        })
        binding.rvUserList.layoutManager = LinearLayoutManager(this)
        binding.rvUserList.adapter = listUserAdapter

    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError() {
        mainViewModel.isSnackBarShown.getContentIfNotHandled()?.let {
            Snackbar.make(binding.root, mainViewModel.errorMessage, Snackbar.LENGTH_SHORT).show()
        }
    }
}