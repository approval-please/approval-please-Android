package com.umc.approval.ui.adapter.document_activity

import android.net.Uri
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.umc.approval.R
import com.umc.approval.databinding.DocumentImageItemBinding

class DocumentImageAdapter(val itemList : ArrayList<Uri>) : RecyclerView.Adapter<DocumentImageAdapter.DocumentImageViewHolder>() {
    inner class DocumentImageViewHolder(var binding : DocumentImageItemBinding) : ViewHolder(binding.root){
        val image1 = binding.image1
        val image2 = binding.image2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.document_image_item, parent, false)
        return DocumentImageViewHolder(DocumentImageItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: DocumentImageViewHolder, position: Int) {
        if (itemList.count() >= (position + 1) * 2){
            // 이미지 uri 설정
        }
        else{
            // 이미지 uri 설정
            // 홀수 개 -> 2 번째 이미지 안 보이도록 설정
            holder.binding.image2.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int {
        var count = (itemList.count() + 1) / 2
        return count
    }
}