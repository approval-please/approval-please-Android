package com.umc.approval.ui.adapter.interesting_activity

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.databinding.InterestingDepartmentActivityUninterestingBinding

class UninterestingDepartmentRVAdapter(private val dataList: ArrayList<String> = arrayListOf()): RecyclerView.Adapter<UninterestingDepartmentRVAdapter.DataViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = InterestingDepartmentActivityUninterestingBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        binding.btnStar.setOnClickListener {
            Log.d("로그", "별 버튼 클릭")
        }

        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    inner class DataViewHolder(private val binding: InterestingDepartmentActivityUninterestingBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: String) {
            binding.tvDepartmentName.text = data
        }
    }
}