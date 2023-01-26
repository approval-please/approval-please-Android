package com.umc.approval.ui.adapter.approval_fragment

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.umc.approval.R
import com.umc.approval.data.dto.approval.get.ApprovalPaper
import com.umc.approval.data.dto.approval.get.ApprovalPaperDto
import com.umc.approval.databinding.ApprovalFragmentItemApprovalPaperBinding

class ApprovalPaperListRVAdapter(private val dataList: ApprovalPaperDto): RecyclerView.Adapter<ApprovalPaperListRVAdapter.DataViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = ApprovalFragmentItemApprovalPaperBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(dataList.approvalPaperDto[position])
    }

    override fun getItemCount(): Int = dataList.approvalPaperDto.size

    inner class DataViewHolder(private val binding: ApprovalFragmentItemApprovalPaperBinding, context: Context): RecyclerView.ViewHolder(binding.root) {
        val context = context
        fun bind(data: ApprovalPaper) {

            if (data.images != null) {
                binding.itemImage.isVisible = false
            } else {
//                binding.itemImage.load(data.image.get(0))
            }

//            binding.tvTitle.text = data.title
//            binding.tvContent.text = data.content
            binding.tvApproveCount.text = data.approveCount.toString()
            binding.tvRejectCount.text = data.rejectCount.toString()
            binding.tvViews.text = data.view.toString()
            binding.tvApprovalPaperInfo.text = "${data.category}∙${data.datetime}"

            // 결재 승인 상태에 배경 설정
            when (data.state) {
                0 -> {
                    // 승인됨
                    binding.itemContainer.setBackgroundResource(R.drawable.approval_fragment_approved_paper_background)
                    binding.tvApprovalState.text = "승인됨"
                    binding.ivApprovalStateCircle.setImageResource(R.drawable.home_fragment_approval_status_approved)
                }
                1 -> {
                    // 반려됨
                    binding.itemContainer.setBackgroundResource(R.drawable.approval_fragment_rejected_paper_background)
                    binding.tvApprovalState.text = "반려됨"
                    binding.ivApprovalStateCircle.setImageResource(R.drawable.home_fragment_approval_status_rejected)
                }
                else -> {
                    // 승인 대기중
                    binding.itemContainer.setBackgroundColor(Color.WHITE)
                    binding.tvApprovalState.text = "승인대기중"
                    binding.ivApprovalStateCircle.setImageResource(R.drawable.home_fragment_approval_status_pending)
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