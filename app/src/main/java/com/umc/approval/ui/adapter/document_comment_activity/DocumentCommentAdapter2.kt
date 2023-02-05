package com.umc.approval.ui.adapter.document_comment_activity

import android.graphics.Color
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.umc.approval.R
import com.umc.approval.databinding.DocumentCommentRecyclerviewItem2Binding

class DocumentCommentAdapter2(val itemList : ArrayList<DocumentCommentItem>)
    : RecyclerView.Adapter<DocumentCommentAdapter2.DocumentComment2ViewHolder>(){
    inner class DocumentComment2ViewHolder(val binding : DocumentCommentRecyclerviewItem2Binding) : RecyclerView.ViewHolder(binding.root){
        val profileImg = binding.documentCommentItemProfilepic
        val name_textview = binding.documentCommentItemName
        val rank = binding.documentCommentItemRank
        val content_textview = binding.documentCommentItemContent
        val date_textview = binding.documentCommentItemDate
        val likes_textview = binding.documentCommentItemLikes
        val like_icon = binding.documentCommentItemLikebtn
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentComment2ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.document_comment_recyclerview_item2, parent, false)
        return DocumentComment2ViewHolder(DocumentCommentRecyclerviewItem2Binding.bind(view))
    }

    override fun onBindViewHolder(holder: DocumentComment2ViewHolder, position: Int) {
        holder.profileImg.load(itemList[position].profileImg)
        holder.name_textview.text = itemList[position].name
        holder.content_textview.text = itemList[position].content
        holder.date_textview.text = itemList[position].date
        holder.likes_textview.text = itemList[position].likes.toString()
        if(itemList[position].isLike == true){
            holder.like_icon.setImageResource(R.drawable.document_comment_recyclerview_icon_like_selected)
        }
        if(itemList[position].isWriter == true){
            holder.rank.text = "글쓴이"
            holder.rank.setBackgroundResource(R.drawable.writer_background)
            holder.rank.setTextColor(Color.parseColor("#6C39FF"))
            holder.name_textview.setTextColor(Color.parseColor("#6C39FF"))
        }
        else{
            when(itemList[position].rank){
                0->{
                    holder.rank.text = "사원"
                }
                1->{
                    holder.rank.text = "주임"
                }
                2->{
                    holder.rank.text = "대리"
                }
                3->{
                    holder.rank.text = "과장"
                }
                4->{
                    holder.rank.text = "차장"
                }
                5->{
                    holder.rank.text = "부장"
                }
                else->{
                    Log.d("직급", "해당 사항 없음")
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }
}