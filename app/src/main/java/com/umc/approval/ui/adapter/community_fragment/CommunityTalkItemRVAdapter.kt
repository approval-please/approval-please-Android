package com.umc.approval.ui.adapter.community_fragment

import android.util.Log
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

/**
 * 결재 톡톡 및 보고서를 받아와 연결해주는 RV Adapter
 * */
class CommunityTalkItemRVAdapter(private val items : CommunityTokDto) : RecyclerView.Adapter<CommunityTalkItemRVAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CommunityTalkItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun binding(data: CommunityTok) {

            Log.d("test", data.toString())

            /*결재 보고서 부분*/
            binding.reportCategoryItemText.text = data.content // 내용
            binding.communityPostUserName.text = data.nickname
            binding.reportViewText.text = data.view.toString()
            binding.tvLikeCount.text = data.likeCount.toString()
            binding.tvCommentCount.text = data.commentCount.toString()

            binding.communityPostUserProfile.load(data.profileImage)

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
                itemClick?.move_to_tok_activity()
            })
        }
    }

    override fun getItemCount(): Int {
        return items.communityTok.size
    }

    /**RV item click event*/
    interface ItemClick{ //인터페이스
        fun move_to_tok_activity()
    }

    var itemClick: ItemClick? = null
}