package com.umc.approval.ui.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.umc.approval.R
import com.umc.approval.check.collie.OtherpageActivity
import com.umc.approval.data.dto.comment.get.CommentDto
import com.umc.approval.data.dto.comment.post.CommentPostDto
import com.umc.approval.data.dto.communitydetail.get.VoteOption
import com.umc.approval.data.dto.communitydetail.post.CommunityVoteResult
import com.umc.approval.data.dto.follow.FollowStateDto
import com.umc.approval.data.dto.follow.NotificationStateDto
import com.umc.approval.data.dto.follow.ScrapStateDto
import com.umc.approval.databinding.ActivityCommunityRemovePostDialogBinding
import com.umc.approval.databinding.ActivityCommunityReportPostDialogBinding
import com.umc.approval.databinding.ActivityCommunityReportUserDialogBinding
import com.umc.approval.databinding.ActivityCommunityTokBinding
import com.umc.approval.ui.adapter.community_post_activity.CommunityImageRVAdapter
import com.umc.approval.ui.adapter.community_post_activity.CommunityVoteCompleteRVAdapter
import com.umc.approval.ui.adapter.community_post_activity.CommunityVoteRVAdapter
import com.umc.approval.ui.adapter.community_upload_activity.CommunityUploadLinkItemRVAdapter
import com.umc.approval.ui.adapter.document_comment_activity.ParentCommentAdapter
import com.umc.approval.ui.adapter.upload_activity.UploadHashtagRVAdapter
import com.umc.approval.ui.viewmodel.comment.CommentViewModel
import com.umc.approval.ui.viewmodel.communityDetail.TokViewModel
import com.umc.approval.ui.viewmodel.follow.FollowViewModel
import com.umc.approval.util.BlackToast
import com.umc.approval.util.Utils
import com.umc.approval.util.Utils.categoryMap
import com.umc.approval.util.Utils.level
import com.umc.approval.util.Utils.levelImage


class CommunityTokActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCommunityTokBinding

    //viewModel
    private val viewModel by viewModels<TokViewModel>()

    val commentViewModel by viewModels<CommentViewModel>()

    //?????????
    private lateinit var dataCompleteRVAdapter: CommunityVoteCompleteRVAdapter
    private lateinit var dataRVAdapter: CommunityVoteCompleteRVAdapter

    //?????? ??????
    private var sendVote = mutableListOf <Int>()

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

        binding = ActivityCommunityTokBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.communityVoteLayout.communityVoteLayout.isVisible = false
        binding.communityVoteLayoutComplete.communityVoteCompleteLayout.isVisible = false

        //?????? ?????? ??? ?????? ??????
        binding.writeButton.setOnClickListener {
            if (viewModel.accessToken.value != false) {
                val postComment = CommentPostDto(toktokId = viewModel.tok.value!!.toktokId,
                    content = binding.communityCommentEt.text.toString(), parentCommentId = null)

                if (commentViewModel.commentId.value != -1) {
                    postComment.parentCommentId = commentViewModel.commentId.value
                }

                commentViewModel.post_comments(postComment, toktokId = viewModel.tok.value!!.toktokId.toString())
                binding.communityCommentEt.text.clear()
                commentViewModel.setParentCommentId(-1)
            } else {
                BlackToast.createToast(this, "????????? ????????? ???????????????").show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        live_data()

        /*setting*/
        post_more()

        //?????????
        binding.postLikeState.setOnClickListener {

            if (viewModel.accessToken.value == false) {
                BlackToast.createToast(this, "????????? ????????? ???????????????").show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                followViewModel.like(toktokId = viewModel.tok.value!!.toktokId)
            }
        }

        //follow??????
        binding.follow.setOnClickListener {
            followViewModel.follow(viewModel.tok.value!!.userId)
        }

        //unfollow??????
        binding.unfollow.setOnClickListener {
            followViewModel.follow(viewModel.tok.value!!.userId)
        }

        /*close*/
        binding.uploadCancelBtn.setOnClickListener{
            finish()
        }

        //?????? ????????? ??????
        binding.communityPostUserName.setOnClickListener {
            if (viewModel.tok.value!!.writerOrNot == false) {
                val intent = Intent(this, OtherpageActivity::class.java)
                intent.putExtra("userId", viewModel.tok.value!!.userId)
                startActivity(intent)
            }
        }

        // ????????? ?????? ?????? ??????
        binding.communityPostLikeNum.setOnClickListener {
            // ?????? ?????? ID??? ??????
            val intent = Intent(this, LikeActivity::class.java)
            intent.putExtra("type", "toktok")
            intent.putExtra("id", viewModel.tok.value!!.toktokId)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.checkAccessToken()

        val toktokId = intent.getStringExtra("toktokId")

        viewModel.get_tok_detail(toktokId.toString())
    }

//    override fun onRestart() {
//        super.onRestart()
//        val toktokId = intent.getStringExtra("toktokId")
//        viewModel.get_tok_detail(toktokId.toString())
//    }

    override fun onResume() {
        super.onResume()
        val toktokId = intent.getStringExtra("toktokId")
        viewModel.get_tok_detail(toktokId.toString())
    }


    //??????????????? ??????
    private fun post_more() {

        binding.uploadSettingBtn.setOnClickListener {

            val writer = viewModel.tok.value!!.writerOrNot
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
                followViewModel.notification(toktokId = viewModel.tok.value!!.toktokId)
                bottomSheetDialog.cancel()
                BlackToast.createToast(this, "??? ??????????????? ????????? ?????????.").show()
            }

            setting_notice_off!!.setOnClickListener {
                followViewModel.notification(toktokId = viewModel.tok.value!!.toktokId)
                bottomSheetDialog.cancel()
                BlackToast.createToast(this, "??? ??????????????? ????????? ??? ?????? ?????? ?????????.").show()
            }

            setting_storage_on!!.setOnClickListener {
                followViewModel.scrap(toktokId = viewModel.tok.value!!.toktokId)
                bottomSheetDialog.cancel()
                BlackToast.createToast(this, "??? ??????????????? ???????????? ????????????.").show()
            }

            setting_storage_off!!.setOnClickListener {
                followViewModel.scrap(toktokId = viewModel.tok.value!!.toktokId)
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
        text.setText("??? ??????????????? ?????????????????????????")

        dialogConfirmButton.setText("???????????? ????????????")

        /*????????????*/
        dialogCancelButton.setOnClickListener {
            linkDialog.dismiss()
        }

        /*????????????*/
        dialogConfirmButton.setOnClickListener{
            linkDialog.dismiss()
            viewModel.delete_tok(viewModel.tok.value!!.toktokId.toString())
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

        /*????????????*/
        dialogCancelButton.setOnClickListener {
            linkDialog.dismiss()
        }

        /*????????????*/
        dialogConfirmButton.setOnClickListener{
            followViewModel.accuse(accuseUserId = viewModel.tok.value!!.userId)
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
        dialogCancelButton = activityCommunityReportPostDialogBinding.communityDialogCancelButton
        dialogConfirmButton = activityCommunityReportPostDialogBinding.communityDialogConfirmButton

        val text = activityCommunityReportPostDialogBinding.communityDialog
        text.setText("??? ??????????????? ?????????????????????????")

        dialogConfirmButton.setText("???????????? ????????????")

        /*????????????*/
        dialogCancelButton.setOnClickListener {
            linkDialog.dismiss()
        }

        /*????????????*/
        dialogConfirmButton.setOnClickListener{
            followViewModel.accuse(toktokId = viewModel.tok.value!!.toktokId)
            linkDialog.dismiss()
            BlackToast.createToast(this, "????????? ?????????????????????.").show()
        }

        /*link ??????*/
        linkDialog.show()
    }

    private fun live_data(){

        //?????? ????????? ??????
        followViewModel.commentLike.observe(this) {
            commentViewModel.get_comments(toktokId = viewModel.tok.value!!.toktokId.toString())
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

        //??? ??????
        viewModel.tok.observe(this) {

            commentViewModel.get_comments(toktokId = it.toktokId.toString())

            //????????????
            binding.communityPostLikeNum.text = "????????? "+ it.likedCount.toString()
            //????????????
            binding.communityPostScrapNum.text = "????????? "+ it.scrapCount.toString()
            //????????????
            binding.communityPostVisitorsNum.text = "????????? "+ it.view.toString()
            //?????????
            binding.communityPostUserName.text = it.nickname
            //??????
            binding.rank.text = level[it.level]
            //????????????
            binding.communityPostCategory.text = categoryMap[it.category]
            //??????
            binding.communityPostTime.text = it.datetime
            //content
            binding.communityPostContent.text = it.content
            //?????? ???
            binding.commentNum.text = it.commentCount.toString()

            //????????? ?????????
            if (it.profileImage != null) {
                binding.communityPostUserProfile.load(it.profileImage)
                binding.communityPostUserProfile.clipToOutline = true
            } else {
                binding.communityPostUserProfile.load(levelImage[it.level])
                binding.communityPostUserProfile.clipToOutline = true
            }


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

            //???????????? ???????????? ??????
            if (it.images!!.isNotEmpty()) {
                var imageRVAdapter = CommunityImageRVAdapter(it.images!!)
                binding.imageRv.adapter = imageRVAdapter
                binding.imageRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            } else {
                binding.imageRv.isVisible = false
            }

            //????????? ???????????? ??????
            if (it.link!!.isNotEmpty()) {
                val dataRVAdapter = CommunityUploadLinkItemRVAdapter(it.link!!)
                binding.uploadLinkItem.adapter = dataRVAdapter
                binding.uploadLinkItem.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
            } else {
                binding.uploadLinkItem.isVisible = false
            }

            //????????? ???????????? ??????
            if (it.tag!!.isNotEmpty()) {
                val dataRVAdapter = UploadHashtagRVAdapter(it.tag!!)
                binding.uploadHashtagItem.adapter = dataRVAdapter
                binding.uploadHashtagItem.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
            } else {
                binding.uploadHashtagItem.isVisible = false
            }

            //????????? ?????? ??????
            if (it.voteId != null) {

                //????????? ???????????? ??????
                if (it.voteIsEnd == true) {
                    viewModel.setIsEnd(true)
                } else {
                    viewModel.setIsEnd(false)
                }

                //????????? ?????????????????? ??????
                if(it.voteIsSingle == true){
                    binding.communityVoteLayoutComplete.communityPostVoteOption.text = " ?? ????????????"
                    binding.communityVoteLayout.communityPostVoteOption.text = " ?? ????????????"

                }else{
                    binding.communityVoteLayoutComplete.communityPostVoteOption.text = " ?? ????????????"
                    binding.communityVoteLayout.communityPostVoteOption.text = " ?? ????????????"
                }

                //?????? ????????? ?????????
                viewModel.initVoteList()

                //?????? ??????
                binding.communityVoteLayoutComplete.communityPostVoteTitle.text = it.voteTitle
                binding.communityVoteLayout.communityPostVoteTitle.text = it.voteTitle

                // ?????? ??????
                binding.communityVoteLayoutComplete.communityPostVoteParticipant.text = it.votePeople.toString() + "?????????"
                binding.communityVoteLayout.communityPostVoteParticipant.text = it.votePeople.toString() + "?????????"

                viewModel.votedList.observe(this) {
                    Log.d("??????????????????", it.toString())
                    if (it.isNotEmpty()) {
                        sendVote = viewModel.votedList.value!!
                    } else {
                        viewModel.setVoted(false)
                    }
                }

                //??? ????????? ????????? ???
                viewModel.setVotePeopleEachOption(CommunityVoteResult(it.votePeopleEachOption!!))
            }
        }

        //??? ???????????? ?????? ????????? ?????? ??????
        viewModel.votePeopleEachOption.observe(this) {
            viewModel.setReVote(false)
        }

        //???????????? ??? ??????
        viewModel.reVote.observe(this) {

            if (it == true) { //???????????? ??? ??????
                binding.communityVoteLayoutComplete.communityPostVoteState.text = "???????????????"
                binding.communityVoteLayout.communityVoteLayout.isVisible = false
                binding.communityVoteLayoutComplete.communityVoteCompleteLayout.isVisible = true
                binding.communityVoteLayoutComplete.revoteButton.isVisible = false
                binding.communityVoteLayoutComplete.closeVoteButton.isVisible = false
                binding.communityVoteLayoutComplete.voteButton.isVisible = true
                binding.communityVoteLayoutComplete.voteButton.setOnClickListener {
                    viewModel.post_vote(sendVote, viewModel.tok.value!!.voteId.toString())
                }

                //????????? ??????
                dataCompleteRVAdapter =
                    CommunityVoteCompleteRVAdapter(
                        viewModel.tok.value!!.voteOptions!!,
                        viewModel.tok.value!!.votePeople!!.toFloat(),
                        viewModel.votePeopleEachOption.value!!.votePeoepleEachOption!!,
                        sendVote,
                        viewModel.reVote.value!!
                    )

                binding.communityVoteLayoutComplete.voteItem.adapter = dataCompleteRVAdapter
                binding.communityVoteLayoutComplete.voteItem.layoutManager =
                    LinearLayoutManager(this, RecyclerView.VERTICAL, false)

                dataCompleteRVAdapter?.setOnItemClickListener(object :
                    CommunityVoteCompleteRVAdapter.OnItemClickListner {
                    override fun onItemClick(v: View, data: VoteOption, pos: Int) {
                        val voteIntent = Intent(
                            this@CommunityTokActivity,
                            CommunityTokVoteParticipant::class.java
                        ) // ???????????? ??????
                        voteIntent.putExtra("voteId", data.voteOptionId)
                        voteIntent.putExtra("title",data.opt)

                        startActivity(voteIntent)
                    }

                    override fun voteClick(
                        v: View,
                        data: Int,
                        pos: Int,
                        voteItemCheck: AppCompatCheckBox
                    ) {
                        if (data in sendVote) {
                            sendVote.remove(data)
                        } else {
                            //?????? ????????? ??????
                            if (viewModel.tok.value!!.voteIsSingle!! == false) {
                                sendVote.add(data)
                            } else { //?????? ????????? ??????
                                if (sendVote.isEmpty()) {
                                    sendVote.add(data)
                                } else {
                                    BlackToast.createToast(baseContext, "?????? ????????? ??????????????????").show()
                                    voteItemCheck.isChecked = false
                                }
                            }
                        }
                    }
                })
            } else {

                if (viewModel.isEnd.value == true) { // ?????? ?????? ???
                    binding.communityVoteLayoutComplete.communityPostVoteState.text = "?????? ??????"
                    binding.communityVoteLayout.communityVoteLayout.isVisible = false
                    binding.communityVoteLayoutComplete.communityVoteCompleteLayout.isVisible = true
                    binding.communityVoteLayoutComplete.closeVoteButton.isVisible = false
                    binding.communityVoteLayoutComplete.revoteButton.isVisible = false
                    binding.communityVoteLayoutComplete.voteButton.isVisible = false

                    //????????? ??????
                    dataCompleteRVAdapter =
                        CommunityVoteCompleteRVAdapter(
                            viewModel.tok.value!!.voteOptions!!,
                            viewModel.tok.value!!.votePeople!!.toFloat(),
                            viewModel.votePeopleEachOption.value!!.votePeoepleEachOption!!,
                            sendVote,
                            viewModel.reVote.value!!
                        )

                    binding.communityVoteLayoutComplete.voteItem.adapter = dataCompleteRVAdapter
                    binding.communityVoteLayoutComplete.voteItem.layoutManager =
                        LinearLayoutManager(this, RecyclerView.VERTICAL, false)

                    dataCompleteRVAdapter?.setOnItemClickListener(object :
                        CommunityVoteCompleteRVAdapter.OnItemClickListner {
                        override fun onItemClick(v: View, data: VoteOption, pos: Int) {
                            val voteIntent = Intent(
                                this@CommunityTokActivity,
                                CommunityTokVoteParticipant::class.java
                            ) // ???????????? ??????
                            voteIntent.putExtra("voteId", data.voteOptionId)
                            voteIntent.putExtra("title",data.opt)

                            startActivity(voteIntent)
                        }

                        override fun voteClick(
                            v: View,
                            data: Int,
                            pos: Int,
                            voteItemCheck: AppCompatCheckBox
                        ) {
                        }
                    })

                } else if (viewModel.tok.value!!.writerOrNot == true) { // ????????? ??????
                    binding.communityVoteLayoutComplete.communityPostVoteState.text = "???????????????"
                    binding.communityVoteLayout.communityVoteLayout.isVisible = false
                    binding.communityVoteLayoutComplete.communityVoteCompleteLayout.isVisible = true
                    binding.communityVoteLayoutComplete.closeVoteButton.isVisible = true
                    binding.communityVoteLayoutComplete.closeVoteButton.setOnClickListener {
                        viewModel.setIsEnd(true)
                        viewModel.setReVote(false)
                        viewModel.end_vote(viewModel.tok.value!!.voteId.toString())
                    }
                    binding.communityVoteLayoutComplete.revoteButton.isVisible = false
                    binding.communityVoteLayoutComplete.voteButton.isVisible = false

                    //????????? ??????
                    dataCompleteRVAdapter =
                        CommunityVoteCompleteRVAdapter(
                            viewModel.tok.value!!.voteOptions!!,
                            viewModel.tok.value!!.votePeople!!.toFloat(),
                            viewModel.votePeopleEachOption.value!!.votePeoepleEachOption!!,
                            sendVote,
                            viewModel.reVote.value!!
                        )

                    binding.communityVoteLayoutComplete.voteItem.adapter = dataCompleteRVAdapter
                    binding.communityVoteLayoutComplete.voteItem.layoutManager =
                        LinearLayoutManager(this, RecyclerView.VERTICAL, false)

                    dataCompleteRVAdapter?.setOnItemClickListener(object :
                        CommunityVoteCompleteRVAdapter.OnItemClickListner {
                        override fun onItemClick(v: View, data: VoteOption, pos: Int) {
                            val voteIntent = Intent(
                                this@CommunityTokActivity,
                                CommunityTokVoteParticipant::class.java
                            ) // ???????????? ??????
                            voteIntent.putExtra("voteId", data.voteOptionId)
                            voteIntent.putExtra("title",data.opt)

                            startActivity(voteIntent)
                        }
                        override fun voteClick(
                            v: View,
                            data: Int,
                            pos: Int,
                            voteItemCheck: AppCompatCheckBox
                        ) {
                        }
                    })
                } else if (viewModel.isVote.value == false) { // ?????? ????????? ???
                    binding.communityVoteLayoutComplete.communityPostVoteState.text = "???????????????"
                    binding.communityVoteLayout.communityVoteLayout.isVisible = true
                    binding.communityVoteLayoutComplete.communityVoteCompleteLayout.isVisible = false

                    val dataRVAdapter = CommunityVoteRVAdapter(viewModel.tok.value!!.voteOptions!!, sendVote)
                    binding.communityVoteLayout.voteItem.adapter = dataRVAdapter
                    binding.communityVoteLayout.voteItem.layoutManager =
                        LinearLayoutManager(this, RecyclerView.VERTICAL, false)
                    binding.communityVoteLayout.voteButton.setOnClickListener {
                        viewModel.setVoted(true)
                        viewModel.post_vote(sendVote, viewModel.tok.value!!.voteId.toString())
                    }
                    dataRVAdapter?.setOnItemClickListener(object :
                        CommunityVoteRVAdapter.OnItemClickListner {
                        override fun onItemClick(v: View, data: VoteOption, pos: Int) {
                        }
                        override fun voteClick(
                            v: View,
                            data: Int,
                            pos: Int,
                            voteItemCheck: AppCompatCheckBox
                        ) {
                            if (data in sendVote) {
                                sendVote.remove(data)
                            } else {
                                //?????? ????????? ??????
                                if (viewModel.tok.value!!.voteIsSingle!! == false) {
                                    sendVote.add(data)
                                } else { //?????? ????????? ??????
                                    if (sendVote.isEmpty()) {
                                        sendVote.add(data)
                                    } else {
                                        BlackToast.createToast(baseContext, "?????? ????????? ??????????????????").show()
                                        voteItemCheck.isChecked = false
                                    }
                                }
                            }
                        }
                    })

                } else { // ?????? ?????? ??? ??????
                    binding.communityVoteLayoutComplete.communityPostVoteState.text = "???????????????"
                    binding.communityVoteLayout.communityVoteLayout.isVisible = false
                    binding.communityVoteLayoutComplete.communityVoteCompleteLayout.isVisible = true
                    binding.communityVoteLayoutComplete.revoteButton.isVisible = true
                    binding.communityVoteLayoutComplete.closeVoteButton.isVisible = false
                    binding.communityVoteLayoutComplete.voteButton.isVisible = false
                    binding.communityVoteLayoutComplete.revoteButton.setOnClickListener {
                        viewModel.setReVote(true)
                    }

                    //????????? ??????
                    dataCompleteRVAdapter =
                        CommunityVoteCompleteRVAdapter(
                            viewModel.tok.value!!.voteOptions!!,
                            viewModel.tok.value!!.votePeople!!.toFloat(),
                            viewModel.votePeopleEachOption.value!!.votePeoepleEachOption!!,
                            sendVote,
                            viewModel.reVote.value!!
                        )

                    binding.communityVoteLayoutComplete.voteItem.adapter = dataCompleteRVAdapter
                    binding.communityVoteLayoutComplete.voteItem.layoutManager =
                        LinearLayoutManager(this, RecyclerView.VERTICAL, false)

                    dataCompleteRVAdapter?.setOnItemClickListener(object :
                        CommunityVoteCompleteRVAdapter.OnItemClickListner {
                        override fun onItemClick(v: View, data: VoteOption, pos: Int) {
                            val voteIntent = Intent(
                                this@CommunityTokActivity,
                                CommunityTokVoteParticipant::class.java
                            ) // ???????????? ??????
                            voteIntent.putExtra("voteId", data.voteOptionId)
                            voteIntent.putExtra("title",data.opt)
                            startActivity(voteIntent)
                        }

                        override fun voteClick(
                            v: View,
                            data: Int,
                            pos: Int,
                            voteItemCheck: AppCompatCheckBox
                        ) {
                        }
                    })
                }
            }
        }

        //?????? ????????? ?????????
        commentViewModel.comments.observe(this) {

            binding.commentItem.layoutManager = LinearLayoutManager(this)
            /**????????? ?????????????????? ?????? ???????????? ??????*/
            val documentCommentAdapter = ParentCommentAdapter(it, this, layoutInflater,
                viewModel.tok.value!!.writerOrNot!!,
                followViewModel, commentViewModel, toktokId = viewModel.tok.value!!.toktokId.toString()
            )
            documentCommentAdapter.notifyDataSetChanged()
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
                        commentViewModel.setParentCommentId(data.commentId)
                        BlackToast.createToast(baseContext, "????????? ?????????????????????").show()
                    }
                }

                /**?????? ??????????????? ??????*/
                override fun setting_comment(v: View, data: CommentDto, pos: Int, context: Context) {
                    val writer = viewModel.tok.value!!.writerOrNot

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
                toktokId = viewModel.tok.value!!.toktokId.toString())
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
        }

        /*link ??????*/
        linkDialog.show()
    }
}