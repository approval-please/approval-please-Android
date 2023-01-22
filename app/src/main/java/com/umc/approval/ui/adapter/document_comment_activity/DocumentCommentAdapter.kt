package com.umc.approval.ui.adapter.document_comment_activity

import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.R
import com.umc.approval.databinding.DocumentCommentRecyclerviewItemBinding

class DocumentCommentAdapter(val itemList : ArrayList<DocumentCommentItem>)
    : RecyclerView.Adapter<DocumentCommentAdapter.DocumentCommentViewHolder>(){
    inner class DocumentCommentViewHolder(val binding : DocumentCommentRecyclerviewItemBinding) : RecyclerView.ViewHolder(binding.root){
        val name_textview = binding.documentCommentItemName
        val content_textview = binding.documentCommentItemContent
        val date_textview = binding.documentCommentItemDate
        val likes_textview = binding.documentCommentItemLikes
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentCommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.document_comment_recyclerview_item, parent, false)
        return DocumentCommentViewHolder(DocumentCommentRecyclerviewItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: DocumentCommentViewHolder, position: Int) {
        holder.name_textview.text = itemList[position].name
        holder.content_textview.text = itemList[position].content
        holder.date_textview.text = itemList[position].date
        holder.likes_textview.text = itemList[position].likes.toString()
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }
}