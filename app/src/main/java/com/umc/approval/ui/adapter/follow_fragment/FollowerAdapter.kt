package com.umc.approval.ui.adapter.follow_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.umc.approval.R
import com.umc.approval.data.dto.common.CommonUserDto
import com.umc.approval.data.dto.common.CommonUserListDto
import com.umc.approval.data.dto.mypage.FollowDto
import com.umc.approval.data.dto.search.post.KeywordDto
import com.umc.approval.databinding.FollowRecyclerviewItemBinding

class FollowerAdapter(val itemList : List<FollowDto>)
    : RecyclerView.Adapter<FollowerAdapter.FollowerViewHolder>(){

    inner class FollowerViewHolder(val binding : FollowRecyclerviewItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun binding(data : FollowDto) {
            //일단 이름만 설정
            binding.followItemName.setText(data.nickname)
            binding.followItemProfilepic.load(data.profileImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.follow_recyclerview_item, parent, false)
        return FollowerViewHolder(FollowRecyclerviewItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: FollowerViewHolder, position: Int) {
        holder.binding(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}