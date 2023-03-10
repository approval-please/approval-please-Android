package com.umc.approval.ui.adapter.participant_activity

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.R
import com.umc.approval.databinding.ParticipantActivityRecyclerviewItemBinding
import com.umc.approval.util.Participant

class ParticipantRVAdapter(private val dataList: ArrayList<Participant> = arrayListOf()): RecyclerView.Adapter<ParticipantRVAdapter.DataViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = ParticipantActivityRecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    inner class DataViewHolder(private val binding: ParticipantActivityRecyclerviewItemBinding, context: Context): RecyclerView.ViewHolder(binding.root) {
        val context = context

        fun bind(data: Participant) {
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