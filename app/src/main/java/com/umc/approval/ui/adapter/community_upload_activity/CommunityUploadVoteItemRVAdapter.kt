package com.umc.approval.ui.adapter.community_upload_activity

import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.databinding.ItemUploadVoteBinding

class CommunityUploadVoteItemRVAdapter (private var dataList: MutableList<String>): RecyclerView.Adapter<CommunityUploadVoteItemRVAdapter.DataViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding =
            ItemUploadVoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    inner class DataViewHolder(private val binding: ItemUploadVoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: String) {
            binding.voteItemEt.setText(data)

            binding.voteItemEt.addTextChangedListener { text: Editable? ->
                text?.let {
                    dataList[adapterPosition]= it.toString()
                }
            }

            binding.voteItemEraseBtn.setOnClickListener{
                if(dataList.size > 2){
                    dataList.removeAt(adapterPosition)
                }else{
                    dataList[adapterPosition] = ""
                }
            }

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
        fun onItemClick(v: View, data: String, pos: Int){}
    }

    private var listner: OnItemClickListner? = null

    fun setOnItemClickListener(listner: OnItemClickListner) {
        this.listner = listner
    }
}