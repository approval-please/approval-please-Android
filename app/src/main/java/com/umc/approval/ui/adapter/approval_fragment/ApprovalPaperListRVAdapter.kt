package com.umc.approval.ui.adapter.approval_fragment

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.R
import com.umc.approval.databinding.ApprovalFragmentItemApprovalPaperBinding
import com.umc.approval.util.ApprovalPaper

class ApprovalPaperListRVAdapter(private val dataList: ArrayList<ApprovalPaper> = arrayListOf()): RecyclerView.Adapter<ApprovalPaperListRVAdapter.DataViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = ApprovalFragmentItemApprovalPaperBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    inner class DataViewHolder(private val binding: ApprovalFragmentItemApprovalPaperBinding, context: Context): RecyclerView.ViewHolder(binding.root) {
        val context = context
        fun bind(data: ApprovalPaper) {
            binding.tvTitle.text = data.title
            binding.tvContent.text = data.content
//            binding.tvApprovalPaperApproveCount.text = data.approve_count.toString()
//            binding.tvApprovalPaperRejectCount.text = data.reject_count.toString()
            binding.tvViews.text = data.views.toString()

            binding.tvApprovalPaperInfo.text = "${data.department}∙${data.date}"  // 수정 필요

            // 결재 승인 상태에 따라 구분선 아래 부분(스탬프 or 승인/반려 수) 뷰 생성 및 추가
            if (data.approval_status && data.approval_result && binding.approveStatusBarContainer.childCount == 0) {
                // 승인 완료, 승인됨
                addApproveStampView()

            } else if (data.approval_status && binding.approveStatusBarContainer.childCount == 0){
                // 승인 완료, 반려됨
                addRejectStampView()
            } else {
                // 승인 대기중
                addCountView(data)
            }

            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                itemView.setOnClickListener {
                    listner?.onItemClick(itemView, data, pos)
                }
            }
        }

        private fun addApproveStampView() {
            binding.ivApprovalStateCircle.setImageResource(R.drawable.approval_fragment_approvel_status_circle_complete)
            binding.tvApprovalState.text = "승인완료"

            // 스탬프 이미지뷰 생성
            val stamp = ImageView(context)
            stamp.setImageResource(R.drawable.approval_fragment_approve_stamp)

            // 레이아웃 파라미터 설정
            val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

            // 이미지뷰 추가
            binding.approveStatusBarContainer.addView(stamp, lp)
        }

        private fun addRejectStampView() {
            binding.ivApprovalStateCircle.setImageResource(R.drawable.approval_fragment_approvel_status_circle_complete)
            binding.tvApprovalState.text = "승인완료"

            // 스탬프 이미지뷰 생성
            val stamp = ImageView(context)
            stamp.setImageResource(R.drawable.approval_fragment_reject_stamp)

            // 레이아웃 파라미터 설정
            val stampLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

            // 이미지뷰 추가
            binding.approveStatusBarContainer.addView(stamp, stampLayoutParams)
        }

        private fun addCountView(data: ApprovalPaper) {

            if (binding.approveStatusBarContainer.childCount == 0) {
                binding.ivApprovalStateCircle.setImageResource(R.drawable.approval_fragment_approval_status_circle_pending)
                binding.tvApprovalState.text = "승인대기중"

                // 승인, 반려 수 표시 뷰 생성
                // (1) 승인 - 리니어 레이아웃 생성
                val approveLayout = LinearLayout(context)
                val approveLayoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                approveLayout.orientation = LinearLayout.HORIZONTAL
                approveLayout.gravity = Gravity.CENTER
                approveLayoutParams.weight = 1f
                binding.approveStatusBarContainer.addView(approveLayout, approveLayoutParams)

                // 구분선
                val divisionLine = View(context)
                divisionLine.setBackgroundColor(Color.LTGRAY)
                val divisionLineParams = LinearLayout.LayoutParams(
                    3,
                    60
                )
                binding.approveStatusBarContainer.addView(divisionLine, divisionLineParams)

                // (2) 반려 - 리니어 레이아웃 생성
                val rejectLayout = LinearLayout(context)
                val rejectLayoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                rejectLayout.orientation = LinearLayout.HORIZONTAL
                rejectLayout.gravity = Gravity.CENTER
                rejectLayoutParams.weight = 1f
                binding.approveStatusBarContainer.addView(rejectLayout, rejectLayoutParams)

                // (3) 승인/반려 레이아웃 내 아이템 생성
                // 승인
                val approveIconImage = ImageView(context)
                approveIconImage.setImageResource(R.drawable.approval_fragment_approve_icon)
                approveIconImage.setPadding(0, 0, 10, 0)
                approveLayout.addView(approveIconImage)

                val approveText = TextView(context)
                approveText.text = "승인"
                approveText.textSize = 11f
                approveText.setTextColor(Color.GRAY)
                approveText.setPadding(0, 0, 8, 0)
                approveLayout.addView(approveText)

                val approveCount = TextView(context)
                approveCount.text = data.approve_count.toString()
                approveCount.textSize = 11f
                approveCount.setTextColor(Color.BLACK)
                approveLayout.addView(approveCount)
                
                // 반려
                val rejectIconImage = ImageView(context)
                rejectIconImage.setImageResource(R.drawable.approval_fragment_reject_icon)
                rejectIconImage.setPadding(0, 0, 10, 0)
                rejectLayout.addView(rejectIconImage)

                val rejectText = TextView(context)
                rejectText.text = "반려"
                rejectText.textSize = 11f
                rejectText.setTextColor(Color.GRAY)
                rejectText.setPadding(0, 0, 8, 0)
                rejectLayout.addView(rejectText)

                val rejectCount = TextView(context)
                rejectCount.text = data.reject_count.toString()
                rejectCount.textSize = 11f
                rejectCount.setTextColor(Color.BLACK)
                rejectLayout.addView(rejectCount)
            }
        }
    }

    // 아이템 클릭 리스너
    interface OnItemClickListner {
        fun onItemClick(v: View, data: ApprovalPaper, pos: Int)
    }
    private var listner: OnItemClickListner?= null

    fun setOnItemClickListener(listner: OnItemClickListner) {
        this.listner = listner
    }

}