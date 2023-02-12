package com.umc.approval.ui.adapter.search_fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.umc.approval.data.dto.approval.get.ApprovalPaper
import com.umc.approval.data.dto.search.get.SearchUserDto
import com.umc.approval.data.dto.search.get.SearchUserInfoDto
import com.umc.approval.databinding.ParticipantActivityRecyclerviewItemBinding
import com.umc.approval.util.Utils.level
import com.umc.approval.util.Utils.levelImage

class SearchUserRVAdapter(private val dataList: SearchUserDto?): RecyclerView.Adapter<SearchUserRVAdapter.DataViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = ParticipantActivityRecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        (dataList?.content?.get(position) ?: null)?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = dataList?.userCount?:0

    inner class DataViewHolder(private val binding: ParticipantActivityRecyclerviewItemBinding, context: Context): RecyclerView.ViewHolder(binding.root) {
        val context = context

        fun bind(data: SearchUserInfoDto) {
            binding.tvNickname.text = data.nickname
            binding.tvRank.text = level[data.level]
            if (data.profileImage != null) {
                binding.ivProfileImage.load(data.profileImage)
            } else {
                binding.ivProfileImage.load(levelImage[data.level])
            }
            binding.btnFollow.isVisible = false
            binding.btnUnfollow.isVisible = false
        }
    }

    // 아이템 클릭 리스너
    interface OnItemClickListner {
        fun onItemClick(v: View, data: SearchUserInfoDto, pos: Int)
    }
    private var listner: OnItemClickListner?= null

    fun setOnItemClickListener(listner: OnItemClickListner) {
        this.listner = listner
    }
}