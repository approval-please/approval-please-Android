package com.umc.approval.ui.adapter.community_post_activity

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.data.dto.community.get.VoteItem
import com.umc.approval.databinding.ItemCommunityPostVoteCompleteBinding

class CommunityVoteCompleteRVAdapter(private val dataList: MutableList<VoteItem>, private val totalParticipant: Float, private val reVote:Boolean): RecyclerView.Adapter<CommunityVoteCompleteRVAdapter.DataViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding =
            ItemCommunityPostVoteCompleteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    inner class DataViewHolder(private val binding: ItemCommunityPostVoteCompleteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: VoteItem) {
            binding.voteItemContent.text = data.content
            binding.voteItemParticipantCount.text = data.participation.toString() + "명"
            (binding.votePercent.layoutParams as LinearLayout.LayoutParams).weight = (data.participation/totalParticipant)

            if(data.check){
                binding.votePercent.background.setTint(Color.parseColor("#6C39FF"))
                binding.voteItemCheck.isChecked = data.check
            }

            if(!reVote){
                binding.voteItemCheck.isVisible = false
            }

            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                itemView.setOnClickListener {
                    listner?.onItemClick(itemView, data, pos)
                }
                binding.voteItemCheck.setOnClickListener {
                    listner?.voteClick(it, data.id , pos)
                }
            }
        }
    }

    // 아이템 클릭 리스너
    interface OnItemClickListner {
        fun onItemClick(v: View, data: VoteItem, pos: Int)

        fun voteClick(v: View, data: Int, pos: Int)
    }

    private var listner: OnItemClickListner? = null

    fun setOnItemClickListener(listner: OnItemClickListner) {
        this.listner = listner
    }
}