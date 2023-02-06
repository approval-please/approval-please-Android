package com.umc.approval.ui.activity

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.umc.approval.R
import com.umc.approval.data.dto.comment.get.CommentDto
import com.umc.approval.data.dto.comment.post.CommentPostDto
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
import com.umc.approval.ui.adapter.document_comment_activity.ParentCommentAdapter
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

        //작성 누를 시 댓글 작성
        binding.writeButton.setOnClickListener {
            if (reportViewModel.accessToken.value != false) {
                val postComment = CommentPostDto(reportId = reportViewModel.report.value!!.reportId ,
                    content = binding.communityCommentEt.text.toString(), parentCommentId = null)

                if (commentViewModel.commentId.value != -1) {
                    postComment.parentCommentId = commentViewModel.commentId.value
                }

                commentViewModel.post_comments(postComment, reportId = reportViewModel.report.value!!.reportId.toString())
                binding.communityCommentEt.text.clear()
                commentViewModel.setParentCommentId(-1)
            } else {
                Toast.makeText(this, "로그인 과정이 필요합니다", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }

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
            binding.communityPostUserName.text = it.nickname

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
            if(it.reportTag == null || it.reportTag.isEmpty()) {
                binding.uploadHashtagItem.isVisible = false
            } else {
                val reportTagRVAdapter = UploadHashtagRVAdapter(it.reportTag)
                binding.uploadHashtagItem.adapter = reportTagRVAdapter
                binding.uploadHashtagItem.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
            }
        }

        //댓글 라이브 데이터
        commentViewModel.comments.observe(this) {

            val documentCommentAdapter = ParentCommentAdapter(it)
            binding.commentItem.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            binding.commentItem.adapter = documentCommentAdapter

            documentCommentAdapter.itemClick = object : ParentCommentAdapter.ItemClick {

                override fun make_chid_comment(v: View, data: CommentDto, pos: Int) {
                    if (data.commentId.toString() == commentViewModel.commentId.value.toString()) {
                        commentViewModel.setParentCommentId(-1)
                    } else {
                        commentViewModel.setParentCommentId(data.commentId)
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        /**AccessToken 확인해서 로그인 상태인지 아닌지 확인*/
        reportViewModel.checkAccessToken()

        val reportId = intent.getStringExtra("reportId")

        reportViewModel.get_report_detail(reportId.toString())

        commentViewModel.get_comments(reportId = reportId.toString())

//        reportViewModel.init()
    }

    //다이얼로그 로직
    private fun post_more() {

        binding.uploadSettingBtn.setOnClickListener {

            val writer = reportViewModel.report.value!!.writerOrNot
            val notice = reportViewModel.report.value!!.isNotification
            val storage = reportViewModel.report.value!!.scrapOrNot

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

            // visible 처리
            if(writer == true){
                setting_report_post.isVisible = false
                setting_report_user.isVisible = false
                setting_remove_post.isVisible = true
            }else{
                setting_report_post.isVisible = true
                setting_report_user.isVisible = true
                setting_remove_post.isVisible = false
            }

            if(notice == false){
                setting_notice_on.isVisible = true
                setting_notice_off.isVisible = false
            }else{
                setting_notice_on.isVisible = false
                setting_notice_off.isVisible = true
            }

            if(storage == false){
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
}