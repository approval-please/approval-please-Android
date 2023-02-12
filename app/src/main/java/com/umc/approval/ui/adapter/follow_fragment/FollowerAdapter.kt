package com.umc.approval.ui.adapter.follow_fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.umc.approval.data.dto.mypage.FollowDto
import com.umc.approval.databinding.FollowRecyclerviewItemBinding
import com.umc.approval.util.Utils.level
import com.umc.approval.util.Utils.levelImage

class FollowerAdapter(val itemList : List<FollowDto>, val id : Boolean): RecyclerView.Adapter<FollowerAdapter.FollowerViewHolder>(){

    inner class FollowerViewHolder(val binding : FollowRecyclerviewItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun binding(data: FollowDto) {
            //일단 이름만 설정
            binding.followItemName.setText(data.nickname)
            if (data.profileImage != null) {
                binding.followItemProfilepic.load(data.profileImage)
            } else {
                binding.followItemProfilepic.load(levelImage[data.level])
            }
            binding.followItemRank.text = level[data.level]
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowerAdapter.FollowerViewHolder {
        val binding = FollowRecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FollowerViewHolder(binding)
    }

    /**RV item click event*/
    interface ItemClick{ //인터페이스
        fun follow_or_not(v: View, data: FollowDto, pos: Int)
        fun other(v: View, data: FollowDto, pos: Int)
    }

    var itemClick: ItemClick? = null

    override fun onBindViewHolder(holder: FollowerViewHolder, position: Int) {
        holder.binding(itemList[position])

        if (itemClick != null){
            holder.binding.followItemProfilepic.setOnClickListener{
                itemClick?.other(it, itemList[position], position)
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}