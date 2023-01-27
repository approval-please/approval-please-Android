package com.umc.approval.ui.adapter.approval_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.databinding.ApprovalFragmentTagItemBinding

class TagRVAdapter(private val dataList: List<String>): RecyclerView.Adapter<TagRVAdapter.DataViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = ApprovalFragmentTagItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    inner class DataViewHolder(private val binding: ApprovalFragmentTagItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: String) {

            binding.tvCategory.text = data
        }
    }
}