package com.umc.approval.ui.adapter.home_fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.umc.approval.R
import com.umc.approval.data.dto.approval.get.ApprovalPaper
import com.umc.approval.data.dto.approval.get.ApprovalPaperDto
import com.umc.approval.databinding.ItemHomeApprovalPaperBinding
import com.umc.approval.util.Utils.categoryMap

class ApprovalPaperRVAdapter(private val dataList: ApprovalPaperDto): RecyclerView.Adapter<ApprovalPaperRVAdapter.DataViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = ItemHomeApprovalPaperBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(dataList.approvalPaperDto[position])
    }

    override fun getItemCount(): Int {
        return dataList.approvalPaperDto.size
    }

    inner class DataViewHolder(private val binding: ItemHomeApprovalPaperBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ApprovalPaper) {

            binding.tvApprovalPaperTitle.text = data.title
            binding.tvApprovalPaperContent.text = data.content
            binding.tvApprovalPaperApproveCount.text = data.approvalCount.toString()
            binding.tvApprovalPaperRejectCount.text = data.rejectCount.toString()
            binding.tvApprovalPaperViewsCount.text = data.view.toString()
            binding.tvImageCount.text = data.imageCount.toString()

            binding.tvCategory.text = categoryMap[data.category]
            binding.tvWriteTime.text = data.datetime

            if (data.thumbnailImage != null) {
                binding.ivThumbnail.load(data.thumbnailImage)
                binding.ivThumbnail.clipToOutline = true
            } else {
                binding.ivThumbnail.visibility = View.GONE
                binding.tvImageCount.visibility = View.GONE
                val layoutParams = binding.contentsContainer.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.marginStart = 0
                binding.contentsContainer.layoutParams = layoutParams
            }

            // ?????? ?????? ????????? ?????? ?????????, ????????? ??????
            when (data.state) {
                0 -> {
                    binding.ivApprovalStateCircle.setImageResource(R.drawable.home_fragment_approval_status_approved)
                    binding.tvApprovalState.text = "?????????"
                }
                1 -> {
                    binding.ivApprovalStateCircle.setImageResource(R.drawable.home_fragment_approval_status_rejected)
                    binding.tvApprovalState.text = "?????????"
                }
                else -> {
                    binding.ivApprovalStateCircle.setImageResource(R.drawable.home_fragment_approval_status_pending)
                    binding.tvApprovalState.text = "???????????????"
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

    // ????????? ?????? ?????????
    interface OnItemClickListner {
        fun onItemClick(v: View, data: ApprovalPaper, pos: Int)
    }
    private var listner: OnItemClickListner?= null

    fun setOnItemClickListener(listner: OnItemClickListner) {
        this.listner = listner
    }
}