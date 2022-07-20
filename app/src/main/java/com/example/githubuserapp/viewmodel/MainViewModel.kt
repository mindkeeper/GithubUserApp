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
    val isLoading : LiveData<Boolean> get()= _isLoading

    private val _searchedUserDetails = MutableLiveData<List<DetailUserResponse>>()
    val searchedUserDetail : LiveData<List<DetailUserResponse>> get() = _searchedUserDetails

    private val _allusers = MutableLiveData<List<DetailUserResponse>>()
    val allUsers: LiveData<List<DetailUserResponse>> get() = _allusers

    private val _isError = MutableLiveData<Boolean>()
    val isError : LiveData<Boolean> get() = _isError

    var isSnackbarShown: Event<Boolean> = Event(false)

    var errorMessage : String = ""
        private set

    
    fun getAllUsers(){
        _isLoading.value = true
        _isError.value = false

        val client = ApiConfig.getApiService().getListUsers()
        client.enqueue(object : Callback<List<ListUsersResponseItem>>{

            override fun onFailure(call: Call<List<ListUsersResponseItem>>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.d(TAG, "onFailure getalluser : ${t.message}")
                t.printStackTrace()
            }

            override fun onResponse(
                call: Call<List<ListUsersResponseItem>>,
                response: Response<List<ListUsersResponseItem>>
            ) {
                _isLoading.value = false
                _isError.value = false
                val responseBody = response.body()
                Log.d(TAG, "success : $responseBody")
                if (response.isSuccessful && responseBody != null){
                    getAllUsersDetails(responseBody)
                }else{
                    Log.d(TAG, "onFailure getall user : ${response.message()}")
                }

            }
        })
    }



    fun getSearchedUsers(username : String){
        _isLoading.value = true
        _isError.value = false
        val client = ApiConfig.getApiService().getSearchUser(username)
        client.enqueue(object : Callback<SearchUserResponse>{
            override fun onFailure(call: Call<SearchUserResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.d(TAG, "onFailure getsearch user: ${t.message}")
                t.printStackTrace()
            }

            override fun onResponse(
                call: Call<SearchUserResponse>,
                response: Response<SearchUserResponse>
            ) {
                _isLoading.value = false
                _isError.value = false
                val responseBody = response.body()
                Log.d(TAG, "success : $responseBody")
                if (response.isSuccessful && responseBody != null){
                    getSearchedUserDetail(responseBody.items as List<ItemsItem>)
                }else{
                    Log.d(TAG, "onFailure getsearchuser : ${response.message()}")
                }
            }
        })
    }

    private fun getSearchedUserDetail(listItem: List<ItemsItem>) {
        _isLoading.value = true
        _isLoading.value = false
        val listUserDetail = ArrayList<DetailUserResponse>()
        if (listItem.isEmpty()){
            _isLoading.value = false
            errorMessage = NO_RESULT
            _isError.value = true
            _searchedUserDetails.postValue(listUserDetail)

        }

        listItem.forEach{
            val client = ApiConfig.getApiService().getUserDetail(it.login!!)
            client.enqueue(object : Callback<DetailUserResponse>{

                override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                    _isLoading.value = false
                    _isLoading.value = true
                    Log.d(TAG, "onFaillure get detailed search : ${t.message}")
                    t.printStackTrace()
                }

                override fun onResponse(
                    call: Call<DetailUserResponse>,
                    response: Response<DetailUserResponse>
                ) {
                    _isLoading.value = false
                    _isError.value = false
                    val responseBody = response.body()
                    Log.d(TAG, "success : $responseBody")
                    if (response.isSuccessful && responseBody != null){
                        _isLoading.value = false
                        _isError.value = false
                        listUserDetail.add(responseBody)
                        if (listUserDetail.count() == listItem.count()){
                            _searchedUserDetails.postValue(listUserDetail)
                        }else{
                            Log.d(TAG, "onFailure get detailed search: ${response.code()}")
                        }

                    }
                }
            })
        }
    }


    private fun getAllUsersDetails(listUsers : List<ListUsersResponseItem>){
        _isLoading.value = true
        _isLoading.value = false
        val listUserDetail = ArrayList<DetailUserResponse>()
        if (listUsers.isEmpty()){
            _isLoading.value = false
            errorMessage = NO_RESULT
            _isError.value = true
            _allusers.postValue(listUserDetail)

        }

        listUsers.forEach{
            val client = ApiConfig.getApiService().getUserDetail(it.login!!)
            client.enqueue(object : Callback<DetailUserResponse>{
                override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                    _isLoading.value = false
                    _isLoading.value = true
                    Log.d(TAG, "onFailure getallusertodetail : ${t.message}")
                    t.printStackTrace()
                }

                override fun onResponse(
                    call: Call<DetailUserResponse>,
                    response: Response<DetailUserResponse>
                ) {
                    _isLoading.value = false
                    _isError.value = false
                    val responseBody = response.body()
                    Log.d(TAG, "success : $responseBody")
                    if (response.isSuccessful && responseBody != null){
                        _isLoading.value = false
                        _isError.value = false
                        listUserDetail.add(responseBody)
                        if (listUserDetail.count() == listUsers.count()){
                            _allusers.postValue(listUserDetail)
                        }else{
                            Log.d(TAG, "onFailure : ${response.code()}")
                        }
                    }
                }
            })
        }
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

    companion object{
        private const val TAG = "MainViewModel"
        private const val NO_RESULT = "No Result"
    }
}
