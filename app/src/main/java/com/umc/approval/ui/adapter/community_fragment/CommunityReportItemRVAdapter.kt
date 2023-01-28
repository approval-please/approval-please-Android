package com.umc.approval.ui.adapter.community_fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.umc.approval.data.dto.community.get.CommunityReport
import com.umc.approval.data.dto.community.get.CommunityReportDto
import com.umc.approval.databinding.CommunityReportItemBinding

/**
 * 결재 톡톡 및 보고서를 받아와 연결해주는 RV Adapter
 * */
class CommunityReportItemRVAdapter(private val items : CommunityReportDto) : RecyclerView.Adapter<CommunityReportItemRVAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CommunityReportItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun binding(data : CommunityReport) {

            if (data.reportLink != null) {
                binding.reportOpenGraphImage.load(data.reportLink.get(0).image)
                binding.reportOpenGraphText.setText(data.reportLink.get(0).title)
                binding.reportOpenGraphUrl.setText(data.reportLink.get(0).url)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CommunityReportItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(items.communityReport[position])
        if (itemClick != null){
            holder.binding.reportDetail.setOnClickListener(View.OnClickListener {
                itemClick?.move_to_report_activity()
            })
        }
    }

    override fun getItemCount(): Int {
        return items.communityReport.size
    }

    /**RV item click event*/
    interface ItemClick{ //인터페이스
        fun move_to_report_activity()
    }

    var itemClick: ItemClick? = null
}