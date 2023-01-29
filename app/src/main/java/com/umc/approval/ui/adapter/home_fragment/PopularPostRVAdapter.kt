package com.umc.approval.ui.adapter.home_fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.umc.approval.data.dto.community.get.CommunityTok
import com.umc.approval.data.dto.community.get.CommunityTokDto
import com.umc.approval.databinding.ItemHomePopularPostBinding

class PopularPostRVAdapter(private val dataList: CommunityTokDto): RecyclerView.Adapter<PopularPostRVAdapter.DataViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = ItemHomePopularPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(dataList.communityTok[position])
    }

    override fun getItemCount(): Int = dataList.communityTok.size

    inner class DataViewHolder(private val binding: ItemHomePopularPostBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CommunityTok) {
            // 이미지 설정 부분 - 설정 필요
            // binding.ivProfileImage.setImageResource()
            binding.tvNickname.text = data.nickname
            binding.tvPostViewsCount.text = data.view.toString()
            binding.tvPostContent.text = data.content

            binding.tvPostCommentCount.text = data.commentCount.toString()
            binding.tvPostLikeCount.text = data.likeCount.toString()
            binding.tvPostWriteTime.text = data.datetime
            // binding.tvImageCount.text = "+$data.image.size"

            if (data.images != null) {
                binding.ivThumbnail.load(data.images.get(0))
            } else {
                binding.ivThumbnail.visibility = View.GONE
                binding.tvImageCount.visibility = View.GONE

                binding.tvPostContent.maxLines = 7
            }

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
        fun onItemClick(v: View, data: CommunityTok, pos: Int)
    }
    private var listner: OnItemClickListner?= null

    fun setOnItemClickListener(listner: OnItemClickListner) {
        this.listner = listner
    }
}