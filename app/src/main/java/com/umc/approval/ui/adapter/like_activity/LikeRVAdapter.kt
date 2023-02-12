package com.umc.approval.ui.adapter.like_activity

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.umc.approval.R
import com.umc.approval.data.dto.common.CommonUserDto
import com.umc.approval.data.dto.common.CommonUserListDto
import com.umc.approval.data.dto.mypage.FollowDto
import com.umc.approval.databinding.ActivityLikeRecyclerviewItemBinding
import com.umc.approval.util.Like
import com.umc.approval.util.Utils.level
import com.umc.approval.util.Utils.levelImage

class LikeRVAdapter(private val dataList: CommonUserListDto): RecyclerView.Adapter<LikeRVAdapter.DataViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = ActivityLikeRecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(dataList.dataEntity[position])

        if (itemClick != null){
            holder.binding.ivProfileImage.setOnClickListener {
                itemClick?.move_to_profile(it, dataList.dataEntity[position], position)
            }
            //팔로우중 X
            holder.binding.btnUnfollow.setOnClickListener {
                itemClick?.follow(it, dataList.dataEntity[position], position)
            }
            //팔로우중
            holder.binding.btnFollow.setOnClickListener {
                itemClick?.unfollow(it, dataList.dataEntity[position], position)
            }
        }
    }

    override fun getItemCount(): Int = dataList.likeCount

    inner class DataViewHolder(
        val binding: ActivityLikeRecyclerviewItemBinding,
        val context: Context
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(data: CommonUserDto) {
            if (binding.ivProfileImage != null) {
                binding.ivProfileImage.load(data.profileImage)
            } else {
                binding.ivProfileImage.load(levelImage[data.level])
            }
            binding.tvNickname.text = data.nickname
            binding.tvRank.text = level[data.level]

            if (data.isMy == true) {
                binding.btnUnfollow.isVisible = false
                binding.btnFollow.isVisible = false
            } else {
                if (data.isFollow) {
                    binding.btnUnfollow.isVisible = false
                    binding.btnFollow.isVisible = true
                } else {
                    binding.btnUnfollow.isVisible = true
                    binding.btnFollow.isVisible = false
                }
            }
        }
    }

    /* recyclerview item 클릭 이벤트 */
    interface ItemClick{
        fun move_to_profile(v: View, data: CommonUserDto, pos: Int)

        fun follow(v: View, data: CommonUserDto, pos: Int)

        fun unfollow(v: View, data: CommonUserDto, pos: Int)
    }
    var itemClick : ItemClick? = null
}