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

    override fun getItemCount(): Int {
        return if (dataList.communityReport.size < 5) {
            dataList.communityReport.size
        } else {
            5
        }
    }

    inner class DataViewHolder(private val binding: ItemHomeApprovalReportBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CommunityReport) {
            binding.tvNickname.text = data.nickname
            binding.tvPostViewsCount.text = data.view.toString()
            binding.tvPostContent.text = data.content
             binding.tvImageCount.text = data.images.size.toString()

            if (data.images != null) {
                if (data.images.isNotEmpty()) {
                    binding.ivApprovalReportThumbnail.load(data.images.get(0))
                    binding.ivApprovalReportThumbnail.clipToOutline = true
                }
            } else {
                binding.ivApprovalReportThumbnail.visibility = View.GONE
                binding.tvImageCount.visibility = View.GONE
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
        fun onItemClick(v: View, data: CommunityReport, pos: Int)
    }
    private var listener: OnItemClickListener?= null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}