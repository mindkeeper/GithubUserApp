package com.example.githubuserapp.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserapp.apiresponse.DetailUserResponse
import com.example.githubuserapp.databinding.ItemRowUserBinding

class ListUserAdapter(private val listUsers : List<DetailUserResponse>): RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {

    private var onItemClickCallback : OnItemClickCallback? = null

    class ListViewHolder(val binding: ItemRowUserBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.binding.tvRealName.text = listUsers[position].name
        holder.binding.tvUsername.text = listUsers[position].login
        Glide.with(holder.itemView.context)
            .load(listUsers[position].avatarUrl)
            .into(holder.binding.itemPhoto)
    }

    override fun getItemCount() = listUsers.size

    interface OnItemClickCallback{
        fun onItemClick(user : DetailUserResponse){

        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }
}