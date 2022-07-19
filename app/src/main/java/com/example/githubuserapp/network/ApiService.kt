package com.example.githubuserapp.network


import com.example.githubuserapp.apiresponse.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @Headers(
        "Accept: application/vnd.github.v3+json",
        "Authorization: token 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"
    )

    @GET("user")
    fun getListUsers():Call<List<ListUsersResponseItem>>

    @GET("search/{users}")
    fun getSearchUser(
        @Query("query") username : String
    ): Call<SearchUserResponse>

    @GET("users/{username}")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<ItemsItem>>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String
    ): Call<List<ItemsItem>>
}