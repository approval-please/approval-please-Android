package com.umc.approval.ui.adapter.document_activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.umc.approval.R
import com.umc.approval.databinding.DocumentImageItemBinding

class DocumentImageAdapter(val itemList : List<String>) : RecyclerView.Adapter<DocumentImageAdapter.DocumentImageViewHolder>() {
    inner class DocumentImageViewHolder(var binding : DocumentImageItemBinding) : ViewHolder(binding.root){
        fun bind(s: String) {
            binding.image1.load(s)
            binding.image1.clipToOutline = true

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.document_image_item, parent, false)
        return DocumentImageViewHolder(DocumentImageItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: DocumentImageViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}