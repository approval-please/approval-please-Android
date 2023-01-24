package com.umc.approval.ui.adapter.community_fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.umc.approval.data.dto.community.get.CommunityTok
import com.umc.approval.data.dto.community.get.CommunityTokDto
import com.umc.approval.databinding.CommunityTalkItemBinding

/**
 * 결재 톡톡 및 보고서를 받아와 연결해주는 RV Adapter
 * */
class CommunityTalkItemRVAdapter(private val items : CommunityTokDto) : RecyclerView.Adapter<CommunityTalkItemRVAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CommunityTalkItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun binding(data : CommunityTok) {

            if (data.linkUrl != null) {
                binding.talkOpenGraphImage.load(data.linkUrl.get(0).image)
                binding.talkOpenGraphText.setText(data.linkUrl.get(0).title)
                binding.talkOpenGraphUrl.setText(data.linkUrl.get(0).url)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CommunityTalkItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(items.communityTok[position])
        if (itemClick != null){
            holder.binding.communityApprovalTalkItem.setOnClickListener(View.OnClickListener {
                itemClick?.move_to_tok_activity()
            })
        }
    }

    override fun getItemCount(): Int {
        return items.communityTok.size
    }

    /**RV item click event*/
    interface ItemClick{ //인터페이스
        fun move_to_tok_activity()
    }

    var itemClick: ItemClick? = null
}