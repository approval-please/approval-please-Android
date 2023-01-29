package com.umc.approval.ui.adapter.approval_fragment

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.R
import com.umc.approval.databinding.ApprovalFragmentCategoryItemBinding
import com.umc.approval.util.InterestingCategory

class CategoryRVAdapter(private val dataList: ArrayList<InterestingCategory> = arrayListOf()): RecyclerView.Adapter<CategoryRVAdapter.DataViewHolder>() {
    var preSelectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = ApprovalFragmentCategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    inner class DataViewHolder(private val binding: ApprovalFragmentCategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: InterestingCategory) {

            binding.tvCategory.text = data.category

            val pos = adapterPosition

            if (dataList[pos].selected) {
                binding.tvCategory.setTextColor(Color.parseColor("#6C39FF"))
                binding.layout.setBackgroundResource(R.drawable.approval_fragment_category_selected_item_background)
            } else {
                binding.tvCategory.setTextColor(Color.parseColor("#141414"))
                binding.layout.setBackgroundResource(R.drawable.approval_fragment_category_unselected_item_background)
            }

            itemView.setOnClickListener {
                for (i in 0 until dataList.size) {
                    dataList[i].selected = i == pos
                }

                notifyItemChanged(preSelectedPosition)
                notifyItemChanged(pos)

                listener?.onItemClick(itemView, data, pos)
                preSelectedPosition = pos

            }
        }
    }

    // 아이템 클릭 리스너
    interface OnItemClickListener {
        fun onItemClick(v: View, data: InterestingCategory, pos: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}