package com.umc.approval.ui.adapter.community_post_activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.databinding.ItemCommunityPostCommentBinding
import com.umc.approval.util.CommentItem

class CommunityCommentRVAdapter (private val dataList: ArrayList<CommentItem> = arrayListOf()): RecyclerView.Adapter<CommunityCommentRVAdapter.DataViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding =
            ItemCommunityPostCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    inner class DataViewHolder(private val binding: ItemCommunityPostCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CommentItem) {
            binding.commentItemNickname.text = data.user_nickname
            binding.documentCommentItemContent.text = data.content
            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                itemView.setOnClickListener {
                    listner?.onItemClick(itemView, data, pos)
                }
            }
        }
    }

    // 아이템 클릭 리스너
    interface OnItemClickListner {
        fun onItemClick(v: View, data: CommentItem, pos: Int)
    }

    private var listner: OnItemClickListner? = null

    fun setOnItemClickListener(listner: OnItemClickListner) {
        this.listner = listner
    }
}