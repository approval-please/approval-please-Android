package com.umc.approval.ui.adapter.document_comment_activity

import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.umc.approval.R
import com.umc.approval.databinding.DocumentCommentRecyclerviewItem3Binding
import com.umc.approval.databinding.DocumentCommentRecyclerviewItemBinding

class DocumentCommentAdapter(val itemList : ArrayList<DocumentCommentItem2>)
    : RecyclerView.Adapter<ViewHolder>(){
    inner class Type1ViewHolder(val binding : DocumentCommentRecyclerviewItemBinding) : RecyclerView.ViewHolder(binding.root){
        val name_textview = binding.documentCommentItemName
        val content_textview = binding.documentCommentItemContent
        val date_textview = binding.documentCommentItemDate
        val likes_textview = binding.documentCommentItemLikes
    }
    inner class Type2ViewHolder(val binding : DocumentCommentRecyclerviewItem3Binding) : ViewHolder(binding.root){
        val recyclerview = binding.documentCommentRecyclerview
    }
    override fun getItemViewType(position: Int): Int {
        return itemList[position].type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view1 = LayoutInflater.from(parent.context).inflate(R.layout.document_comment_recyclerview_item, parent, false)
        val view2 = LayoutInflater.from(parent.context).inflate(R.layout.document_comment_recyclerview_item3, parent, false)
        return when (viewType){
            DocumentCommentItem2.TYPE_1 -> {
                Type1ViewHolder(DocumentCommentRecyclerviewItemBinding.bind(view1))
            }
            else -> {
                Type2ViewHolder(DocumentCommentRecyclerviewItem3Binding.bind(view2))
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(itemList[position].type){
            DocumentCommentItem2.TYPE_1 -> {
                (holder as Type1ViewHolder).name_textview.text = itemList[position].list.get(0).name
                holder.content_textview.text = itemList[position].list.get(0).content
                holder.date_textview.text = itemList[position].list.get(0).date
                holder.likes_textview.text = itemList[position].list.get(0).likes.toString()
            }
            DocumentCommentItem2.TYPE_2 -> {
                (holder as Type2ViewHolder).recyclerview.layoutManager = LinearLayoutManager(holder.recyclerview.context)
                val adapter = DocumentCommentAdapter2(itemList[position].list)
                adapter.notifyDataSetChanged()
                holder.recyclerview.adapter = adapter
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }
}