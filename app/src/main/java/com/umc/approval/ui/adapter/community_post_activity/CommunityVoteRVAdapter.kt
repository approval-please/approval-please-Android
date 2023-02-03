package com.umc.approval.ui.adapter.community_post_activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.data.dto.community.get.VoteItem
import com.umc.approval.databinding.ItemCommunityPostVoteBinding

class CommunityVoteRVAdapter(private val dataList: MutableList<VoteItem>): RecyclerView.Adapter<CommunityVoteRVAdapter.DataViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding =
            ItemCommunityPostVoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    inner class DataViewHolder(private val binding: ItemCommunityPostVoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: VoteItem) {
            binding.voteItemCheck.isChecked = data.check
            binding.voteItemContent.text = data.content
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