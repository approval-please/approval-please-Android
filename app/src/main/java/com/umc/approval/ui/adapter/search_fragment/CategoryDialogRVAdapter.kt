package com.umc.approval.ui.adapter.search_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.databinding.FragmentSearchBottomSheetDialogCategoryItemBinding
import com.umc.approval.util.CategorySelectDialogItem

class CategoryDialogRVAdapter(private val dataList: ArrayList<CategorySelectDialogItem> = arrayListOf()): RecyclerView.Adapter<CategoryDialogRVAdapter.DataViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = FragmentSearchBottomSheetDialogCategoryItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DataViewHolder(binding)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    inner class DataViewHolder(private val binding: FragmentSearchBottomSheetDialogCategoryItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CategorySelectDialogItem) {
            binding.tvCategory.text = data.category
            binding.cbCategory.isChecked = dataList[adapterPosition].isChecked

            binding.cbCategory.setOnCheckedChangeListener { _, isChecked ->
                dataList[adapterPosition].isChecked = isChecked
            }
        }
    }
}