package com.umc.approval.ui.adapter.community_upload_activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.umc.approval.data.dto.opengraph.OpenGraphDto
import com.umc.approval.databinding.ItemUploadLinkBinding

class CommunityUploadLinkItemRVAdapter (private var dataList: ArrayList<OpenGraphDto> = arrayListOf()): RecyclerView.Adapter<CommunityUploadLinkItemRVAdapter.DataViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding =
            ItemUploadLinkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    inner class DataViewHolder(private val binding: ItemUploadLinkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: OpenGraphDto) {

            binding.openGraphText.setText(data.title)
            binding.openGraphUrl.setText(data.url)
            binding.openGraphImage.load(data.image)

            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                itemView.setOnClickListener {
                    listner?.onItemClick(itemView, data, pos)
                }
            }
        }
    }

    // 아이템 클릭 리스너
    interface OnItemClickListner {
        fun onItemClick(v: View, data: OpenGraphDto, pos: Int){}
    }

    private var listner: OnItemClickListner? = null

    fun setOnItemClickListener(listner: OnItemClickListner) {
        this.listner = listner
    }
}