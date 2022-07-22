package com.example.githubuserapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserapp.apiresponse.*
import com.example.githubuserapp.event.Event
import com.example.githubuserapp.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _searchedUserDetails = MutableLiveData<List<DetailUserResponse>>()
    val searchedUserDetail: LiveData<List<DetailUserResponse>> get() = _searchedUserDetails

    private val _allUsers = MutableLiveData<List<ListUsersResponseItem>>()
    val allUsers: LiveData<List<ListUsersResponseItem>> get() = _allUsers

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> get() = _isError

    var isSnackBarShown: Event<Boolean> = Event(false)

    var errorMessage: String = ""
        private set


    fun getSearchedUsers(username : String){
        _isError.value = false
        _isLoading.value = true

        val client = ApiConfig.getApiService().getSearchUser(username)
        client.enqueue(object : Callback<SearchUserResponse>{
            override fun onFailure(call: Call<SearchUserResponse>, t: Throwable) {
                onError(t.message)
                t.printStackTrace()
            }

            override fun onResponse(
                call: Call<SearchUserResponse>,
                response: Response<SearchUserResponse>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null){

                }
            }
        })

    }
    fun getAllusers(){
        _isLoading.value = true
        _isError.value = false


        val client = ApiConfig.getApiService().getListUsers()
        client.enqueue(object : Callback<List<ListUsersResponseItem>>{
            override fun onFailure(call: Call<List<ListUsersResponseItem>>, t: Throwable) {
                onError(t.message)
                t.printStackTrace()
            }

            override fun onResponse(
                call: Call<List<ListUsersResponseItem>>,
                response: Response<List<ListUsersResponseItem>>
            ) {
                _isError.value = false
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null){
                    _allUsers.postValue(responseBody)
                }else{

                    onError(response.code().toString())
                }
            }
        })

    }

    private fun onError(inputMessage: String?) {
        var message = inputMessage
        message = if (message.isNullOrBlank() or message.isNullOrEmpty()) ApiConfig.error
        else message

        errorMessage = StringBuilder("ERROR: ")
            .append("$message some data may not displayed properly").toString()

        _isError.value = true
        _isLoading.value = false
    }

    companion object {
        private const val TAG = "MainViewModel"
        private const val NO_RESULT = "No Result"
    }
}
