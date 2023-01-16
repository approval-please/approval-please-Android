package com.umc.approval.ui.adapter.interesting_activity

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.databinding.InterestingDepartmentActivityInterestingBinding
import java.util.*
import kotlin.collections.ArrayList

class InterestingDepartmentRVAdapter(private val dataList: ArrayList<String> = arrayListOf()): RecyclerView.Adapter<InterestingDepartmentRVAdapter.DataViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = InterestingDepartmentActivityInterestingBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        binding.btnStar.setOnClickListener {
            Log.d("로그", "별 버튼 클릭")
        }

        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    // 데이터 순서를 바꾸는 함수
    fun swapData(fromPos: Int, toPos: Int) {
        Collections.swap(dataList, fromPos, toPos)
        notifyItemMoved(fromPos, toPos)
    }

    inner class DataViewHolder(private val binding: InterestingDepartmentActivityInterestingBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: String) {
            binding.tvDepartmentName.text = data
        }
    }
}