package com.umc.approval.ui.adapter.like_activity

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.R
import com.umc.approval.databinding.ActivityLikeRecyclerviewItemBinding
import com.umc.approval.util.Like

class LikeRVAdapter(private val dataList: ArrayList<Like> = arrayListOf()): RecyclerView.Adapter<LikeRVAdapter.DataViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = ActivityLikeRecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    inner class DataViewHolder(private val binding: ActivityLikeRecyclerviewItemBinding,
                               val context: Context
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Like) {
            // binding.ivProfileImage.setImageResource()
            binding.tvNickname.text = data.user_nickname
            binding.tvRank.text = data.user_rank

            if (data.follow_status) {
                binding.btnFollow.setBackgroundColor(ContextCompat.getColor(context, R.color.unselected_tab_text))
                binding.btnFollow.text = "팔로잉"
            }

            binding.btnFollow.setOnClickListener {
                Log.d("로그", "팔로우 버튼 클릭, $adapterPosition")
            }
        }
    }
}