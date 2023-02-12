package com.umc.approval.ui.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.umc.approval.check.collie.OtherpageActivity
import com.umc.approval.data.dto.comment.get.CommentDto
import com.umc.approval.data.dto.comment.post.CommentPostDto
import com.umc.approval.data.dto.follow.FollowStateDto
import com.umc.approval.data.dto.follow.NotificationStateDto
import com.umc.approval.data.dto.follow.ScrapStateDto
import com.umc.approval.databinding.ActivityCommunityRemovePostDialogBinding
import com.umc.approval.databinding.ActivityCommunityReportBinding
import com.umc.approval.databinding.ActivityCommunityReportPostDialogBinding
import com.umc.approval.databinding.ActivityCommunityReportUserDialogBinding
import com.umc.approval.ui.adapter.community_post_activity.CommunityImageRVAdapter
import com.umc.approval.ui.adapter.community_upload_activity.CommunityUploadLinkItemRVAdapter
import com.umc.approval.ui.adapter.document_comment_activity.ParentCommentAdapter
import com.umc.approval.ui.adapter.upload_activity.UploadHashtagRVAdapter
import com.umc.approval.ui.viewmodel.comment.CommentViewModel
import com.umc.approval.ui.viewmodel.communityDetail.ReportViewModel
import com.umc.approval.ui.viewmodel.follow.FollowViewModel
import com.umc.approval.util.BlackToast
import com.umc.approval.util.Utils
import com.umc.approval.util.Utils.categoryMap
import com.umc.approval.util.Utils.levelImage

class CommunityReportActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCommunityReportBinding

    val reportViewModel by viewModels<ReportViewModel>()

    val commentViewModel by viewModels<CommentViewModel>()

    //viewModel
    private val followViewModel by viewModels<FollowViewModel>()

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

        //follow하면
        binding.follow.setOnClickListener {
            followViewModel.follow(reportViewModel.report.value!!.userId!!)
        }

        //unfollow하면
        binding.unfollow.setOnClickListener {
            followViewModel.follow(reportViewModel.report.value!!.userId!!)
        }

        //좋아요
        binding.postLikeState.setOnClickListener {

            if (reportViewModel.accessToken.value == false) {
                BlackToast.createToast(this, "로그인이 필요한 서비스 입니다").show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                followViewModel.like(reportId = reportViewModel.report.value!!.reportId)
            }
        }


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
                BlackToast.createToast(this, "로그인이 필요한 서비스 입니다").show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        binding.communityDocumentLayout.documentBtn.setOnClickListener{
            val intent = Intent(this, DocumentActivity::class.java)
            intent.putExtra("documentId", reportViewModel.report.value!!.documentId.toString())
            startActivity(intent)
        }

        /*close*/
        binding.uploadCancelBtn.setOnClickListener{
            finish()
        }

        //이름 누를시 이동
        binding.communityPostUserName.setOnClickListener {
            if (reportViewModel.report.value!!.writerOrNot == false) {
                val intent = Intent(this, OtherpageActivity::class.java)
                intent.putExtra("userId", reportViewModel.report.value!!.userId)
                startActivity(intent)
            }
        }


        // 좋아요 누른 유저 확인
        binding.communityPostLikeBtn.setOnClickListener {
            // 결재 보고서 ID를 넘김
            val intent = Intent(this, LikeActivity::class.java)
            intent.putExtra("type", "report")
            intent.putExtra("id", reportViewModel.report.value!!.reportId)
            startActivity(intent)
        }
    }

    private fun live_data() {

        //댓글 좋아요 로직
        followViewModel.commentLike.observe(this) {
            commentViewModel.get_comments(reportId = reportViewModel.report.value!!.reportId.toString())
        }

        //로직
        followViewModel.like.observe(this) {
            if (it == true) {
                binding.postLikeState.setImageResource(R.drawable.community_post_like_btn)
            } else {
                binding.postLikeState.setImageResource(R.drawable.community_post_unlike_btn)
            }
        }

        //팔로잉 로직
        followViewModel.isFollow.observe(this) {
            if (it.isFollow == false) {
                binding.unfollow.isVisible = true
                binding.follow.isVisible = false
            } else {
                binding.unfollow.isVisible = false
                binding.follow.isVisible = true
            }
        }

        //리포트 데이터 받아왔을때 처리
        reportViewModel.report.observe(this) {

            commentViewModel.get_comments(reportId = it.reportId.toString())

            //프로필 이미지 처리
            if (it.profileImage != null) {
                binding.communityPostUserProfile.load(it.profileImage)
                binding.communityPostUserProfile.clipToOutline = true
            } else {
                binding.communityPostUserProfile.load(levelImage[it.level])
            }


            //닉네임
            binding.communityPostUserName.text = it.nickname
            //직급
            binding.rank.text = Utils.level[it.level]
            //카테고리
            binding.communityPostCategory.text = categoryMap[it.documentCategory]
            //시간
            binding.communityPostTime.text = it.datetime
            //서류 제목
            binding.communityDocumentLayout.documentTitle.text = it.documentTitle
            //서류 내용
            binding.communityDocumentLayout.documentContent.text = it.documentContent
            //보고서 제목 내용
            binding.communityPostContent.text = it.reportContent

            //좋아요
            binding.communityPostLikeNum.text = "좋아요 "+ it.likedCount
            //스크랩
            binding.communityPostScrapNum.text = "스크랩 "+ it.scrapCount
            //조회수
            binding.communityPostVisitorsNum.text = "조회수 "+ it.view

            if (it.likeOrNot == true) {
                binding.postLikeState.setImageResource(R.drawable.community_post_like_btn)
            } else {
                binding.postLikeState.setImageResource(R.drawable.community_post_unlike_btn)
            }

            if (it.writerOrNot == true) {
                binding.unfollow.isVisible = false
                binding.follow.isVisible = false
            } else {
                //follow했다면
                if (it.followOrNot == true) {
                    followViewModel.setFollow(FollowStateDto(true))
                } else {
                    followViewModel.setFollow(FollowStateDto(false))
                }
            }

            //scrap했다면
            if (it.scrapOrNot == true) {
                followViewModel.setScrap(ScrapStateDto(true))
            } else {
                followViewModel.setScrap(ScrapStateDto(false))
            }

            //알림설정했다면
            if (it.isNotification == true) {
                followViewModel.setNotification(NotificationStateDto(true))
            } else {
                followViewModel.setNotification(NotificationStateDto(false))
            }

            //서류 태그
            if (it.documentTag == null || it.documentTag.isEmpty()) {
                binding.communityDocumentLayout.documentHashtagItem.isVisible = false
            } else {
                val dataRVAdapter = UploadHashtagRVAdapter(it.documentTag)
                binding.communityDocumentLayout.documentHashtagItem.adapter = dataRVAdapter
                binding.communityDocumentLayout.documentHashtagItem.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
            }

            //서류 이미지 처리
            if(it.documentImageCount == 0) {
                binding.communityDocumentLayout.documentImageCountTv.isVisible = false
                binding.communityDocumentLayout.ivApprovalReportThumbnail.isVisible = false
            } else {
                binding.communityDocumentLayout.documentImageCountTv.text = it.documentImageCount.toString()
                binding.communityDocumentLayout.ivApprovalReportThumbnail.load(it.documentImageUrl)
                binding.communityDocumentLayout.ivApprovalReportThumbnail.clipToOutline = true
            }

            //리포트 이미지 처리
            if(it.reportImageUrl == null || it.reportImageUrl.isEmpty()) {
                binding.imageRv.isVisible = false
            } else {
                var imageRVAdapter = CommunityImageRVAdapter(it.reportImageUrl)
                binding.imageRv.adapter = imageRVAdapter
                binding.imageRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            }

            //리프트 링크 처리
            if(it.reportLink == null || it.reportLink.isEmpty()) {
                binding.uploadLinkItem.isVisible = false
            } else {
                val dataRVAdapter = CommunityUploadLinkItemRVAdapter(it.reportLink)
                binding.uploadLinkItem.adapter = dataRVAdapter
                binding.uploadLinkItem.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
            }

            //리포트 태그 처리
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

            /**대댓글 다이얼로그 로직*/
            val documentCommentAdapter = ParentCommentAdapter(it, this, layoutInflater,
                reportViewModel.report.value!!.writerOrNot,
                followViewModel, commentViewModel, reportId = reportViewModel.report.value!!.reportId.toString())
            binding.commentItem.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            binding.commentItem.adapter = documentCommentAdapter

            documentCommentAdapter.itemClick = object : ParentCommentAdapter.ItemClick {

                override fun like(v: View, data: CommentDto, pos: Int) {
                    followViewModel.like(commentId = data.commentId)
                }

                override fun make_chid_comment(v: View, data: CommentDto, pos: Int) {
                    if (data.commentId.toString() == commentViewModel.commentId.value.toString()) {
                        commentViewModel.setParentCommentId(-1)
                        BlackToast.createToast(baseContext, "댓글 선택이 해제되었습니다").show()
                    } else {
                        BlackToast.createToast(baseContext, "댓글이 선택되었습니다").show()
                        commentViewModel.setParentCommentId(data.commentId)
                    }
                }

                /**댓글 다이얼로그 로직*/
                override fun setting_comment(v: View, data: CommentDto, pos: Int, context: Context) {
                    val writer = reportViewModel.report.value!!.writerOrNot

                    val bottomSheetView =
                        layoutInflater.inflate(R.layout.community_comment_selector_dialog, null)
                    val bottomSheetDialog = BottomSheetDialog(context)
                    bottomSheetDialog.setContentView(bottomSheetView)
                    bottomSheetDialog.show()

                    //dialog의 view Component 접근
                    val setting_report_post = bottomSheetView.findViewById<LinearLayout>(R.id.setting_report_post)
                    val setting_report_user = bottomSheetView.findViewById<LinearLayout>(R.id.setting_report_user)
                    val setting_remove_post = bottomSheetView.findViewById<LinearLayout>(R.id.setting_remove_post)

                    // visible 처리
                    if(data.isMy == true){
                        setting_report_post.isVisible = false
                        setting_report_user.isVisible = false
                        setting_remove_post.isVisible = true
                    }else{
                        setting_report_post.isVisible = true
                        setting_report_user.isVisible = true
                        setting_remove_post.isVisible = false
                    }

                    setting_report_post!!.setOnClickListener {
                        showReportCommentDialog(data.commentId)
                        bottomSheetDialog.cancel()
                    }

                    setting_report_user!!.setOnClickListener {
                        showReportCommentUserDialog(data.userId)
                        bottomSheetDialog.cancel()
                    }

                    setting_remove_post!!.setOnClickListener {
                        showRemoveCommentDialog(data.commentId)
                        bottomSheetDialog.cancel()
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
    }

    private fun post_more() {

        binding.uploadSettingBtn.setOnClickListener {

            val writer = reportViewModel.report.value!!.writerOrNot
            val notice = followViewModel.notif.value!!.isNotification
            val storage = followViewModel.isScrap.value!!.isScrap

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
                followViewModel.notification(reportId = reportViewModel.report.value!!.reportId)
                bottomSheetDialog.cancel()
                BlackToast.createToast(this, "이 결재보고서의 알람을 받아요").show()
            }

            setting_notice_off!!.setOnClickListener {
                followViewModel.notification(reportId = reportViewModel.report.value!!.reportId)
                bottomSheetDialog.cancel()
                BlackToast.createToast(this, "이 결재보고서의 알람을 더 이상 받지 않아요").show()
            }

            setting_storage_on!!.setOnClickListener {
                followViewModel.scrap(reportId = reportViewModel.report.value!!.reportId)
                bottomSheetDialog.cancel()
                BlackToast.createToast(this, "이 결재보고서를 보관함에 넣었어요").show()
            }

            setting_storage_off!!.setOnClickListener {
                followViewModel.scrap(reportId = reportViewModel.report.value!!.reportId)
                bottomSheetDialog.cancel()
                BlackToast.createToast(this, "이 결재보고서를 보관함에서 뺐어요").show()
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
            reportViewModel.delete_report(reportViewModel.report.value!!.reportId.toString())
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
            followViewModel.accuse(accuseUserId = reportViewModel.report.value!!.userId)
            linkDialog.dismiss()
            BlackToast.createToast(this, "신고가 접수되었습니다.").show()
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
            followViewModel.accuse(reportId = reportViewModel.report.value!!.reportId)
            linkDialog.dismiss()
            BlackToast.createToast(this, "신고가 접수되었습니다.").show()
        }

        /*link 팝업*/
        linkDialog.show()
    }

    /**댓글 다이얼로그 로직*/
    private fun showRemoveCommentDialog(commentId: Int) {
        val linkDialog : Dialog = Dialog(this);
        activityCommunityRemovePostDialogBinding = ActivityCommunityRemovePostDialogBinding.inflate(layoutInflater)

        linkDialog.setContentView(activityCommunityRemovePostDialogBinding.root)
        linkDialog.setCanceledOnTouchOutside(true)
        linkDialog.setCancelable(true)
        dialogCancelButton = activityCommunityRemovePostDialogBinding.communityDialogCancelButton
        dialogConfirmButton = activityCommunityRemovePostDialogBinding.communityDialogConfirmButton

        val text = activityCommunityRemovePostDialogBinding.communityDialog
        text.setText("이 댓글을 삭제하시겠습니까?")

        dialogConfirmButton.setText("댓글 삭제하기")

        /*취소버튼*/
        dialogCancelButton.setOnClickListener {
            linkDialog.dismiss()
        }

        /*확인버튼*/
        dialogConfirmButton.setOnClickListener{
            linkDialog.dismiss()
            commentViewModel.delete_comments(commentId = commentId.toString(),
                reportId = reportViewModel.report.value!!.reportId.toString())
        }
        /*link 팝업*/
        linkDialog.show()
    }

    //사용자 신고
    private fun showReportCommentUserDialog(userId: Int) {
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
            followViewModel.accuse(accuseUserId = userId)
            linkDialog.dismiss()
            BlackToast.createToast(this, "신고가 접수되었습니다.").show()
        }

        /*link 팝업*/
        linkDialog.show()
    }

    //댓글 신고
    private fun showReportCommentDialog(commentId: Int) {
        val linkDialog : Dialog = Dialog(this);
        activityCommunityReportPostDialogBinding = ActivityCommunityReportPostDialogBinding.inflate(layoutInflater)

        linkDialog.setContentView(activityCommunityReportPostDialogBinding.root)
        linkDialog.setCanceledOnTouchOutside(true)
        linkDialog.setCancelable(true)
        dialogCancelButton = activityCommunityReportPostDialogBinding.communityDialogCancelButton
        dialogConfirmButton = activityCommunityReportPostDialogBinding.communityDialogConfirmButton

        val text = activityCommunityReportPostDialogBinding.communityDialog
        text.setText("이 댓글을 신고하시겠습니까?")

        dialogConfirmButton.setText("댓글 신고하기")
        /*취소버튼*/
        dialogCancelButton.setOnClickListener {
            linkDialog.dismiss()
        }

        /*확인버튼*/
        dialogConfirmButton.setOnClickListener{
            followViewModel.accuse(commentId = commentId)
            linkDialog.dismiss()
            BlackToast.createToast(this, "신고가 접수되었습니다.").show()
        }

        /*link 팝업*/
        linkDialog.show()
    }
}