package com.umc.approval.ui.adapter.community_upload_activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.data.dto.approval.get.ApprovalPaper
import com.umc.approval.databinding.ItemUploadDocumentBinding

class CommunityUploadDocumentItemRVAdapter (private var dataList: ArrayList<ApprovalPaper> = arrayListOf()): RecyclerView.Adapter<CommunityUploadDocumentItemRVAdapter.DataViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding =
            ItemUploadDocumentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    inner class DataViewHolder(private val binding: ItemUploadDocumentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ApprovalPaper) {

            if(adapterPosition == dataList.size-1) binding.approvalLine.isVisible = false

            when(data.state){
                1->{
                    binding.approvalStateApproval.isVisible = true
                    binding.approvalStateReject.isVisible = false
                }
                2->{
                    binding.approvalStateApproval.isVisible = false
                    binding.approvalStateReject.isVisible = true
                }
                else->{
                    binding.approvalLayout.isVisible = false
                }
            }

            binding.documentTitleTv.text = data.title
            binding.documentUpdateDateTv.text = data.datetime

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
        fun onItemClick(v: View, data: ApprovalPaper, pos: Int){}
    }

    private var listner: OnItemClickListner? = null

    fun setOnItemClickListener(listner: OnItemClickListner) {
        this.listner = listner
    }
}