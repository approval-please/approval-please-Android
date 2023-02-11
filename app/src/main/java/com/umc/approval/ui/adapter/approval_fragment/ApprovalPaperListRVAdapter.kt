package com.umc.approval.ui.adapter.approval_fragment

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.umc.approval.R
import com.umc.approval.data.dto.approval.get.ApprovalPaper
import com.umc.approval.data.dto.approval.get.ApprovalPaperDto
import com.umc.approval.databinding.ApprovalFragmentItemApprovalPaperBinding
import com.umc.approval.util.Utils.categoryMap


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

            Log.d("테스트입니다", data.toString())

            /**
             * 이미지가 없으면 이미지 제외하고 처리
             * 이미지가 있으면 로드
             * */
            if (data.thumbnailImage == null) {
                binding.itemImage.isVisible = false
                binding.tvImageCount.isVisible = false
                val layoutParams = binding.contentContainer.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.marginStart = 0
                binding.contentContainer.layoutParams = layoutParams
            } else {
                binding.itemImage.load(data.thumbnailImage)
                binding.itemImage.clipToOutline = true
            }

            /**
             * 제목, 내용, 승인, 거절, 뷰, 카테고리, 작성시간표시
             * */
            binding.tvTitle.text = data.title
            binding.tvContent.text = data.content
            binding.tvApproveCount.text = data.approvalCount.toString()
            binding.tvRejectCount.text = data.rejectCount.toString()
            binding.tvViews.text = data.view.toString()
            binding.tvCategory.text = categoryMap[data.category]
            binding.tvWriteTime.text = data.datetime
            binding.tvImageCount.text = data.imageCount.toString()

            /**
             * 승인 상태에 따라 처리
             * */
            when (data.state) {
                0 -> {
                    // 승인됨
                    binding.approvalPaperBackground.setImageResource(R.drawable.approval_fragment_approved_paper_background)
                    binding.tvApprovalState.text = "승인됨"
                    binding.ivApprovalStateCircle.setImageResource(R.drawable.home_fragment_approval_status_approved)
                }
                1 -> {
                    // 반려됨
                    binding.approvalPaperBackground.setImageResource(R.drawable.approval_fragment_rejected_paper_background)
                    binding.tvApprovalState.text = "반려됨"
                    binding.ivApprovalStateCircle.setImageResource(R.drawable.home_fragment_approval_status_rejected)
                }
                else -> {
                    // 승인 대기중
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

            /**
             * tag RecyclerView
             * */
            if (data.tag != null) {
                val tagRVAdapter = TagRVAdapter(data.tag)
                binding.rvTag.adapter = tagRVAdapter
                binding.rvTag.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
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