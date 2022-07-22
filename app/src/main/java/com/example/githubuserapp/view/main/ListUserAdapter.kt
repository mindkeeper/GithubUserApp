package com.example.githubuserapp.view.main

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserapp.apiresponse.DetailUserResponse
import com.example.githubuserapp.apiresponse.ListUsersResponseItem
import com.example.githubuserapp.databinding.ItemRowUserBinding

class ListUserAdapter(private val listUsers : List<ListUsersResponseItem>): RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {

    private var onItemClickCallback : OnItemClickCallback? = null

    class ListViewHolder(val binding: ItemRowUserBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.binding.tvUsername.text = listUsers[position].login
        Glide.with(holder.itemView.context)
            .load(listUsers[position].avatarUrl)
            .into(holder.binding.itemPhoto)
        val username : String? = listUsers[holder.adapterPosition].login
        Log.d("ListUserAdapter",  username!!)

        holder.itemView.setOnClickListener{
            onItemClickCallback?.onItemClick(username)
            Log.d("ListUserAdapter",  username)
        }
    }

    override fun getItemCount() = listUsers.size

    interface OnItemClickCallback{
        fun onItemClick(user : String?){

        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }
}