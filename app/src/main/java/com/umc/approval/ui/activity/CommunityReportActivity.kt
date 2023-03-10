package com.umc.approval.ui.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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

    /*???????????????*/
    private lateinit var activityCommunityReportPostDialogBinding: ActivityCommunityReportPostDialogBinding
    private lateinit var activityCommunityReportUserDialogBinding: ActivityCommunityReportUserDialogBinding
    private lateinit var activityCommunityRemovePostDialogBinding: ActivityCommunityRemovePostDialogBinding

    /*??????????????? ??????*/
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

        //follow??????
        binding.follow.setOnClickListener {
            followViewModel.follow(reportViewModel.report.value!!.userId!!)
        }

        //unfollow??????
        binding.unfollow.setOnClickListener {
            followViewModel.follow(reportViewModel.report.value!!.userId!!)
        }

        //?????????
        binding.postLikeState.setOnClickListener {

            if (reportViewModel.accessToken.value == false) {
                BlackToast.createToast(this, "???????????? ????????? ????????? ?????????").show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                followViewModel.like(reportId = reportViewModel.report.value!!.reportId)
            }
        }


        //?????? ?????? ??? ?????? ??????
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
                BlackToast.createToast(this, "???????????? ????????? ????????? ?????????").show()
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

        //?????? ????????? ??????
        binding.communityPostUserName.setOnClickListener {
            if (reportViewModel.report.value!!.writerOrNot == false) {
                val intent = Intent(this, OtherpageActivity::class.java)
                intent.putExtra("userId", reportViewModel.report.value!!.userId)
                startActivity(intent)
            }
        }


        // ????????? ?????? ?????? ??????
        binding.communityPostLikeNum.setOnClickListener {
            // ?????? ????????? ID??? ??????
            val intent = Intent(this, LikeActivity::class.java)
            intent.putExtra("type", "report")
            intent.putExtra("id", reportViewModel.report.value!!.reportId)
            startActivity(intent)
        }
    }

    private fun live_data() {

        //?????? ????????? ??????
        followViewModel.commentLike.observe(this) {
            commentViewModel.get_comments(reportId = reportViewModel.report.value!!.reportId.toString())
        }

        //??????
        followViewModel.like.observe(this) {
            if (it == true) {
                binding.postLikeState.setImageResource(R.drawable.community_post_like_btn)
            } else {
                binding.postLikeState.setImageResource(R.drawable.community_post_unlike_btn)
            }
        }

        //????????? ??????
        followViewModel.isFollow.observe(this) {
            if (it.isFollow == false) {
                binding.unfollow.isVisible = true
                binding.follow.isVisible = false
            } else {
                binding.unfollow.isVisible = false
                binding.follow.isVisible = true
            }
        }

        //????????? ????????? ??????????????? ??????
        reportViewModel.report.observe(this) {

            commentViewModel.get_comments(reportId = it.reportId.toString())

            //????????? ????????? ??????
            if (it.profileImage != null) {
                binding.communityPostUserProfile.load(it.profileImage)
                binding.communityPostUserProfile.clipToOutline = true
            } else {
                binding.communityPostUserProfile.load(levelImage[it.level])
            }

            binding.voteNum.text = it.commentCount.toString()

            //?????????
            binding.communityPostUserName.text = it.nickname
            //??????
            binding.rank.text = Utils.level[it.level]
            //????????????
            binding.communityPostCategory.text = categoryMap[it.documentCategory]
            //??????
            binding.communityPostTime.text = it.datetime
            //?????? ??????
            binding.communityDocumentLayout.documentTitle.text = it.documentTitle
            //?????? ??????
            binding.communityDocumentLayout.documentContent.text = it.documentContent
            //????????? ?????? ??????
            binding.communityPostContent.text = it.reportContent

            //?????????
            binding.communityPostLikeNum.text = "????????? "+ it.likedCount
            //?????????
            binding.communityPostScrapNum.text = "????????? "+ it.scrapCount
            //?????????
            binding.communityPostVisitorsNum.text = "????????? "+ it.view

            if (it.likeOrNot == true) {
                binding.postLikeState.setImageResource(R.drawable.community_post_like_btn)
            } else {
                binding.postLikeState.setImageResource(R.drawable.community_post_unlike_btn)
            }

            if (it.writerOrNot == true) {
                binding.unfollow.isVisible = false
                binding.follow.isVisible = false
            } else {
                //follow?????????
                if (it.followOrNot == true) {
                    followViewModel.setFollow(FollowStateDto(true))
                } else {
                    followViewModel.setFollow(FollowStateDto(false))
                }
            }

            //scrap?????????
            if (it.scrapOrNot == true) {
                followViewModel.setScrap(ScrapStateDto(true))
            } else {
                followViewModel.setScrap(ScrapStateDto(false))
            }

            //?????????????????????
            if (it.isNotification == true) {
                followViewModel.setNotification(NotificationStateDto(true))
            } else {
                followViewModel.setNotification(NotificationStateDto(false))
            }

            //?????? ??????
            if (it.documentTag == null || it.documentTag.isEmpty()) {
                binding.communityDocumentLayout.documentHashtagItem.isVisible = false
            } else {
                val dataRVAdapter = UploadHashtagRVAdapter(it.documentTag)
                binding.communityDocumentLayout.documentHashtagItem.adapter = dataRVAdapter
                binding.communityDocumentLayout.documentHashtagItem.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
            }

            //?????? ????????? ??????
            if(it.documentImageCount == 0) {
                binding.communityDocumentLayout.documentImageCountTv.isVisible = false
                binding.communityDocumentLayout.ivApprovalReportThumbnail.isVisible = false
            } else {
                binding.communityDocumentLayout.documentImageCountTv.text = it.documentImageCount.toString()
                binding.communityDocumentLayout.ivApprovalReportThumbnail.load(it.documentImageUrl)
                binding.communityDocumentLayout.ivApprovalReportThumbnail.clipToOutline = true
            }

            //????????? ????????? ??????
            if(it.reportImageUrl == null || it.reportImageUrl.isEmpty()) {
                binding.imageRv.isVisible = false
            } else {
                var imageRVAdapter = CommunityImageRVAdapter(it.reportImageUrl)
                binding.imageRv.adapter = imageRVAdapter
                binding.imageRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            }

            //????????? ?????? ??????
            if(it.reportLink == null || it.reportLink.isEmpty()) {
                binding.uploadLinkItem.isVisible = false
            } else {
                val dataRVAdapter = CommunityUploadLinkItemRVAdapter(it.reportLink)
                binding.uploadLinkItem.adapter = dataRVAdapter
                binding.uploadLinkItem.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
            }

            //????????? ?????? ??????
            if(it.reportTag == null || it.reportTag.isEmpty()) {
                binding.uploadHashtagItem.isVisible = false
            } else {
                val reportTagRVAdapter = UploadHashtagRVAdapter(it.reportTag)
                binding.uploadHashtagItem.adapter = reportTagRVAdapter
                binding.uploadHashtagItem.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
            }
        }

        //?????? ????????? ?????????
        commentViewModel.comments.observe(this) {

            /**????????? ??????????????? ??????*/
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
                        BlackToast.createToast(baseContext, "?????? ????????? ?????????????????????").show()
                    } else {
                        BlackToast.createToast(baseContext, "????????? ?????????????????????").show()
                        commentViewModel.setParentCommentId(data.commentId)
                    }
                }

                /**?????? ??????????????? ??????*/
                override fun setting_comment(v: View, data: CommentDto, pos: Int, context: Context) {
                    val writer = reportViewModel.report.value!!.writerOrNot

                    val bottomSheetView =
                        layoutInflater.inflate(R.layout.community_comment_selector_dialog, null)
                    val bottomSheetDialog = BottomSheetDialog(context)
                    bottomSheetDialog.setContentView(bottomSheetView)
                    bottomSheetDialog.show()

                    //dialog??? view Component ??????
                    val setting_report_post = bottomSheetView.findViewById<LinearLayout>(R.id.setting_report_post)
                    val setting_report_user = bottomSheetView.findViewById<LinearLayout>(R.id.setting_report_user)
                    val setting_remove_post = bottomSheetView.findViewById<LinearLayout>(R.id.setting_remove_post)

                    // visible ??????
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

        /**AccessToken ???????????? ????????? ???????????? ????????? ??????*/
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

            //dialog??? view Component ??????
            val setting_notice_on = bottomSheetView.findViewById<LinearLayout>(R.id.setting_notice_on)
            val setting_notice_off = bottomSheetView.findViewById<LinearLayout>(R.id.setting_notice_off)
            val setting_storage_on = bottomSheetView.findViewById<LinearLayout>(R.id.setting_storage_on)
            val setting_storage_off = bottomSheetView.findViewById<LinearLayout>(R.id.setting_storage_off)
            val setting_report_post = bottomSheetView.findViewById<LinearLayout>(R.id.setting_report_post)
            val setting_report_user = bottomSheetView.findViewById<LinearLayout>(R.id.setting_report_user)
            val setting_remove_post = bottomSheetView.findViewById<LinearLayout>(R.id.setting_remove_post)

            // visible ??????
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

            // ??????????????? ?????? ?????????
            setting_notice_on!!.setOnClickListener {
                followViewModel.notification(reportId = reportViewModel.report.value!!.reportId)
                bottomSheetDialog.cancel()
                BlackToast.createToast(this, "??? ?????????????????? ????????? ?????????").show()
            }

            setting_notice_off!!.setOnClickListener {
                followViewModel.notification(reportId = reportViewModel.report.value!!.reportId)
                bottomSheetDialog.cancel()
                BlackToast.createToast(this, "??? ?????????????????? ????????? ??? ?????? ?????? ?????????").show()
            }

            setting_storage_on!!.setOnClickListener {
                followViewModel.scrap(reportId = reportViewModel.report.value!!.reportId)
                bottomSheetDialog.cancel()
                BlackToast.createToast(this, "??? ?????????????????? ???????????? ????????????").show()
            }

            setting_storage_off!!.setOnClickListener {
                followViewModel.scrap(reportId = reportViewModel.report.value!!.reportId)
                bottomSheetDialog.cancel()
                BlackToast.createToast(this, "??? ?????????????????? ??????????????? ?????????").show()
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

        /*????????????*/
        dialogCancelButton.setOnClickListener {
            linkDialog.dismiss()
        }

        /*????????????*/
        dialogConfirmButton.setOnClickListener{
            linkDialog.dismiss()
            reportViewModel.delete_report(reportViewModel.report.value!!.reportId.toString())
            Handler(Looper.myLooper()!!).postDelayed({
                finish()
            }, 500)
        }
        /*link ??????*/
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

        /*????????????*/
        dialogCancelButton.setOnClickListener {
            linkDialog.dismiss()
        }

        /*????????????*/
        dialogConfirmButton.setOnClickListener{
            followViewModel.accuse(accuseUserId = reportViewModel.report.value!!.userId)
            linkDialog.dismiss()
            BlackToast.createToast(this, "????????? ?????????????????????.").show()
        }

        /*link ??????*/
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

        /*????????????*/
        dialogCancelButton.setOnClickListener {
            linkDialog.dismiss()
        }

        /*????????????*/
        dialogConfirmButton.setOnClickListener{
            followViewModel.accuse(reportId = reportViewModel.report.value!!.reportId)
            linkDialog.dismiss()
            BlackToast.createToast(this, "????????? ?????????????????????.").show()
        }

        /*link ??????*/
        linkDialog.show()
    }

    /**?????? ??????????????? ??????*/
    private fun showRemoveCommentDialog(commentId: Int) {
        val linkDialog : Dialog = Dialog(this);
        activityCommunityRemovePostDialogBinding = ActivityCommunityRemovePostDialogBinding.inflate(layoutInflater)

        linkDialog.setContentView(activityCommunityRemovePostDialogBinding.root)
        linkDialog.setCanceledOnTouchOutside(true)
        linkDialog.setCancelable(true)
        dialogCancelButton = activityCommunityRemovePostDialogBinding.communityDialogCancelButton
        dialogConfirmButton = activityCommunityRemovePostDialogBinding.communityDialogConfirmButton

        val text = activityCommunityRemovePostDialogBinding.communityDialog
        text.setText("??? ????????? ?????????????????????????")

        dialogConfirmButton.setText("?????? ????????????")

        /*????????????*/
        dialogCancelButton.setOnClickListener {
            linkDialog.dismiss()
        }

        /*????????????*/
        dialogConfirmButton.setOnClickListener{
            linkDialog.dismiss()
            commentViewModel.delete_comments(commentId = commentId.toString(),
                reportId = reportViewModel.report.value!!.reportId.toString())
        }
        /*link ??????*/
        linkDialog.show()
    }

    //????????? ??????
    private fun showReportCommentUserDialog(userId: Int) {
        val linkDialog : Dialog = Dialog(this);
        activityCommunityReportUserDialogBinding = ActivityCommunityReportUserDialogBinding.inflate(layoutInflater)

        linkDialog.setContentView(activityCommunityReportUserDialogBinding.root)
        linkDialog.setCanceledOnTouchOutside(true)
        linkDialog.setCancelable(true)
        dialogCancelButton = activityCommunityReportUserDialogBinding.communityDialogCancelButton
        dialogConfirmButton = activityCommunityReportUserDialogBinding.communityDialogConfirmButton

        /*????????????*/
        dialogCancelButton.setOnClickListener {
            linkDialog.dismiss()
        }

        /*????????????*/
        dialogConfirmButton.setOnClickListener{
            followViewModel.accuse(accuseUserId = userId)
            linkDialog.dismiss()
            BlackToast.createToast(this, "????????? ?????????????????????.").show()
        }

        /*link ??????*/
        linkDialog.show()
    }

    //?????? ??????
    private fun showReportCommentDialog(commentId: Int) {
        val linkDialog : Dialog = Dialog(this);
        activityCommunityReportPostDialogBinding = ActivityCommunityReportPostDialogBinding.inflate(layoutInflater)

        linkDialog.setContentView(activityCommunityReportPostDialogBinding.root)
        linkDialog.setCanceledOnTouchOutside(true)
        linkDialog.setCancelable(true)
        dialogCancelButton = activityCommunityReportPostDialogBinding.communityDialogCancelButton
        dialogConfirmButton = activityCommunityReportPostDialogBinding.communityDialogConfirmButton

        val text = activityCommunityReportPostDialogBinding.communityDialog
        text.setText("??? ????????? ?????????????????????????")

        dialogConfirmButton.setText("?????? ????????????")
        /*????????????*/
        dialogCancelButton.setOnClickListener {
            linkDialog.dismiss()
        }

        /*????????????*/
        dialogConfirmButton.setOnClickListener{
            followViewModel.accuse(commentId = commentId)
            linkDialog.dismiss()
            BlackToast.createToast(this, "????????? ?????????????????????.").show()
        }

        /*link ??????*/
        linkDialog.show()
    }
}