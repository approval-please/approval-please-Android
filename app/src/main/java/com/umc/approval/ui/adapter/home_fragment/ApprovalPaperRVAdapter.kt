package com.umc.approval.ui.adapter.home_fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.R
import com.umc.approval.data.dto.approval.get.ApprovalPaper
import com.umc.approval.data.dto.approval.get.ApprovalPaperDto
import com.umc.approval.databinding.ItemHomeApprovalPaperBinding

class ApprovalPaperRVAdapter(private val dataList: ApprovalPaperDto): RecyclerView.Adapter<ApprovalPaperRVAdapter.DataViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = ItemHomeApprovalPaperBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(dataList.approvalPaperDto[position])
    }

    override fun getItemCount(): Int = dataList.approvalPaperDto.size

    inner class DataViewHolder(private val binding: ItemHomeApprovalPaperBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ApprovalPaper) {
            binding.tvApprovalPaperTitle.text = data.title
            binding.tvApprovalPaperContent.text = data.content
            binding.tvApprovalPaperContent.text = data.content
            binding.tvApprovalPaperApproveCount.text = data.approveCount.toString()
            binding.tvApprovalPaperRejectCount.text = data.rejectCount.toString()
            binding.tvApprovalPaperViewsCount.text = data.view.toString()

            binding.tvApprovalPaperInfo.text = "디지털부서∙${data.datetime}"  // 수정 필요

            // 결재 승인 상태에 따라 이미지, 텍스트 변경
            when (data.state) {
                0 -> {
                    binding.ivApprovalStateCircle.setImageResource(R.drawable.home_fragment_approval_status_approved)
                    binding.tvApprovalState.text = "승인됨"
                }
                1 -> {
                    binding.ivApprovalStateCircle.setImageResource(R.drawable.home_fragment_approval_status_rejected)
                    binding.tvApprovalState.text = "반려됨"
                }
                else -> {
                    binding.ivApprovalStateCircle.setImageResource(R.drawable.home_fragment_approval_status_pending)
                    binding.tvApprovalState.text = "승인대기중"
                }
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
        fun onItemClick(v: View, data: ApprovalPaper, pos: Int)
    }
    private var listner: OnItemClickListner?= null

    fun setOnItemClickListener(listner: OnItemClickListner) {
        this.listner = listner
    }
}