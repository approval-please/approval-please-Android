package Collie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.R
import com.umc.approval.databinding.NotificationRecyclerviewItemBinding

class NotificationAdapter(val itemList: ArrayList<NotificationItem>)
    : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {
    inner class NotificationViewHolder(val binding : NotificationRecyclerviewItemBinding) : RecyclerView.ViewHolder(binding.root){
        val title1_textview = binding.notificationItemTitle1
        val title2_textview = binding.notificationItemTitle2
        val content_textview = binding.notificationItemContent
        val date_textview = binding.notificationItemDate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notification_recyclerview_item, parent,false)
        return NotificationViewHolder(NotificationRecyclerviewItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.title1_textview.text = itemList[position].title1
        holder.title2_textview.text = itemList[position].title2
        holder.content_textview.text = itemList[position].content
        holder.date_textview.text = itemList[position].date
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }
}