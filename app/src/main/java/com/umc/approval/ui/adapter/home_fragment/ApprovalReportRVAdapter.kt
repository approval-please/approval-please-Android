package com.umc.approval.ui.adapter.home_fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.databinding.ItemHomeApprovalReportBinding
import com.umc.approval.util.ApprovalReport

class ApprovalReportRVAdapter(private val dataList: ArrayList<ApprovalReport> = arrayListOf()): RecyclerView.Adapter<ApprovalReportRVAdapter.DataViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = ItemHomeApprovalReportBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    inner class DataViewHolder(private val binding: ItemHomeApprovalReportBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ApprovalReport) {
            // 이미지 설정 부분 -> 수정 필요
            // binding.ivProfileImage.setImageResource()
            // binding.ivApprovalReportThumbnail.setImageResource()
            binding.tvNickname.text = data.user_nickname
            binding.tvRank.text = data.user_rank
            binding.tvPostViewsCount.text = data.views.toString()
            binding.tvPostContent.text = data.content
            binding.tvPostCommentCount.text = data.comment_count.toString()
            binding.tvPostLikeCount.text = data.like_count.toString()
            binding.tvPostWriteTime.text = data.date

            // binding.tvImageCount.text = "+$data.image.size"

            if (data.image != null) {
                binding.ivApprovalReportThumbnail.setImageResource(data.image as Int)
            } else {
                binding.ivApprovalReportThumbnail.visibility = View.GONE
                binding.tvImageCount.visibility = View.GONE
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
        fun onItemClick(v: View, data: ApprovalReport, pos: Int)
    }
    private var listner: OnItemClickListner?= null

    fun setOnItemClickListener(listner: OnItemClickListner) {
        this.listner = listner
    }
}