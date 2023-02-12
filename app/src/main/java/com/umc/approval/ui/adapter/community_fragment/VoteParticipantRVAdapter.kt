package com.umc.approval.ui.adapter.community_fragment

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
import com.umc.approval.data.dto.community.get.ParticipantDto
import com.umc.approval.data.dto.community.get.VoteParticipantDto
import com.umc.approval.databinding.ParticipantActivityRecyclerviewItemBinding
import com.umc.approval.util.Utils.level

class VoteParticipantRVAdapter(private val dataList: VoteParticipantDto): RecyclerView.Adapter<VoteParticipantRVAdapter.DataViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = ParticipantActivityRecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(dataList.content!![position])

        if (itemClick != null){
            holder.binding.ivProfileImage.setOnClickListener {
                itemClick?.move_to_profile(it, dataList.content!![position], position)
            }
            //팔로우중 X
            holder.binding.btnUnfollow.setOnClickListener {
                itemClick?.follow(it, dataList.content!![position], position)
            }
            //팔로우중
            holder.binding.btnFollow.setOnClickListener {
                itemClick?.unfollow(it, dataList.content!![position], position)
            }
        }
    }

    override fun getItemCount(): Int = dataList?.content?.size ?: 0

    inner class DataViewHolder(val binding: ParticipantActivityRecyclerviewItemBinding, context: Context): RecyclerView.ViewHolder(binding.root) {
        val context = context

        fun bind(data: ParticipantDto) {
            if (data.profileImage != null) {
                binding.ivProfileImage.load(data.profileImage)
                binding.ivProfileImage.clipToOutline = true
            }
            binding.tvNickname.text = data.nickname
            binding.tvRank.text = level[data.level]

            if (data.isMy == true) {
                binding.btnUnfollow.isVisible = false
                binding.btnFollow.isVisible = false
            } else {
                if (data.followOrNot) {
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
        fun move_to_profile(v: View, data: ParticipantDto, pos: Int)
        fun follow(v: View, data: ParticipantDto, pos: Int)
        fun unfollow(v: View, data: ParticipantDto, pos: Int)
    }
    var itemClick : ItemClick? = null
}