package com.umc.approval.ui.adapter.community_post_activity

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.data.dto.communitydetail.get.VoteOption
import com.umc.approval.databinding.ItemCommunityPostVoteCompleteBinding

class CommunityVoteCompleteRVAdapter(private val dataList: List<VoteOption>,
                                     private val totalParticipant: Float,
                                     private val votePeopleEachOption: List<Int>,
                                     private val selected: List<Int>,
                                     private val reVote:Boolean): RecyclerView.Adapter<CommunityVoteCompleteRVAdapter.DataViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding =
            ItemCommunityPostVoteCompleteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(dataList[position], position)
    }

    override fun getItemCount(): Int = dataList.size

    inner class DataViewHolder(private val binding: ItemCommunityPostVoteCompleteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: VoteOption, position: Int) {

            //내용
            binding.voteItemContent.text = data.opt

            //투표자 수
            binding.voteItemParticipantCount.text = votePeopleEachOption.get(position).toString() + "명"

            //프로그래스바 처리
            (binding.votePercent.layoutParams as LinearLayout.LayoutParams).weight =
                (votePeopleEachOption.get(position)/totalParticipant)

            //투표한게 있다면
            if(data.voteOptionId in selected){
                binding.votePercent.background.setTint(Color.parseColor("#6C39FF"))
                binding.voteItemCheck.isChecked = true
            }

            //재투표
            if(!reVote){
                binding.voteItemCheck.isVisible = false
            }

            //클릭이벤트
            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                itemView.setOnClickListener {
                    listner?.onItemClick(itemView, data, pos)
                }
                binding.voteItemCheck.setOnClickListener {
                    listner?.voteClick(it, data.voteOptionId , pos, binding.voteItemCheck)
                }
            }
        }
    }

    // 아이템 클릭 리스너
    interface OnItemClickListner {
        fun onItemClick(v: View, data: VoteOption, pos: Int)

        fun voteClick(v: View, data: Int, pos: Int, voteItemCheck: AppCompatCheckBox)
    }

    private var listner: OnItemClickListner? = null

    fun setOnItemClickListener(listner: OnItemClickListner) {
        this.listner = listner
    }
}