package com.umc.approval.ui.adapter.follow_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.R
import com.umc.approval.databinding.FollowRecyclerviewItemBinding

class FollowerAdapter(val itemList : ArrayList<FollowerItem>)
    : RecyclerView.Adapter<FollowerAdapter.FollowerViewHolder>(){
    inner class FollowerViewHolder(val binding : FollowRecyclerviewItemBinding) : RecyclerView.ViewHolder(binding.root){
        val name_textview = binding.followItemName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.follow_recyclerview_item, parent, false)
        return FollowerViewHolder(FollowRecyclerviewItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: FollowerViewHolder, position: Int) {
        holder.name_textview.text = itemList[position].name
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }
}