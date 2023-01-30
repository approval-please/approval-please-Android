package com.umc.approval.ui.adapter.home_fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.umc.approval.data.dto.community.get.CommunityReport
import com.umc.approval.data.dto.community.get.CommunityReportDto
import com.umc.approval.databinding.ItemHomeApprovalReportBinding

class ApprovalReportRVAdapter(private val dataList: CommunityReportDto): RecyclerView.Adapter<ApprovalReportRVAdapter.DataViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = ItemHomeApprovalReportBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(dataList.communityReport[position])
    }

    override fun getItemCount(): Int = dataList.communityReport.size

    inner class DataViewHolder(private val binding: ItemHomeApprovalReportBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CommunityReport) {
            // 이미지 설정 부분 -> 수정 필요
            // binding.ivProfileImage.setImageResource()
            // binding.ivApprovalReportThumbnail.setImageResource()
            binding.tvNickname.text = data.nickname
            binding.tvPostViewsCount.text = data.view.toString()
            binding.tvPostContent.text = data.content

            // binding.tvImageCount.text = "+$data.image.size"

            if (data.images != null) {
                binding.ivApprovalReportThumbnail.load(data.images.get(0))
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
        fun onItemClick(v: View, data: CommunityReport, pos: Int)
    }
    private var listner: OnItemClickListner?= null

    fun setOnItemClickListener(listner: OnItemClickListner) {
        this.listner = listner
    }
}