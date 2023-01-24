package com.umc.approval.ui.adapter.upload_activity

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.databinding.ItemUploadHashtagBinding
import com.umc.approval.databinding.UploadImageItemBinding

class UploadHashtagRVAdapter(private val items : List<String>) : RecyclerView.Adapter<UploadHashtagRVAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemUploadHashtagBinding) : RecyclerView.ViewHolder(binding.root) {
        fun binding(hashtag: String) {
            if (hashtag != "") {
                binding.uploadHashtagItem.text = hashtag
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemUploadHashtagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}