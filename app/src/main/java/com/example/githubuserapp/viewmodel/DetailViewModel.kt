package com.example.githubuserapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserapp.apiresponse.DetailUserResponse
import com.example.githubuserapp.apiresponse.ItemsItem
import com.example.githubuserapp.apiresponse.ListUsersResponseItem
import com.example.githubuserapp.event.Event
import com.example.githubuserapp.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.StringBuilder

class DetailViewModel : ViewModel() {

    private val _listFollower = MutableLiveData<List<ListUsersResponseItem>>()
    val listFollower: LiveData<List<ListUsersResponseItem>>get() = _listFollower

    private val _listFollowing = MutableLiveData<List<ListUsersResponseItem>>()
    val listFollowing: LiveData<List<ListUsersResponseItem>>get() = _listFollowing

    private val _userDetail = MutableLiveData<DetailUserResponse>()
    val userDetail : LiveData<DetailUserResponse> get() = _userDetail

    private var _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> get() = _isError

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    val isSnackbarShown: Event<Boolean> = Event(false)

    var errorMessage: String = ""
        private set

    fun getUserFollower(username : String){
        _isError.value = false

        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<ListUsersResponseItem>>{
            override fun onFailure(call: Call<List<ListUsersResponseItem>>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
                t.printStackTrace()
            }

            override fun onResponse(
                call: Call<List<ListUsersResponseItem>>,
                response: Response<List<ListUsersResponseItem>>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null){
                    _listFollower.postValue(responseBody)
                }else{
                    Log.d(TAG, "onResponse: ${response.code()}")
                }
            }
        })

    }

    fun getUserFollowing(username : String){
        _isError.value = false

        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<ListUsersResponseItem>>{
            override fun onFailure(call: Call<List<ListUsersResponseItem>>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
                t.printStackTrace()
            }

            override fun onResponse(
                call: Call<List<ListUsersResponseItem>>,
                response: Response<List<ListUsersResponseItem>>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null){
                    _listFollowing.postValue(responseBody)
                }else{
                    Log.d(TAG, "onResponse: ${response.code()}")
                }
            }
        })

    }

    fun getDetailUser(username: String){
        val client = ApiConfig.getApiService().getUserDetail(username)
        client.enqueue(object : Callback<DetailUserResponse>{
            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
                t.printStackTrace()
            }

            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null){
                    _userDetail.postValue(responseBody)
                }else{
                    Log.d(TAG, "onResponse : ${response.code()}")
                }
            }
        })
    }

    private fun onError(e : String?){
        var message = e
        if (message.isNullOrBlank()||message.isNullOrEmpty()){
            message = ApiConfig.error
        }

        errorMessage = StringBuilder("ERROR: ")
            .append("$message, data cannot be displayed").toString()

        _isLoading.value = false
        _isError.value = true
    }
    companion object{
        const val TAG = "DetailViewModel"
    }
}