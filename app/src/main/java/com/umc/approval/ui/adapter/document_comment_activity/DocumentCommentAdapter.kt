package com.umc.approval.ui.adapter.document_comment_activity

import android.graphics.Color
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.umc.approval.R
import com.umc.approval.databinding.DocumentCommentRecyclerviewItem3Binding
import com.umc.approval.databinding.DocumentCommentRecyclerviewItemBinding
import com.umc.approval.ui.activity.DocumentActivity
import com.umc.approval.ui.viewmodel.comment.CommentViewModel

class DocumentCommentAdapter(val itemList : ArrayList<DocumentCommentItem2>, val activity : DocumentActivity)
    : RecyclerView.Adapter<ViewHolder>(){

    inner class Type1ViewHolder(val binding : DocumentCommentRecyclerviewItemBinding) : RecyclerView.ViewHolder(binding.root){
        val profileImg = binding.documentCommentItemProfilepic
        val name_textview = binding.documentCommentItemName
        val rank = binding.documentCommentItemRank
        val content_textview = binding.documentCommentItemContent
        val date_textview = binding.documentCommentItemDate
        val likes_textview = binding.documentCommentItemLikes
        val like_icon = binding.documentCommentItemLikebtn
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
                val item = itemList[position].list.get(0)
                (holder as Type1ViewHolder).profileImg.load(item.profileImg)
                holder.name_textview.text = item.name
                holder.content_textview.text = item.content
                holder.date_textview.text = item.date
                holder.likes_textview.text = item.likes.toString()
                if(item.isLike == true){
                    holder.like_icon.setImageResource(R.drawable.document_comment_recyclerview_icon_like_selected)
                }
                if(item.isWriter == true){
                    holder.rank.text = "글쓴이"
                    holder.rank.setBackgroundResource(R.drawable.writer_background)
                    holder.rank.setTextColor(Color.parseColor("#6C39FF"))
                    holder.name_textview.setTextColor(Color.parseColor("#6C39FF"))
                }
                else{
                    when(item.rank){
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
                holder.like_icon.setOnClickListener {
                    if(activity.getDocumentViewModel().accessToken.value == true){
                        if(item.isLike == false){
                            holder.like_icon.setImageResource(R.drawable.document_comment_recyclerview_icon_like_selected)
                            val likes = item.likes
                            if(likes != null){
                                // 댓글 좋아요 수 변경(+1) 로직 이후 추가
                                holder.likes_textview.text = item.likes.toString()
                            }
                        }
                        else{
                            holder.like_icon.setImageResource(R.drawable.document_comment_recyclerview_icon_like)
                            val likes = item.likes
                            if(likes != null){
                                // 댓글 좋아요 수 변경(-1) 로직 이후 추가
                                holder.likes_textview.text = item.likes.toString()
                            }
                        }
                    }
                    else{
                        activity.sendNeedLoginToast()
                    }
                }
            }
            DocumentCommentItem2.TYPE_2 -> {
                (holder as Type2ViewHolder).recyclerview.layoutManager = LinearLayoutManager(holder.recyclerview.context)
                val adapter = DocumentCommentAdapter2(itemList[position].list, activity)
                adapter.notifyDataSetChanged()
                holder.recyclerview.adapter = adapter
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }
}