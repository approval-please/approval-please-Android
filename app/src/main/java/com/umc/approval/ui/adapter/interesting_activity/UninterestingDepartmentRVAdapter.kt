package com.umc.approval.ui.adapter.interesting_activity

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.data.dto.search.post.KeywordDto
import com.umc.approval.databinding.InterestingDepartmentActivityUninterestingBinding
import com.umc.approval.util.InterestingCategory
import com.umc.approval.util.Utils.categoryMap

class UninterestingDepartmentRVAdapter(private val dataList: List<Int>): RecyclerView.Adapter<UninterestingDepartmentRVAdapter.DataViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = InterestingDepartmentActivityUninterestingBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        binding.btnStar.setOnClickListener {
            Log.d("로그", "별 버튼 클릭")
        }

        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(dataList[position])
        if (itemClick != null){
            //최근 검색어 제거
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

    inner class DataViewHolder(val binding: InterestingDepartmentActivityUninterestingBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Int) {
            binding.tvDepartmentName.text = categoryMap[data]
        }
    }


}