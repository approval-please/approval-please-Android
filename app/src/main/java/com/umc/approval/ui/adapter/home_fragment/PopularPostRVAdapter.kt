package com.umc.approval.ui.adapter.home_fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.umc.approval.data.dto.community.get.CommunityTok
import com.umc.approval.data.dto.community.get.CommunityTokDto
import com.umc.approval.databinding.ItemHomePopularPostBinding
import com.umc.approval.util.Utils

class PopularPostRVAdapter(private val dataList: CommunityTokDto): RecyclerView.Adapter<PopularPostRVAdapter.DataViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = ItemHomePopularPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(dataList.communityTok[position])
    }

    override fun getItemCount(): Int {
        return if (dataList.communityTok.size < 5) {
            dataList.communityTok.size
        } else {
            5
        }
    }

    inner class DataViewHolder(private val binding: ItemHomePopularPostBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CommunityTok) {
            //profile image
            binding.ivProfileImage.clipToOutline = true
            if (data.profileImage != null) {
                binding.ivProfileImage.load(data.profileImage)
            } else {
                binding.ivProfileImage.load(Utils.levelImage[data.userLevel])
            }
            binding.tvNickname.text = data.nickname
            binding.tvPostViewsCount.text = data.view.toString()
            binding.tvPostContent.text = data.content

            binding.tvPostCommentCount.text = data.commentCount.toString()
            binding.tvPostLikeCount.text = data.likeCount.toString()
            binding.tvPostWriteTime.text = data.datetime
             binding.tvImageCount.text = data.images.size.toString()

            if (data.images != null) {

                if (data.images.isNotEmpty()) {
                    binding.ivThumbnail.load(data.images.get(0))
                    binding.ivThumbnail.clipToOutline = true
                }
            } else {
                binding.ivThumbnail.visibility = View.GONE
                binding.tvImageCount.visibility = View.GONE

                binding.tvPostContent.maxLines = 7
            }

            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                itemView.setOnClickListener {
                    listener?.onItemClick(itemView, data, pos)
                }
            }
        }
    }

    // 아이템 클릭 리스너
    interface OnItemClickListener {
        fun onItemClick(v: View, data: CommunityTok, pos: Int)
    }
    private var listener: OnItemClickListener?= null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}