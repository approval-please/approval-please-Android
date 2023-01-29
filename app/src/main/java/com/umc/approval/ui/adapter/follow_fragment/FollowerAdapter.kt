package com.umc.approval.ui.adapter.follow_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.umc.approval.R
import com.umc.approval.databinding.FollowRecyclerviewItem2Binding
import com.umc.approval.databinding.FollowRecyclerviewItemBinding

class FollowerAdapter(val itemList : ArrayList<FollowerItem>)
    : RecyclerView.Adapter<ViewHolder>(){
    inner class FollowerViewHolder1(val binding : FollowRecyclerviewItemBinding) : RecyclerView.ViewHolder(binding.root){
        val name_textview = binding.followItemName
    }
    inner class FollowerViewHolder2(val binding : FollowRecyclerviewItem2Binding) : RecyclerView.ViewHolder(binding.root){
        val name_textview = binding.followItemName
    }

    override fun getItemViewType(position: Int): Int {
        return itemList[position].isFollowing
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view1 = LayoutInflater.from(parent.context).inflate(R.layout.follow_recyclerview_item, parent, false)
        val view2 = LayoutInflater.from(parent.context).inflate(R.layout.follow_recyclerview_item2, parent, false)
        return when(viewType){
            0 -> {
                FollowerViewHolder1(FollowRecyclerviewItemBinding.bind(view1))
            }
            else -> {
                FollowerViewHolder2(FollowRecyclerviewItem2Binding.bind(view2))
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(itemList[position].isFollowing){
            0 -> {
                (holder as FollowerViewHolder1).name_textview.text = itemList[position].name
            }
            1 -> {
                (holder as FollowerViewHolder2).name_textview.text = itemList[position].name
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }
}