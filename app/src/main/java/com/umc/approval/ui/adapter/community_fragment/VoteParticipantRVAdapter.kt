package com.umc.approval.ui.adapter.community_fragment

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.umc.approval.R
import com.umc.approval.data.dto.community.get.ParticipantDto
import com.umc.approval.data.dto.community.get.VoteParticipantDto
import com.umc.approval.databinding.ParticipantActivityRecyclerviewItemBinding

class VoteParticipantRVAdapter(private val dataList: VoteParticipantDto?=null): RecyclerView.Adapter<VoteParticipantRVAdapter.DataViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = ParticipantActivityRecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        (dataList?.content?.get(position) ?: null)?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = dataList?.content?.size ?: 0

    inner class DataViewHolder(private val binding: ParticipantActivityRecyclerviewItemBinding, context: Context): RecyclerView.ViewHolder(binding.root) {
        val context = context

        fun bind(data: ParticipantDto) {
            // binding.ivProfileImage.setImageResource()
            if (data.profileImage != null) {
                binding.ivProfileImage.load(data.profileImage)
                binding.ivProfileImage.clipToOutline = true

            }
            binding.tvNickname.text = data.nickname
            binding.tvRank.text = data.level.toString()

            if (data.followOrNot) {
                binding.btnFollow.setBackgroundColor(ContextCompat.getColor(context, R.color.unselected_tab_text))
                binding.btnFollow.text = "팔로잉"
            }

            binding.btnFollow.setOnClickListener {
                Log.d("로그", "팔로우 버튼 클릭, $adapterPosition")
            }
        }
    }
}