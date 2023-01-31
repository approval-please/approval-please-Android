package com.umc.approval.ui.adapter.interesting_activity

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.databinding.InterestingDepartmentActivityInterestingBinding
import com.umc.approval.util.Utils.categoryMap
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.List

class InterestingDepartmentRVAdapter(private val dataList: List<Int>): RecyclerView.Adapter<InterestingDepartmentRVAdapter.DataViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = InterestingDepartmentActivityInterestingBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        binding.btnStar.setOnClickListener {
            Log.d("로그", "별 버튼 클릭")
        }

        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(dataList[position])
        if (itemClick != null){
            holder.binding.btnStar.setOnClickListener(View.OnClickListener {
                itemClick?.category_select(it, dataList[position])
            })
        }
    }

    override fun getItemCount(): Int = dataList.size

    /**RV item click event*/
    interface ItemClick{ //인터페이스
        fun category_select(view: View, int: Int)
    }

    var itemClick: ItemClick? = null

    // 데이터 순서를 바꾸는 함수
    fun swapData(fromPos: Int, toPos: Int) {
        Collections.swap(dataList, fromPos, toPos)
        notifyItemMoved(fromPos, toPos)
    }

    inner class DataViewHolder(val binding: InterestingDepartmentActivityInterestingBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Int) {
            binding.tvDepartmentName.text = categoryMap[data]
        }
    }
}