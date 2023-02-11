package com.umc.approval.ui.adapter.search_fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.databinding.FragmentSearchNoResultBinding

class NoSearchResultRVAdapter (private val query: List<String?>): RecyclerView.Adapter<NoSearchResultRVAdapter.DataViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = FragmentSearchNoResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        (query?.get(position) ?: null)?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = query.size ?: 0

    inner class DataViewHolder(private val binding: FragmentSearchNoResultBinding, context: Context): RecyclerView.ViewHolder(binding.root) {
        val context = context

        fun bind(data: String) {
            binding.searchTv.text = data.toString()
        }
    }

}