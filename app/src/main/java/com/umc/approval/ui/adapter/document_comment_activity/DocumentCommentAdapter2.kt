package com.umc.approval.ui.adapter.document_comment_activity

import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.R
import com.umc.approval.databinding.DocumentCommentRecyclerviewItem2Binding

class DocumentCommentAdapter2(val itemList : ArrayList<DocumentCommentItem>)
    : RecyclerView.Adapter<DocumentCommentAdapter2.DocumentComment2ViewHolder>(){
    inner class DocumentComment2ViewHolder(val binding : DocumentCommentRecyclerviewItem2Binding) : RecyclerView.ViewHolder(binding.root){
        val name_textview = binding.documentCommentItemName
        val content_textview = binding.documentCommentItemContent
        val date_textview = binding.documentCommentItemDate
        val likes_textview = binding.documentCommentItemLikes
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentComment2ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.document_comment_recyclerview_item2, parent, false)
        return DocumentComment2ViewHolder(DocumentCommentRecyclerviewItem2Binding.bind(view))
    }

    override fun onBindViewHolder(holder: DocumentComment2ViewHolder, position: Int) {
        holder.name_textview.text = itemList[position].name
        holder.content_textview.text = itemList[position].content
        holder.date_textview.text = itemList[position].date
        holder.likes_textview.text = itemList[position].likes.toString()
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }
}