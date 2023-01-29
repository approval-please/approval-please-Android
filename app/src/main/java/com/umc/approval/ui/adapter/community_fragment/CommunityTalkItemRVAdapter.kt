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
import com.umc.approval.databinding.CommunityTalkItemBinding
import com.umc.approval.ui.adapter.community_post_activity.CommunityImageRVAdapter
import com.umc.approval.ui.adapter.upload_activity.UploadHashtagRVAdapter

/**
 * 결재 톡톡 및 보고서를 받아와 연결해주는 RV Adapter
 * */
class CommunityTalkItemRVAdapter(private val items : List<CommunityTok>) : RecyclerView.Adapter<CommunityTalkItemRVAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CommunityTalkItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun binding(data: CommunityTok) {

            /*결재 보고서 부분*/
            binding.reportCategoryItemText.text = data.body // 내용
            binding.communityPostUserName.text = data.nickname
            binding.reportViewText.text = data.view.toString()
            binding.tvLikeCount.text = data.like.toString()
            binding.tvCommentCount.text = data.reply.toString()

            if(data.voteInfo != null){
                binding.communityPostVoteTitle.text = data.voteInfo!!.voteTitle
                binding.communityPostVoteParticipant.text = data.voteInfo!!.voteParticipants.toString()
                binding.communityPostVoteOption.text = data.voteInfo!!.voteOption
                binding.communityPostVoteState.text = data.voteInfo!!.voteState
            }else{
                binding.communityPostVote.isVisible = false
            }

            if(data.image.size ==0){
                binding.uploadImageLayout.isVisible = false
            }else{
                var imageRVAdapter = CommunityImageRVAdapter(data.image as ArrayList<String>)
                binding.imageRv.adapter = imageRVAdapter
                binding.imageRv.layoutManager =
                    LinearLayoutManager(App.context(), LinearLayoutManager.HORIZONTAL, false)
            }

            if(data.tags.size == 0){
                binding.uploadHashtagItem.isVisible = false
            }else{
                val dataRVAdapter = UploadHashtagRVAdapter(data.tags)
                binding.uploadHashtagItem.adapter = dataRVAdapter
                binding.uploadHashtagItem.layoutManager = LinearLayoutManager(App.context(), RecyclerView.HORIZONTAL, false)
            }

            if (data.opengraph.size > 0) {
                binding.reportOpenGraphImage.load(data.opengraph[0].image)
                binding.reportOpenGraphText.setText(data.opengraph[0].title)
                binding.reportOpenGraphUrl.setText(data.opengraph[0].url)
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
        holder.binding(items[position])
        if (itemClick != null){
            holder.binding.reportCategoryItemText.setOnClickListener(View.OnClickListener {
                itemClick?.move_to_tok_activity()
            })
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    /**RV item click event*/
    interface ItemClick{ //인터페이스
        fun move_to_tok_activity()
    }

    var itemClick: ItemClick? = null
}