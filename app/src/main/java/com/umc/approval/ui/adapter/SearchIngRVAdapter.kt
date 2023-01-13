package com.umc.approval.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.databinding.SearchIngItemBinding
import com.umc.approval.ui.dto.KeywordDto

/**
 * 최근 검색 데이터를 받아와 연결해주는 RV Adapter
 * */
class SearchIngRVAdapter(private val items : List<KeywordDto>) : RecyclerView.Adapter<SearchIngRVAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: SearchIngItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun binding(data : KeywordDto) {
            binding.searchRecord.setText(data.keyword)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = SearchIngItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}