package com.umc.approval.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.databinding.SearchIngItemBinding
import com.umc.approval.ui.dto.KeywordDto

/**
 * 최근 검색 데이터를 받아와 연결해주는 RV Adapter
 * */
class SearchIngRVAdapter(private val items : List<KeywordDto>) : RecyclerView.Adapter<SearchIngRVAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: SearchIngItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun binding(data : KeywordDto) {
            binding.searchRecord.setText(data.keyword)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = SearchIngItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    /**RV item click event*/
    interface ItemClick{ //인터페이스
        fun related_keyword_search(view: View, position: KeywordDto)
    }

    var itemClick: ItemClick? = null

    override fun onBindViewHolder(holder: SearchIngRVAdapter.ViewHolder, position: Int) {
        holder.binding(items[position])
        if (itemClick != null){

            //연관 검색어 탐색
            holder.binding.relatedText.setOnClickListener(View.OnClickListener {
                itemClick?.related_keyword_search(it, items[position])
                Log.d("keyword_click_event", "related_keyword_search")
            })
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}