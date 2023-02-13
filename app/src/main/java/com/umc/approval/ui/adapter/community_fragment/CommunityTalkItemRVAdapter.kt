package com.umc.approval.ui.adapter.community_fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.umc.approval.App
import com.umc.approval.data.dto.community.get.CommunityTok
import com.umc.approval.data.dto.community.get.CommunityTokDto
import com.umc.approval.databinding.CommunityTalkItemBinding
import com.umc.approval.ui.adapter.community_post_activity.CommunityImageRVAdapter
import com.umc.approval.ui.adapter.upload_activity.UploadHashtagRVAdapter
import com.umc.approval.util.Utils
import com.umc.approval.util.Utils.categoryMap
import com.umc.approval.util.Utils.level

/**
 * 결재 톡톡 및 보고서를 받아와 연결해주는 RV Adapter
 * */
class CommunityTalkItemRVAdapter(private val items : CommunityTokDto) : RecyclerView.Adapter<CommunityTalkItemRVAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CommunityTalkItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun binding(data: CommunityTok) {

            /*결재 보고서 부분*/
            binding.reportCategoryItemText.text = data.content // 내용
            binding.communityPostUserName.text = data.nickname
            binding.reportViewText.text = data.view.toString()
            binding.communityPostCategory.text = categoryMap[data.category]
            binding.communityPostTime.text = data.datetime
            binding.rank.text = level[data.userLevel]
            binding.tvLikeCount.text = data.likeCount.toString()
            binding.tvCommentCount.text = data.commentCount.toString()

            //profile image
            binding.communityPostUserProfile.clipToOutline = true
            if (data.profileImage != null) {
                binding.communityPostUserProfile.load(data.profileImage)
            } else {
                binding.communityPostUserProfile.load(Utils.levelImage[data.userLevel])
            }

            if (data.voteDto != null) {
                if (data.voteDto.isEnd == false) {
                    binding.communityPostVoteState.text = "투표진행중"
                } else {
                    binding.communityPostVoteState.text = "투표 종료"
                }
                if(data.voteDto.isSingle == false){
                    binding.communityPostVoteOption.text = " · 복수선택"
                }else{
                    binding.communityPostVoteOption.text = " · 단일선택"
                }
                binding.communityPostVoteTitle.text = data.voteDto.title
                binding.communityPostVoteParticipant.text = data.voteDto.voteUserCount.toString() + "명 참여"
            } else {
                binding.communityPostVote.isVisible = false
            }

            if(data.images == null){
                binding.uploadImageLayout.isVisible = false
            }else{
                var imageRVAdapter = CommunityImageRVAdapter(data.images as ArrayList<String>)
                binding.imageRv.adapter = imageRVAdapter
                binding.imageRv.layoutManager =
                    LinearLayoutManager(App.context(), LinearLayoutManager.HORIZONTAL, false)
            }

            if(data.tag == null){
                binding.uploadHashtagItem.isVisible = false
            }else{
                val dataRVAdapter = UploadHashtagRVAdapter(data.tag)
                binding.uploadHashtagItem.adapter = dataRVAdapter
                binding.uploadHashtagItem.layoutManager = LinearLayoutManager(App.context(), RecyclerView.HORIZONTAL, false)
            }

            if (data.link != null) {
                binding.reportOpenGraphImage.load(data.link.image)
                binding.reportOpenGraphText.setText(data.link.title)
                binding.reportOpenGraphUrl.setText(data.link.url)
            }else{
                binding.reportLinkLayout.isVisible = false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CommunityTalkItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding(items.communityTok[position])
        if (itemClick != null){
            holder.binding.reportCategoryItemText.setOnClickListener(View.OnClickListener {
                itemClick?.move_to_tok_activity(it, items.communityTok[position], position)
            })
            holder.binding.uploadImageLayout.setOnClickListener{
                itemClick?.move_to_tok_activity(it, items.communityTok[position], position)
            }
            holder.binding.reportLinkLayout.setOnClickListener{
                itemClick?.move_to_tok_activity(it, items.communityTok[position], position)
            }
            holder.binding.communityPostVote.setOnClickListener{
                itemClick?.move_to_tok_activity(it, items.communityTok[position], position)
            }
            holder.binding.likeAndComment.setOnClickListener{
                itemClick?.move_to_tok_activity(it, items.communityTok[position], position)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.communityTok.size
    }

    /**RV item click event*/
    interface ItemClick{ //인터페이스
        fun move_to_tok_activity(v: View, data: CommunityTok, pos: Int)
    }

    var itemClick: ItemClick? = null
}