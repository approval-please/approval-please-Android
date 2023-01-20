package com.umc.approval.ui.adapter.community_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.data.dto.community.CommunityItemDto
import com.umc.approval.databinding.CommunityItemBinding

/**
 * 결재 톡톡 및 보고서를 받아와 연결해주는 RV Adapter
 * */
class CommunityItemRVAdapter(private val items : List<CommunityItemDto>) : RecyclerView.Adapter<CommunityItemRVAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CommunityItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun binding(data : CommunityItemDto) {
            binding.reportTextView.text = data.body

            if (data.vote.toString() == "") {
                binding.reportVoteLayout.isVisible = false
            }

            if (data.image.toString() == "") {
                binding.reportImageLayout.isVisible = false
            }

            if (data.link.toString() == "") {
                binding.reportLinkLayout.isVisible = false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CommunityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}