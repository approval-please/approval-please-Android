package com.umc.approval.ui.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.umc.approval.App
import com.umc.approval.databinding.ActivityDocumentBinding
import com.umc.approval.ui.viewmodel.approval.DocumentViewModel
import com.umc.approval.ui.viewmodel.comment.CommentViewModel
import com.umc.approval.R
import com.umc.approval.check.collie.OtherpageActivity
import com.umc.approval.data.dto.approval.post.AgreeMyPostDto
import com.umc.approval.data.dto.approval.post.AgreePostDto
import com.umc.approval.data.dto.comment.get.CommentDto
import com.umc.approval.data.dto.comment.post.CommentPostDto
import com.umc.approval.data.dto.follow.NotificationStateDto
import com.umc.approval.data.dto.follow.ScrapStateDto
import com.umc.approval.databinding.ActivityCommunityRemovePostDialogBinding
import com.umc.approval.databinding.ActivityCommunityReportPostDialogBinding
import com.umc.approval.databinding.ActivityCommunityReportUserDialogBinding
import com.umc.approval.ui.adapter.document_activity.DocumentImageAdapter
import com.umc.approval.ui.adapter.document_comment_activity.ParentCommentAdapter
import com.umc.approval.ui.adapter.upload_activity.UploadHashtagRVAdapter
import com.umc.approval.ui.fragment.document.ApproveDialog
import com.umc.approval.ui.fragment.document.RefuseDialog
import com.umc.approval.ui.viewmodel.follow.FollowViewModel
import com.umc.approval.util.BlackToast
import com.umc.approval.util.Utils
import com.umc.approval.util.Utils.categoryMap

class DocumentActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDocumentBinding

    /**Approval view model*/
    private val viewModel by viewModels<DocumentViewModel>()
    private val commentViewModel by viewModels<CommentViewModel>()

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

        binding = ActivityDocumentBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //?????? ????????? ???????????? ?????????
        move_to_other()

        approve_or_reject()

        post_more()

        //????????? ??????????????? View ??????
        live_data()

        binding.heart.setOnClickListener {

            if (viewModel.accessToken.value == false) {
                BlackToast.createToast(this, "???????????? ????????? ????????? ?????????").show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                followViewModel.like(documentId = viewModel.document.value!!.documentId)
            }
        }

        //?????? ?????? ??? ?????? ??????
        binding.writeButton.setOnClickListener {
            if (viewModel.accessToken.value != false) {
                val postComment = CommentPostDto(documentId = viewModel.document.value!!.documentId,
                    content = binding.commentEdit.text.toString(), parentCommentId = null)

                if (commentViewModel.commentId.value != -1) {
                    postComment.parentCommentId = commentViewModel.commentId.value
                }

                commentViewModel.post_comments(postComment, documentId = viewModel.document.value!!.documentId.toString())
                binding.commentEdit.text.clear()
                commentViewModel.setParentCommentId(-1)
            } else {
                BlackToast.createToast(this, "???????????? ????????? ????????? ?????????").show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        
        //????????? ?????? ??????
        binding.reportWriteButton.setOnClickListener {
            val intent = Intent(this, CommunityUploadActivity::class.java)

            intent.putExtra("documentId", viewModel.document.value!!.documentId.toString())
            intent.putExtra("report", true)

            startActivity(intent)

            finish()
        }

        //????????? ?????? ??????
        binding.reportCheckButton.setOnClickListener {
            /**???????????? ???????????? ??????*/
            val intent = Intent(this, CommunityReportActivity::class.java)
            intent.putExtra("reportId", viewModel.document.value!!.reportId.toString())
            startActivity(intent)
        }
        
        binding.shareButton.setOnClickListener {
            share()
        }

        // ????????? ?????? ?????? ??????
        binding.documentCommentPostLikes.setOnClickListener {
            // ?????? ?????? ID??? ??????
            val intent = Intent(this, LikeActivity::class.java)
            intent.putExtra("type", "document")
            intent.putExtra("id", viewModel.document.value!!.documentId)
            startActivity(intent)
        }

        //?????? ????????? ??????
        binding.name.setOnClickListener {
            if (viewModel.document.value!!.isWriter == false) {
                val intent = Intent(this, OtherpageActivity::class.java)
                intent.putExtra("userId", viewModel.document.value!!.userId)
                startActivity(intent)
            }
        }
    }

    /* ?????? ?????? ????????? ?????? ?????? ??? ??????????????? ?????? */
    private fun share(){
        var sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.setType("text/html")
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "postlink")
        startActivity(Intent.createChooser(sharingIntent, "sharing text"))
    }

    //?????? ?????? ?????? ?????? ?????? ??????
    private fun approve_or_reject() {
        //???????????? ????????? ????????? ???????????? ????????? ?????? ?????? ???, ????????? ????????? ????????? ?????? ?????????
        //????????? ?????? ??????????????? ?????? ??????
        binding.approveButton.setOnClickListener {
            if (viewModel.accessToken.value == true) {
                if (viewModel.document.value!!.isVoted == 0) {
                    val dialog = ApproveDialog()
                    dialog.show(supportFragmentManager, dialog.tag)
                }
            } else {
                BlackToast.createToast(this, "???????????? ????????? ????????? ?????????").show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        //???????????? ????????? ????????? ???????????? ????????? ?????? ?????? ???, ????????? ????????? ????????? ?????? ?????????
        //????????? ?????? ??????????????? ?????? ??????
        binding.refuseButton.setOnClickListener {
            if (viewModel.accessToken.value == true) {
                if (viewModel.document.value!!.isVoted == 0) {
                    val dialog = RefuseDialog()
                    dialog.show(supportFragmentManager, dialog.tag)
                }
            } else {
                BlackToast.createToast(this, "???????????? ????????? ????????? ?????????").show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    //????????? ?????????
    private fun live_data() {

        viewModel.after.observe(this) {
            binding.approveButton.text = "?????? " + it.approveCount
            binding.refuseButton.text = "?????? " + it.rejectCount
        }

        //????????? ??????
        followViewModel.like.observe(this) {
            if (it == true) {
                binding.heart.setImageResource(R.drawable.icon_heart_new_fill)
            } else {
                binding.heart.setImageResource(R.drawable.icon_heart_new)
            }
        }

        //?????? ????????? ??????
        followViewModel.commentLike.observe(this) {
            commentViewModel.get_comments(documentId = viewModel.document.value!!.documentId.toString())
        }

        //?????? ?????? ????????? ?????????
        viewModel.document.observe(this) {

            commentViewModel.get_comments(documentId = it.documentId.toString())

            followViewModel.setLike(it.isLiked!!)

            //scrap?????????
            if (it.isScrap == true) {
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

            binding.cate.text = categoryMap[it.category]

            //profile image
            if (it.profileImage != null) {
                binding.profile.load(it.profileImage)
            } else {
                binding.profile.load(Utils.levelImage[it.level])
            }
            binding.profile.clipToOutline = true

            binding.name.text = it.nickname
            binding.title.text = it.title
            binding.rank.text = setRank(it.level!!)
            binding.content.text = it.content
            binding.documentCommentPostLikes.text = "????????? " + it.likedCount.toString()
            binding.documentCommentPostViews.text = "????????? " + it.view.toString()
            binding.documentCommentPostComments.text = "?????? " + it.commentCount.toString()
            binding.documentCommentPostTime.text = it.datetime
            binding.approveButton.text = "?????? " + it.approveCount
            binding.refuseButton.text = "?????? " + it.rejectCount
            binding.approveNum.text = "?????? " + it.approveCount + "???"
            binding.rejectNum.text = "?????? " + it.rejectCount + "???"

            //?????? ??????
            if (it.tag != null && it.tag.isNotEmpty()) {
                val dataRVAdapter = UploadHashtagRVAdapter(it.tag)
                binding.uploadHashtagItem.adapter = dataRVAdapter
                binding.uploadHashtagItem.layoutManager = LinearLayoutManager(App.context(), RecyclerView.HORIZONTAL, false)
            }

            //?????? ??????
            if (it.link != null) {
                binding.openGraphImage.load(it.link.image)
                binding.openGraphText.text = it.link.title
                binding.openGraphUrl.text = it.link.url
            } else {
                binding.openGraph.isVisible = false
            }

            if (it.imageUrl != null) {

                val list1 = mutableListOf<String>()
                val list2 = mutableListOf<String>()

                for ((i, k) in it.imageUrl.withIndex()) {
                    if (i == 0 || i == 2) {
                        list1.add(k)
                    } else {
                        list2.add(k)
                    }
                }

                val documentImageAdapter2 = DocumentImageAdapter(list2)
                binding.imageRv2.adapter = documentImageAdapter2
                binding.imageRv2.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

                val documentImageAdapter1 = DocumentImageAdapter(list1)
                binding.imageRv1.adapter = documentImageAdapter1
                binding.imageRv1.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            }

            //???????????? ??????
            if (viewModel.document.value!!.isWriter == true) {
                //?????? ?????? ????????? ?????????
                if (viewModel.document.value!!.reportId == null) {

                    if (it.state == 2) {
                        binding.reportWriteButton.isVisible = false
                    } else {
                        binding.reportWriteButton.isVisible = true
                    }

                    binding.reportCheckButton.isVisible = false
                } else {
                    binding.reportWriteButton.isVisible = false
                    binding.reportCheckButton.isVisible = true
                }
            } else {
                //?????? ?????? ????????? ?????????
                if (viewModel.document.value!!.reportId != null) {
                    binding.reportWriteButton.isVisible = false
                    binding.reportCheckButton.isVisible = true
                } else {
                    binding.reportWriteButton.isVisible = false
                    binding.reportCheckButton.isVisible = false
                }
            }

            //????????? ?????? ????????? ??? ??????
            if (viewModel.document.value!!.isVoted == 1) {

                //????????????
                if (viewModel.document.value!!.isWriter == true) {

                    //?????? ????????? ????????? ??????
                    binding.approveArea.isVisible = false
                    binding.writerApprove.isVisible = true
                    binding.approval.isVisible = true
                    binding.approval.setImageResource(R.drawable.document_result_approval)

                } else { //???????????? ?????????

                    if (it.reportMade == true) {
                        binding.approveArea.isVisible = false
                        binding.writerApprove.isVisible = true
                        binding.approval.isVisible = true
                        binding.approval.setImageResource(R.drawable.document_result_approval)
                    } else {
                        binding.approveButtonIcon.setImageResource(R.drawable.document_approval_icon_selected)
                        binding.approveButton.setTextColor(Color.parseColor("#141414"))
                    }
                }

            } else if (viewModel.document.value!!.isVoted == 2) {

                //????????????
                if (viewModel.document.value!!.isWriter == true) {

                    //?????? ????????? ????????? ??????
                    binding.approveArea.isVisible = false
                    binding.writerApprove.isVisible = true
                    binding.approval.isVisible = true
                    binding.approval.setImageResource(R.drawable.document_result_refusal)

                } else {

                    if (it.reportMade == true) {
                        binding.approveArea.isVisible = false
                        binding.writerApprove.isVisible = true
                        binding.approval.isVisible = true
                        binding.approval.setImageResource(R.drawable.document_result_refusal)
                    } else {
                        binding.refuseButtonIcon.setImageResource(R.drawable.document_refusal_icon_selected)
                        binding.refuseButton.setTextColor(Color.parseColor("#141414"))
                    }
                }
            } else if (viewModel.document.value!!.isVoted == 0) {
                if (viewModel.document.value!!.state == 0) {

                    //?????? ????????? ????????? ??????
                    binding.approveArea.isVisible = false
                    binding.writerApprove.isVisible = true
                    binding.approval.isVisible = true
                    binding.approval.setImageResource(R.drawable.document_result_approval)
                } else if (viewModel.document.value!!.state == 1) {

                    //?????? ????????? ????????? ??????
                    binding.approveArea.isVisible = false
                    binding.writerApprove.isVisible = true
                    binding.approval.isVisible = true
                    binding.approval.setImageResource(R.drawable.document_result_refusal)
                }
            }
        }


        //?????? ????????? ?????????
        commentViewModel.comments.observe(this) {

            binding.documentCommentRecyclerview.layoutManager = LinearLayoutManager(this)

            /**????????? ?????????????????? ?????? ???????????? ??????*/
            val documentCommentAdapter = ParentCommentAdapter(it, this, layoutInflater,
                viewModel.document.value!!.isWriter!!, followViewModel, commentViewModel, documentId = viewModel.document.value!!.documentId.toString())
            documentCommentAdapter.notifyDataSetChanged()
            binding.documentCommentRecyclerview.adapter = documentCommentAdapter

            documentCommentAdapter.itemClick = object : ParentCommentAdapter.ItemClick {

                override fun make_chid_comment(v: View, data: CommentDto, pos: Int) {
                    if (data.commentId.toString() == commentViewModel.commentId.value.toString()) {
                        commentViewModel.setParentCommentId(-1)
                        BlackToast.createToast(baseContext, "?????? ????????? ?????????????????????.").show()
                    } else {
                        BlackToast.createToast(baseContext, "????????? ?????????????????????.").show()
                        commentViewModel.setParentCommentId(data.commentId)
                    }
                }

                override fun like(v: View, data: CommentDto, pos: Int) {
                    followViewModel.like(commentId = data.commentId)
                }

                /**?????? ??????????????? ??????*/
                override fun setting_comment(v: View, data: CommentDto, pos: Int, context: Context) {
                    val writer = viewModel.document.value!!.isWriter

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

    //??? ?????? ??????
    private fun move_to_other() {

        binding.cancel.setOnClickListener {
            finish()
        }
    }

    //????????? ????????? ?????? ?????? ??? ?????? ?????? ????????? ?????? ??????
    override fun onStart() {
        super.onStart()

        /**AccessToken ???????????? ????????? ???????????? ????????? ??????*/
        viewModel.checkAccessToken()

        commentViewModel.setParentCommentId(-1)

        val documentId = intent.getStringExtra("documentId")

        viewModel.get_document_detail(documentId.toString())
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        /*
        Log.d("before", binding.row1Content.height.toString())
        Log.d("value", binding.row2Content.height.toString())
        binding.row2Content.measuredHeightAndState
        Log.d("value2", (binding.line2.y - binding.line1.y).toString())
        binding.row1Content.layoutParams = ConstraintLayout.LayoutParams(binding.row1Content.width, binding.row2Content.measuredHeight)
            //binding.row2Content.height + 250)
        setContentView(binding.root)
        Log.d("after", binding.row1Content.height.toString())

         */
        binding.rowBackgroundImg.minimumHeight = binding.documentCommentPost.measuredHeight
        setContentView(binding.root)
    }

    //?????? ??? ?????? Api
    fun changeApproveButton() {

        if (viewModel.document.value!!.isWriter == true) {

            //?????? ????????? ????????? ??????
            binding.approveArea.isVisible = false
            binding.writerApprove.isVisible = true
            binding.approval.isVisible = true
            binding.approval.setImageResource(R.drawable.document_result_approval)

            viewModel.agree_my_document(AgreeMyPostDto(viewModel.document.value!!.documentId!!.toInt(), true))
            binding.reportWriteButton.isVisible = true
            binding.reportCheckButton.isVisible = false

        } else {
            viewModel.setIsVoted(1)
            binding.approveButtonIcon.setImageResource(R.drawable.document_approval_icon_selected)
            binding.approveButton.setTextColor(Color.parseColor("#141414"))
            viewModel.agree_document(viewModel.document.value!!.documentId.toString(), AgreePostDto(true))
        }
    }

    //?????? ??? ?????? Api
    fun changeRefuseButton(){

        if (viewModel.document.value!!.isWriter == true) {

            //?????? ????????? ????????? ??????
            binding.approveArea.isVisible = false
            binding.writerApprove.isVisible = true
            binding.approval.isVisible = true
            binding.approval.setImageResource(R.drawable.document_result_refusal)

            viewModel.agree_my_document(AgreeMyPostDto(viewModel.document.value!!.documentId!!.toInt(), false))
            binding.reportWriteButton.isVisible = true
            binding.reportCheckButton.isVisible = false

        } else {
            viewModel.setIsVoted(2)
            binding.refuseButtonIcon.setImageResource(R.drawable.document_refusal_icon_selected)
            binding.refuseButton.setTextColor(Color.parseColor("#141414"))
            viewModel.agree_document(viewModel.document.value!!.documentId.toString(), AgreePostDto(false))
        }
    }

    /* ???????????? ?????? ???????????? ?????? */
    private fun setRank(rankInt : Int?) : String?{
        var rank : String? = null
        when(rankInt){
            0->{ rank = "??????" }
            1->{ rank = "??????" }
            2->{ rank = "??????" }
            3->{ rank = "??????" }
            4->{ rank = "??????" }
            5->{ rank = "??????" }
        }
        return rank
    }

    //???????????????
    //??????????????? ??????
    private fun post_more() {

        binding.uploadSettingBtn.setOnClickListener {

            val writer = viewModel.document.value!!.isWriter
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

            //??????????????? ????????? ??????
            val setting_notice_on_text = bottomSheetView.findViewById<TextView>(R.id.notice_on_text)
            setting_notice_on_text.setText("??? ???????????? ?????? ??????")
            val setting_notice_off_text = bottomSheetView.findViewById<TextView>(R.id.notice_off_text)
            setting_notice_off_text.setText("??? ???????????? ?????? ??????")
            val setting_storage_on_text = bottomSheetView.findViewById<TextView>(R.id.storage_on_text)
            setting_storage_on_text.setText("??? ???????????? ???????????? ??????")
            val setting_storage_off_text = bottomSheetView.findViewById<TextView>(R.id.storage_off_text)
            setting_storage_off_text.setText("??? ???????????? ??????????????? ??????")
            val setting_report_post_text = bottomSheetView.findViewById<TextView>(R.id.report_report_text)
            setting_report_post_text.setText("??? ???????????? ????????????")
            val setting_remove_post_text = bottomSheetView.findViewById<TextView>(R.id.remove_post_text)
            setting_remove_post_text.setText("??? ???????????? ????????????")

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
                followViewModel.notification(documentId = viewModel.document.value!!.documentId)
                bottomSheetDialog.cancel()
                BlackToast.createToast(this, "??? ??????????????? ????????? ?????????.").show()
            }

            setting_notice_off!!.setOnClickListener {
                followViewModel.notification(documentId = viewModel.document.value!!.documentId)
                bottomSheetDialog.cancel()
                BlackToast.createToast(this, "??? ??????????????? ????????? ??? ?????? ?????? ?????????.").show()
            }

            setting_storage_on!!.setOnClickListener {
                followViewModel.scrap(documentId = viewModel.document.value!!.documentId)
                bottomSheetDialog.cancel()
                BlackToast.createToast(this, "??? ??????????????? ???????????? ????????????.").show()
            }

            setting_storage_off!!.setOnClickListener {
                followViewModel.scrap(documentId = viewModel.document.value!!.documentId)
                bottomSheetDialog.cancel()
                BlackToast.createToast(this, "??? ??????????????? ??????????????? ?????????.").show()
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

    //??? ?????? ???????????????
    private fun showRemovePostDialog(){
        val linkDialog : Dialog = Dialog(this);
        activityCommunityRemovePostDialogBinding = ActivityCommunityRemovePostDialogBinding.inflate(layoutInflater)

        linkDialog.setContentView(activityCommunityRemovePostDialogBinding.root)
        linkDialog.setCanceledOnTouchOutside(true)
        linkDialog.setCancelable(true)
        dialogCancelButton = activityCommunityRemovePostDialogBinding.communityDialogCancelButton
        dialogConfirmButton = activityCommunityRemovePostDialogBinding.communityDialogConfirmButton

        val text = activityCommunityRemovePostDialogBinding.communityDialog
        text.setText("???????????? ????????? ??????,\n??????????????? ?????? ???????????????.\n\n??? ??????????????? ?????????????????????????\n")

        dialogConfirmButton.setText("???????????? ????????????")

        /*????????????*/
        dialogCancelButton.setOnClickListener {
            linkDialog.dismiss()
        }

        /*????????????*/
        dialogConfirmButton.setOnClickListener{
            linkDialog.dismiss()
            viewModel.delete_document(viewModel.document.value!!.documentId.toString())
            Handler(Looper.myLooper()!!).postDelayed({
                finish()
            }, 500)
        }
        /*link ??????*/
        linkDialog.show()
    }

    //????????? ??????
    private fun showReportUserDialog(){
        val linkDialog : Dialog = Dialog(this);
        activityCommunityReportUserDialogBinding = ActivityCommunityReportUserDialogBinding.inflate(layoutInflater)

        linkDialog.setContentView(activityCommunityReportUserDialogBinding.root)
        linkDialog.setCanceledOnTouchOutside(true)
        linkDialog.setCancelable(true)
        dialogCancelButton = activityCommunityReportUserDialogBinding.communityDialogCancelButton
        dialogConfirmButton = activityCommunityReportUserDialogBinding.communityDialogConfirmButton

        val text = activityCommunityReportUserDialogBinding.communityDialog
        text.setText("??? ???????????? ?????????????????????????")

        /*????????????*/
        dialogCancelButton.setOnClickListener {
            linkDialog.dismiss()
        }

        /*????????????*/
        dialogConfirmButton.setOnClickListener{
            followViewModel.accuse(accuseUserId = viewModel.document.value!!.userId)
            linkDialog.dismiss()
            BlackToast.createToast(this, "????????? ?????????????????????.").show()
        }

        /*link ??????*/
        linkDialog.show()
    }

    //????????? ??????
    private fun showReportPostDialog(){
        val linkDialog : Dialog = Dialog(this);
        activityCommunityReportPostDialogBinding = ActivityCommunityReportPostDialogBinding.inflate(layoutInflater)

        linkDialog.setContentView(activityCommunityReportPostDialogBinding.root)
        linkDialog.setCanceledOnTouchOutside(true)
        linkDialog.setCancelable(true)
        val text = activityCommunityReportPostDialogBinding.communityDialog
        text.setText("??? ??????????????? ?????????????????????????")

        dialogCancelButton = activityCommunityReportPostDialogBinding.communityDialogCancelButton
        dialogConfirmButton = activityCommunityReportPostDialogBinding.communityDialogConfirmButton

        dialogConfirmButton.setText("???????????? ????????????")

        /*????????????*/
        dialogCancelButton.setOnClickListener {
            linkDialog.dismiss()
        }

        /*????????????*/
        dialogConfirmButton.setOnClickListener{
            followViewModel.accuse(documentId = viewModel.document.value!!.documentId)
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
                documentId = viewModel.document.value!!.documentId.toString())
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