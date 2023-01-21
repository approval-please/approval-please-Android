package com.umc.approval.ui.adapter.follow_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.R
import com.umc.approval.databinding.FollowRecyclerviewItemBinding

class FollowingAdapter(val itemList : ArrayList<FollowingItem>)
    : RecyclerView.Adapter<FollowingAdapter.FollowingViewHolder>(){
    inner class FollowingViewHolder(val binding : FollowRecyclerviewItemBinding) : RecyclerView.ViewHolder(binding.root){
        val name_textview = binding.followItemName
        val follow_button = binding.followBtn
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.follow_recyclerview_item, parent, false)
        return FollowingViewHolder(FollowRecyclerviewItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: FollowingViewHolder, position: Int) {
        holder.name_textview.text = itemList[position].name
        if (itemList[position].isFollowing == false){
            holder.follow_button.text = "팔로우"
        }
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }
}