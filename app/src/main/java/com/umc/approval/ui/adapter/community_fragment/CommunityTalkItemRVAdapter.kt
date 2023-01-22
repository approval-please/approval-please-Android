package com.umc.approval.ui.adapter.community_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.umc.approval.data.dto.community.CommunityItemDto
import com.umc.approval.databinding.CommunityTalkItemBinding

/**
 * 결재 톡톡 및 보고서를 받아와 연결해주는 RV Adapter
 * */
class CommunityTalkItemRVAdapter(private val items : List<CommunityItemDto>) : RecyclerView.Adapter<CommunityTalkItemRVAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CommunityTalkItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun binding(data : CommunityItemDto) {
            if (data.opengraph.image.toString() != "") {
                binding.talkOpenGraphImage.load(data.opengraph.image)
                binding.talkOpenGraphText.setText(data.opengraph.title)
                binding.talkOpenGraphUrl.setText(data.opengraph.url)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CommunityTalkItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}