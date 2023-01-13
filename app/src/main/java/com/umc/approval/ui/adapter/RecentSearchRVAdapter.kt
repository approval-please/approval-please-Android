package com.umc.approval.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.databinding.RecentSearchItemBinding
import com.umc.approval.ui.dto.KeywordDto

/**
 * 최근 검색 데이터를 받아와 연결해주는 RV Adapter
 * */
class RecentSearchRVAdapter(private val items : List<KeywordDto>) : RecyclerView.Adapter<RecentSearchRVAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RecentSearchItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun binding(data : KeywordDto) {
            binding.recentSearchText.setText(data.keyword)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RecentSearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    /**RV item click event*/
    interface ItemClick{ //인터페이스
        fun keyword_remove(view: View, position: KeywordDto)
        fun search(view: View, position: KeywordDto)
    }

    var itemClick: ItemClick? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(items[position])
        if (itemClick != null){

            //최근 검색어 제거
            holder?.binding!!.textRemove.setOnClickListener(View.OnClickListener {
                itemClick?.keyword_remove(it, items[position])
                Log.d("keyword_click_event", "keyword_delete")
            })

            //최근 검색어 탐색
            holder?.binding!!.recentSearchText.setOnClickListener(View.OnClickListener {
                itemClick?.search(it, items[position])
                Log.d("keyword_click_event", "keyword_search")
            })
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}