package com.umc.approval.ui.adapter.notification_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.umc.approval.R
import com.umc.approval.databinding.NotificationRecyclerviewItem2Binding
import com.umc.approval.databinding.NotificationRecyclerviewItemBinding

class NotificationAdapter(val itemList: ArrayList<NotificationItem>)
    : RecyclerView.Adapter<ViewHolder>() {
    inner class Type1ViewHolder(val binding : NotificationRecyclerviewItem2Binding) : ViewHolder(binding.root){
        val title_textview = binding.notificationItemTitle
        val content_textview = binding.notificationItemContent
        val date_textview = binding.notificationItemDate
    }
    inner class Type2ViewHolder(val binding : NotificationRecyclerviewItemBinding) : ViewHolder(binding.root){
        val title1_textview = binding.notificationItemTitle1
        val title2_textview = binding.notificationItemTitle2
        val content_textview = binding.notificationItemContent
        val date_textview = binding.notificationItemDate
        val icon = binding.notificationItemIcon
        val state_textview = binding.notificationItemIconText
    }
    override fun getItemViewType(position: Int): Int {
        return itemList[position].type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view1 = LayoutInflater.from(parent.context).inflate(R.layout.notification_recyclerview_item2, parent,false)
        val view2 = LayoutInflater.from(parent.context).inflate(R.layout.notification_recyclerview_item, parent,false)
        return when (viewType) {
            NotificationItem.TYPE_1 -> {
                Type1ViewHolder(NotificationRecyclerviewItem2Binding.bind(view1))
            }
            else -> {
                Type2ViewHolder(NotificationRecyclerviewItemBinding.bind(view2))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(itemList[position].type){
            NotificationItem.TYPE_1 -> {
                (holder as Type1ViewHolder).title_textview.text = itemList[position].title1
                holder.content_textview.text = itemList[position].content
                holder.date_textview.text = itemList[position].date
            }
            NotificationItem.TYPE_2 -> {
                (holder as Type2ViewHolder).title1_textview.text = itemList[position].title1
                holder.title2_textview.text = itemList[position].title2
                holder.content_textview.text = itemList[position].content
                holder.date_textview.text = itemList[position].date
                holder.icon.setImageResource(R.drawable.notification_icon_comment)
                holder.state_textview.text = "댓글"
            }
            NotificationItem.TYPE_3 -> {
                (holder as Type2ViewHolder).title1_textview.text = itemList[position].title1
                holder.title2_textview.text = itemList[position].title2
                holder.content_textview.text = itemList[position].content
                holder.date_textview.text = itemList[position].date
                holder.icon.setImageResource(R.drawable.notification_icon_approval)
                holder.state_textview.text = "승인"
            }
            NotificationItem.TYPE_4 -> {
                (holder as Type2ViewHolder).title1_textview.text = itemList[position].title1
                holder.title2_textview.text = itemList[position].title2
                holder.content_textview.text = itemList[position].content
                holder.date_textview.text = itemList[position].date
                holder.icon.setImageResource(R.drawable.notification_icon_refusal)
                holder.state_textview.text = "반려"
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }
}