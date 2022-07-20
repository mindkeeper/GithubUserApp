package com.example.githubuserapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserapp.apiresponse.DetailUserResponse
import com.example.githubuserapp.apiresponse.ItemsItem
import com.example.githubuserapp.event.Event
import com.example.githubuserapp.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.StringBuilder

class DetailViewModel : ViewModel() {

    private val _listFollower = MutableLiveData<List<DetailUserResponse>>()
    val listFollower: LiveData<List<DetailUserResponse>>get() = _listFollower

    private val _listFollowing = MutableLiveData<List<DetailUserResponse>>()
    val listFollowing: LiveData<List<DetailUserResponse>>get() = _listFollowing

    private var _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> get() = _isError

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    val isSnackbarShown: Event<Boolean> = Event(false)

    var errorMessage: String = ""
        private set

    fun getUserFollower(username : String){
        _isError.value = false

        fun setFollower(follower : ArrayList<DetailUserResponse>){
            _listFollower.postValue(follower)
        }

        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<ItemsItem>>{
            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                onError(t.message)
                t.printStackTrace()
            }

            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody!= null){
                    getUserDetails(responseBody, ::setFollower)
                }else{
                    onError(response.code().toString())
                }
            }
        })

    }

    fun getUserFollowing(username : String){
        _isError.value = false

        fun setFollowing(following : ArrayList<DetailUserResponse>){
            _listFollower.postValue(following)
        }

        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<ItemsItem>>{
            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                onError(t.message)
                t.printStackTrace()
            }

            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody!= null){
                    getUserDetails(responseBody, ::setFollowing)
                }else{
                    onError(response.code().toString())
                }
            }
        })

    }

    private fun getUserDetails(listItem: List<ItemsItem>, setValue: (newValue: ArrayList<DetailUserResponse>)-> Unit){
        _isLoading.value = true
        _isError.value = false

        val listUserDetails = ArrayList<DetailUserResponse>()
        if (listItem.isEmpty()){
            setValue(listUserDetails)
            _isLoading.value = false
        }

        listItem.forEach{
            val client = ApiConfig.getApiService().getUserDetail(it.login!!)
            client.enqueue(object : Callback<DetailUserResponse>{
                override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                    _isLoading.value = false
                    onError(t.message)
                    t.printStackTrace()
                }

                override fun onResponse(
                    call: Call<DetailUserResponse>,
                    response: Response<DetailUserResponse>
                ) {
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody != null){
                        listUserDetails.add(responseBody)

                        if (listUserDetails.count() >= listItem.count()){
                            setValue(listUserDetails)
                            _isLoading.value = false
                        }
                    }else{
                        setValue(listUserDetails)
                        onError(response.code().toString())
                    }
                }
            })
        }
    }

    private fun onError(e : String?){
        var message = e
        if (message.isNullOrBlank()||message.isNullOrEmpty()){
            message = ApiConfig.error
        }else message

        errorMessage = StringBuilder("ERROR: ")
            .append("$message, data cannot be displayed").toString()

        _isLoading.value = false
        _isError.value = true
    }
}