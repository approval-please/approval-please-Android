package com.umc.approval.ui.adapter.home_fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginStart
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.R
import com.umc.approval.databinding.ItemHomeApprovalPaperBinding
import com.umc.approval.util.ApprovalPaper

class ApprovalPaperRVAdapter(private val dataList: ArrayList<ApprovalPaper> = arrayListOf()): RecyclerView.Adapter<ApprovalPaperRVAdapter.DataViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = ItemHomeApprovalPaperBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    inner class DataViewHolder(private val binding: ItemHomeApprovalPaperBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ApprovalPaper) {
            binding.tvApprovalPaperTitle.text = data.title
            binding.tvApprovalPaperContent.text = data.content
            binding.tvApprovalPaperContent.text = data.content
            binding.tvApprovalPaperApproveCount.text = data.approve_count.toString()
            binding.tvApprovalPaperRejectCount.text = data.reject_count.toString()
            binding.tvApprovalPaperViewsCount.text = data.views.toString()

            binding.tvCategory.text = data.department // 수정 필요
            binding.tvWriteTime.text = data.date

            if (data.image != null) {
                binding.ivThumbnail.setImageResource(data.image as Int)
            } else {
                binding.ivThumbnail.visibility = View.GONE
                val layoutParams = binding.contentsContainer.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.marginStart = 0
                binding.contentsContainer.layoutParams = layoutParams
            }

            // 결재 승인 상태에 따라 이미지, 텍스트 변경
            when (data.approval_status) {
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