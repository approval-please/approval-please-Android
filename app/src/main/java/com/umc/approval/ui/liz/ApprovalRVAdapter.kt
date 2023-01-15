package com.umc.approval.ui.liz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.R
import com.umc.approval.databinding.ItemHomeApprovalPaperBinding

class ApprovalRVAdapter(private val dataList: ArrayList<ApprovalPaper_HomeFragment> = arrayListOf()): RecyclerView.Adapter<ApprovalRVAdapter.DataViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = ItemHomeApprovalPaperBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    inner class DataViewHolder(private val binding: ItemHomeApprovalPaperBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ApprovalPaper_HomeFragment) {
            binding.tvApprovalPaperApproveCount.text = data.title
            binding.tvApprovalPaperContent.text = data.content
            binding.tvApprovalPaperApproveCount.text = data.approve_count.toString()
            binding.tvApprovalPaperRejectCount.text = data.reject_count.toString()
            binding.tvApprovalPaperViewsCount.text = data.views.toString()

            binding.tvApprovalPaperInfo.text = "${data.department}∙${data.date}"  // 수정 필요

            // 결재 승인 상태에 따라 이미지, 텍스트 변경
            if (data.approval_status) {
                binding.ivApprovalStateCircle.setImageResource(R.drawable.home_fragment_approval_status_circle_complete)
                binding.tvApprovalState.text = "승인완료"
            } else {
                binding.ivApprovalStateCircle.setImageResource(R.drawable.home_fragment_approval_status_circle_pending)
                binding.tvApprovalState.text = "승인대기중"
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
        fun onItemClick(v: View, data: ApprovalPaper_HomeFragment, pos: Int)
    }
    private var listner: OnItemClickListner ?= null

    fun setOnItemClickListener(listner: OnItemClickListner) {
        this.listner = listner
    }
}