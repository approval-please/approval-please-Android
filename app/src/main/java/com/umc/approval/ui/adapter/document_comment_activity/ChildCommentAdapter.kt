package com.umc.approval.ui.adapter.document_comment_activity

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.umc.approval.R
import com.umc.approval.data.dto.comment.get.CommentDto
import com.umc.approval.databinding.DocumentCommentRecyclerviewItem2Binding
import com.umc.approval.util.Utils

class ChildCommentAdapter(val itemList: List<CommentDto>, val context: Context) : RecyclerView.Adapter<ChildCommentAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: DocumentCommentRecyclerviewItem2Binding) : RecyclerView.ViewHolder(binding.root) {

        fun binding(data: CommentDto) {
            //프로필 이미지 있을시
            if (data.profileImage != null) {
                binding.documentCommentItemProfilepic.load(data.profileImage)
                binding.documentCommentItemProfilepic.clipToOutline = true

            }

            //닉네임
            binding.documentCommentItemName.text = data.nickname

            //직급
            binding.documentCommentItemRank.text = Utils.level[data.level]

            //내용
            binding.documentCommentItemContent.text = data.content

            //날짜
            binding.documentCommentItemDate.text = data.datetime

            //작성자일시
            if(data.isWriter == true){
                binding.documentCommentItemRank.text = "글쓴이"
                binding.documentCommentItemRank.setBackgroundResource(R.drawable.writer_background)
                binding.documentCommentItemRank.setTextColor(Color.parseColor("#6C39FF"))
                binding.documentCommentItemName.setTextColor(Color.parseColor("#6C39FF"))
            }

            //라이크
            if(data.isLike == true){
                binding.documentCommentItemLikebtn.setImageResource(R.drawable.document_comment_recyclerview_icon_like_selected)
            }

            binding.documentCommentItemLikes.text = data.likeCount.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = DocumentCommentRecyclerviewItem2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    /**RV item click event*/
    interface ItemClick{ //인터페이스
        fun setting_comment(v: View, data: CommentDto, pos: Int, context: Context)
        fun like(v: View, data: CommentDto, pos: Int)
    }

    var itemClick: ItemClick? = null

    override fun getItemCount(): Int {
        return itemList.size
    }

    /**대ㄴ댓글 다이얼로그 로직*/
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(itemList[position])

        if (itemClick != null){

            holder.binding.setting.setOnClickListener(View.OnClickListener {
                itemClick?.setting_comment(it, itemList[position], position, context)
            })

            //라이크
            holder.binding.documentCommentItemLikebtn.setOnClickListener(View.OnClickListener {
                itemClick?.like(it, itemList[position], position)
            })
        }
    }
}