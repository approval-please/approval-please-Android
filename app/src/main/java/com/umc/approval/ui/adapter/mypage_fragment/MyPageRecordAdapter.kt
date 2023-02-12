package com.umc.approval.ui.adapter.mypage_fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.R
import com.umc.approval.data.dto.mypage.Record
import com.umc.approval.data.dto.mypage.RecordDto
import com.umc.approval.data.dto.search.post.KeywordDto
import com.umc.approval.databinding.MypageRecordItemBinding

class MyPageRecordAdapter(val items: List<Record>) :
RecyclerView.Adapter<MyPageRecordAdapter.MyPageRecordViewHolder>() {

    inner class MyPageRecordViewHolder(val binding: MypageRecordItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun binding(data : Record) {
            binding.recordItemContent.text = data.content
            binding.recordItemDate.text = data.datetime
            binding.recordItemPoint.text = data.point.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageRecordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mypage_record_item, parent, false)
        return MyPageRecordViewHolder(MypageRecordItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: MyPageRecordViewHolder, position: Int) {
        holder.binding(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}