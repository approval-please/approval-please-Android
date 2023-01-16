package com.umc.approval.ui.adapter.mypage_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.R
import com.umc.approval.databinding.MypageRecordItemBinding

class MyPageRecordAdapter(val itemList: ArrayList<MyPageRecordItem>) :
RecyclerView.Adapter<MyPageRecordAdapter.MyPageRecordViewHolder>() {
    inner class MyPageRecordViewHolder(val binding: MypageRecordItemBinding) : RecyclerView.ViewHolder(binding.root){
        val time_textview = binding.recordItemDate
        val content_textview = binding.recordItemContent
        val point_textview =  binding.recordItemPoint
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageRecordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mypage_record_item, parent, false)
        return MyPageRecordViewHolder(MypageRecordItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: MyPageRecordViewHolder, position: Int) {
        holder.time_textview.text = itemList[position].time
        holder.content_textview.text = itemList[position].content
        holder.point_textview.text = itemList[position].point
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }
}