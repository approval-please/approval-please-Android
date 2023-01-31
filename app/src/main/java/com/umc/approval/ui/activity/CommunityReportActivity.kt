package com.umc.approval.ui.activity

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.umc.approval.R
import com.umc.approval.data.dto.opengraph.OpenGraphDto
import com.umc.approval.databinding.ActivityCommunityRemovePostDialogBinding
import com.umc.approval.databinding.ActivityCommunityReportBinding
import com.umc.approval.databinding.ActivityCommunityReportPostDialogBinding
import com.umc.approval.databinding.ActivityCommunityReportUserDialogBinding
import com.umc.approval.ui.adapter.community_post_activity.CommunityCommentRVAdapter
import com.umc.approval.ui.adapter.community_post_activity.CommunityImageRVAdapter
import com.umc.approval.ui.adapter.community_post_activity.CommunityVoteCompleteRVAdapter
import com.umc.approval.ui.adapter.community_upload_activity.CommunityUploadLinkItemRVAdapter
import com.umc.approval.ui.adapter.document_comment_activity.DocumentCommentAdapter
import com.umc.approval.ui.adapter.document_comment_activity.DocumentCommentItem
import com.umc.approval.ui.adapter.document_comment_activity.DocumentCommentItem2
import com.umc.approval.ui.adapter.upload_activity.UploadHashtagRVAdapter
import com.umc.approval.ui.viewmodel.comment.CommentViewModel
import com.umc.approval.ui.viewmodel.community.CommunityReportUploadViewModel
import com.umc.approval.ui.viewmodel.communityDetail.ReportViewModel
import com.umc.approval.util.CommentItem
import com.umc.approval.util.Utils.categoryMap
import com.umc.approval.util.VoteItem

class CommunityReportActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCommunityReportBinding

    val reportViewModel by viewModels<ReportViewModel>()

    val commentViewModel by viewModels<CommentViewModel>()

    /*다이얼로그*/
    private lateinit var activityCommunityReportPostDialogBinding: ActivityCommunityReportPostDialogBinding
    private lateinit var activityCommunityReportUserDialogBinding: ActivityCommunityReportUserDialogBinding
    private lateinit var activityCommunityRemovePostDialogBinding: ActivityCommunityRemovePostDialogBinding

    /*다이얼로그 버튼*/
    private lateinit var dialogCancelButton : Button
    private lateinit var dialogConfirmButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityReportBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        /*setting*/
        post_more()

        live_data()

        binding.communityDocumentLayout.documentBtn.setOnClickListener{
            val intent = Intent(this, DocumentActivity::class.java)
            intent.putExtra("documentId", reportViewModel.report.value!!.documentId.toString())
            startActivity(intent)
        }

        binding.communityPostLikeNum.setOnClickListener{
            startActivity(Intent(this,LikeActivity::class.java))
        }

        /*close*/
        binding.uploadCancelBtn.setOnClickListener{
            finish()
        }
    }

    private fun live_data() {

        //리포트 데이터 받아왔을때 처리
        reportViewModel.report.observe(this) {

            binding.communityDocumentLayout.documentTitle.text = it.documentTitle
            binding.communityDocumentLayout.documentContent.text = it.documentContent

            if (it.documentTag == null || it.documentTag.isEmpty()) {
                binding.communityDocumentLayout.documentHashtagItem.isVisible = false
            } else {
                val dataRVAdapter = UploadHashtagRVAdapter(it.documentTag)
                binding.communityDocumentLayout.documentHashtagItem.adapter = dataRVAdapter
                binding.communityDocumentLayout.documentHashtagItem.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
            }

            binding.communityPostUserProfile.load(it.profileImage)

            //report 이미지 처리
            if(it.documentImageCount == 0) {
                binding.communityDocumentLayout.documentImageCountTv.isVisible = false
                binding.communityDocumentLayout.ivApprovalReportThumbnail.isVisible = false
            } else {
                binding.communityDocumentLayout.documentImageCountTv.text = it.documentImageCount.toString()
                binding.communityDocumentLayout.ivApprovalReportThumbnail.load(it.documentImageUrl)
            }

            binding.communityPostCategory.text = categoryMap[it.documentCategory]
            binding.communityPostLikeNum.text = "좋아요 "+ it.likedCount
            binding.communityPostScrapNum.text = "스크랩 "+ it.scrapCount
            binding.communityPostVisitorsNum.text = "조회수 "+ it.view

            //report 이미지 처리
            if(it.reportImageUrl == null || it.reportImageUrl.isEmpty()) {
                binding.imageRv.isVisible = false
            } else {
                var imageRVAdapter = CommunityImageRVAdapter(it.reportImageUrl)
                binding.imageRv.adapter = imageRVAdapter
                binding.imageRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            }

            //report 이미지 처리
            if(it.reportLink == null || it.reportLink.isEmpty()) {
                binding.uploadLinkItem.isVisible = false
            } else {
                val dataRVAdapter = CommunityUploadLinkItemRVAdapter(it.reportLink)
                binding.uploadLinkItem.adapter = dataRVAdapter
                binding.uploadLinkItem.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
            }

            //report 이미지 처리
            if(it.reportLink == null || it.reportLink.isEmpty()) {
                binding.uploadHashtagItem.isVisible = false
            } else {
                val reportTagRVAdapter = UploadHashtagRVAdapter(it.reportTag)
                binding.uploadHashtagItem.adapter = reportTagRVAdapter
                binding.uploadHashtagItem.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
            }
        }
    }

    override fun onStart() {
        super.onStart()

        /**AccessToken 확인해서 로그인 상태인지 아닌지 확인*/
        reportViewModel.checkAccessToken()

        val reportId = intent.getStringExtra("reportId")

        reportViewModel.get_report_detail(reportId.toString())

        reportViewModel.init()
    }

    //뷰 재시작시 로그인 상태 검증 및 서류 정보 가지고 오는 로직
    override fun onResume() {
        super.onResume()

        /**AccessToken 확인해서 로그인 상태인지 아닌지 확인*/
        reportViewModel.checkAccessToken()

        val reportId = intent.getStringExtra("reportId")

        reportViewModel.get_report_detail(reportId.toString())

    }

    /**post more*/
    private fun post_more() {
        val writer = 0
        val notice = 0
        val storage = 0

        binding.uploadSettingBtn.setOnClickListener {

            val bottomSheetView =
                layoutInflater.inflate(R.layout.community_post_selector_dialog, null)
            val bottomSheetDialog = BottomSheetDialog(this)
            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()

            //dialog의 view Component 접근
            val setting_notice_on = bottomSheetView.findViewById<LinearLayout>(R.id.setting_notice_on)
            val setting_notice_off = bottomSheetView.findViewById<LinearLayout>(R.id.setting_notice_off)
            val setting_storage_on = bottomSheetView.findViewById<LinearLayout>(R.id.setting_storage_on)
            val setting_storage_off = bottomSheetView.findViewById<LinearLayout>(R.id.setting_storage_off)
            val setting_report_post = bottomSheetView.findViewById<LinearLayout>(R.id.setting_report_post)
            val setting_report_user = bottomSheetView.findViewById<LinearLayout>(R.id.setting_report_user)
            val setting_remove_post = bottomSheetView.findViewById<LinearLayout>(R.id.setting_remove_post)
            val setting_edit_post = bottomSheetView.findViewById<LinearLayout>(R.id.setting_edit_post)

            // visible 처리
            if(writer == 1){
                setting_report_post.isVisible = false
                setting_report_user.isVisible = false
                setting_remove_post.isVisible = true
                setting_edit_post.isVisible = true
            }else{
                setting_report_post.isVisible = true
                setting_report_user.isVisible = true
                setting_remove_post.isVisible = false
                setting_edit_post.isVisible = false
            }

            if(notice == 0){
                setting_notice_on.isVisible = true
                setting_notice_off.isVisible = false
            }else{
                setting_notice_on.isVisible = false
                setting_notice_off.isVisible = true
            }

            if(storage == 0){
                setting_storage_on.isVisible = true
                setting_storage_off.isVisible = false
            }else{
                setting_storage_on.isVisible = false
                setting_storage_off.isVisible = true
            }

            // 다이얼로그 클릭 이벤트
            setting_notice_on!!.setOnClickListener {
                bottomSheetDialog.cancel()
            }

            setting_notice_off!!.setOnClickListener {
                bottomSheetDialog.cancel()
            }

            setting_storage_on!!.setOnClickListener {
                bottomSheetDialog.cancel()
            }

            setting_storage_off!!.setOnClickListener {
                bottomSheetDialog.cancel()
            }

            setting_report_post!!.setOnClickListener {
                showReportPostDialog()
                bottomSheetDialog.cancel()
            }

            setting_report_user!!.setOnClickListener {
                showReportUserDialog()
                bottomSheetDialog.cancel()
            }

            setting_remove_post!!.setOnClickListener {
                showRemovePostDialog()
                bottomSheetDialog.cancel()
            }

            setting_edit_post!!.setOnClickListener {
                bottomSheetDialog.cancel()
            }

        }
    }

    private fun showRemovePostDialog(){
        val linkDialog : Dialog = Dialog(this);
        activityCommunityRemovePostDialogBinding = ActivityCommunityRemovePostDialogBinding.inflate(layoutInflater)

        linkDialog.setContentView(activityCommunityRemovePostDialogBinding.root)
        linkDialog.setCanceledOnTouchOutside(true)
        linkDialog.setCancelable(true)
        dialogCancelButton = activityCommunityRemovePostDialogBinding.communityDialogCancelButton
        dialogConfirmButton = activityCommunityRemovePostDialogBinding.communityDialogConfirmButton

        /*취소버튼*/
        dialogCancelButton.setOnClickListener {
            linkDialog.dismiss()
        }

        /*확인버튼*/
        dialogConfirmButton.setOnClickListener{
            linkDialog.dismiss()
        }
        /*link 팝업*/
        linkDialog.show()
    }

    private fun showReportUserDialog(){
        val linkDialog : Dialog = Dialog(this);
        activityCommunityReportUserDialogBinding = ActivityCommunityReportUserDialogBinding.inflate(layoutInflater)

        linkDialog.setContentView(activityCommunityReportUserDialogBinding.root)
        linkDialog.setCanceledOnTouchOutside(true)
        linkDialog.setCancelable(true)
        dialogCancelButton = activityCommunityReportUserDialogBinding.communityDialogCancelButton
        dialogConfirmButton = activityCommunityReportUserDialogBinding.communityDialogConfirmButton

        /*취소버튼*/
        dialogCancelButton.setOnClickListener {
            linkDialog.dismiss()
        }

        /*확인버튼*/
        dialogConfirmButton.setOnClickListener{
            linkDialog.dismiss()
        }

        /*link 팝업*/
        linkDialog.show()
    }

    private fun showReportPostDialog(){
        val linkDialog : Dialog = Dialog(this);
        activityCommunityReportPostDialogBinding = ActivityCommunityReportPostDialogBinding.inflate(layoutInflater)

        linkDialog.setContentView(activityCommunityReportPostDialogBinding.root)
        linkDialog.setCanceledOnTouchOutside(true)
        linkDialog.setCancelable(true)
        dialogCancelButton = activityCommunityReportPostDialogBinding.communityDialogCancelButton
        dialogConfirmButton = activityCommunityReportPostDialogBinding.communityDialogConfirmButton

        /*취소버튼*/
        dialogCancelButton.setOnClickListener {
            linkDialog.dismiss()
        }

        /*확인버튼*/
        dialogConfirmButton.setOnClickListener{
            linkDialog.dismiss()
        }

        /*link 팝업*/
        linkDialog.show()
    }

//    data class CommentItem(
//        val id:Int,
//        val user_nickname: String,
//        val user_rank :String,
//        val content:String,
//        val date : String,
//        val like : Int,
//        val replyComment : Int,
//    )

}