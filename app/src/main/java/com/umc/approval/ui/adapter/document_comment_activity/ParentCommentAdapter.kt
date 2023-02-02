package com.umc.approval.ui.adapter.document_comment_activity

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.umc.approval.App
import com.umc.approval.R
import com.umc.approval.data.dto.comment.get.CommentDto
import com.umc.approval.data.dto.comment.get.CommentListDto
import com.umc.approval.databinding.DocumentCommentRecyclerviewItemBinding
import com.umc.approval.util.Utils.level

class ParentCommentAdapter(val itemList : CommentListDto) : RecyclerView.Adapter<ParentCommentAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: DocumentCommentRecyclerviewItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun binding(data: CommentDto) {

            //자식 댓글 처리
            if(data.childComment == null || data.childComment.isEmpty()){
                binding.child.isVisible = false
            }else{
                val dataRVAdapter = ChildCommentAdapter(data.childComment)
                binding.child.adapter = dataRVAdapter
                binding.child.layoutManager = LinearLayoutManager(App.context(), RecyclerView.VERTICAL, false)
            }

            //프로필 이미지 있을시
            if (data.profileImage != null) {
                binding.documentCommentItemProfilepic.load(data.profileImage)
            }

            //닉네임
            binding.documentCommentItemName.text = data.nickname

            //직급
            binding.documentCommentItemRank.text = level[data.level]

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
        val view = DocumentCommentRecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(itemList.content[position])

        if (itemClick != null){
            holder.binding.checkBtn.setOnClickListener(View.OnClickListener {
                itemClick?.make_chid_comment(it, itemList.content[position], position)
            })
        }
    }

    /**RV item click event*/
    interface ItemClick{ //인터페이스
        fun make_chid_comment(v: View, data: CommentDto, pos: Int)
    }

    var itemClick: ItemClick? = null

    override fun getItemCount(): Int {
        return itemList.content.size
    }
}