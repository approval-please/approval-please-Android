package com.umc.approval.ui.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    //어댑터
    private lateinit var dataCompleteRVAdapter: CommunityVoteCompleteRVAdapter
    private lateinit var dataRVAdapter: CommunityVoteCompleteRVAdapter

    //보낼 투표
    private var sendVote = mutableListOf <Int>()

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

        binding = ActivityCommunityTokBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.communityVoteLayout.communityVoteLayout.isVisible = false
        binding.communityVoteLayoutComplete.communityVoteCompleteLayout.isVisible = false

        //작성 누를 시 댓글 작성
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
                BlackToast.createToast(this, "로그인 과정이 필요합니다").show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        live_data()

        /*setting*/
        post_more()

        //좋아요
        binding.postLikeState.setOnClickListener {

            if (viewModel.accessToken.value == false) {
                BlackToast.createToast(this, "로그인 과정이 필요합니다").show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                followViewModel.like(toktokId = viewModel.tok.value!!.toktokId)
            }
        }

        //follow하면
        binding.follow.setOnClickListener {
            followViewModel.follow(viewModel.tok.value!!.userId)
        }

        //unfollow하면
        binding.unfollow.setOnClickListener {
            followViewModel.follow(viewModel.tok.value!!.userId)
        }

        /*close*/
        binding.uploadCancelBtn.setOnClickListener{
            finish()
        }

        //이름 누를시 이동
        binding.communityPostUserName.setOnClickListener {
            if (viewModel.tok.value!!.writerOrNot == false) {
                val intent = Intent(this, OtherpageActivity::class.java)
                intent.putExtra("userId", viewModel.tok.value!!.userId)
                startActivity(intent)
            }
        }

        // 좋아요 누른 유저 확인
        binding.communityPostLikeBtn.setOnClickListener {
            // 결재 톡톡 ID를 넘김
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


    //다이얼로그 로직
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

            //dialog의 view Component 접근
            val setting_notice_on = bottomSheetView.findViewById<LinearLayout>(R.id.setting_notice_on)
            val setting_notice_off = bottomSheetView.findViewById<LinearLayout>(R.id.setting_notice_off)
            val setting_storage_on = bottomSheetView.findViewById<LinearLayout>(R.id.setting_storage_on)
            val setting_storage_off = bottomSheetView.findViewById<LinearLayout>(R.id.setting_storage_off)
            val setting_report_post = bottomSheetView.findViewById<LinearLayout>(R.id.setting_report_post)
            val setting_report_user = bottomSheetView.findViewById<LinearLayout>(R.id.setting_report_user)
            val setting_remove_post = bottomSheetView.findViewById<LinearLayout>(R.id.setting_remove_post)

            //다이얼로그 텍스트 수정
            val setting_notice_on_text = bottomSheetView.findViewById<TextView>(R.id.notice_on_text)
            setting_notice_on_text.setText("이 결재톡톡 알람 받기")
            val setting_notice_off_text = bottomSheetView.findViewById<TextView>(R.id.notice_off_text)
            setting_notice_off_text.setText("이 결재톡톡 알람 끄기")
            val setting_storage_on_text = bottomSheetView.findViewById<TextView>(R.id.storage_on_text)
            setting_storage_on_text.setText("이 결재톡톡 보관함에 넣기")
            val setting_storage_off_text = bottomSheetView.findViewById<TextView>(R.id.storage_off_text)
            setting_storage_off_text.setText("이 결재톡톡 보관함에서 빼기")
            val setting_report_post_text = bottomSheetView.findViewById<TextView>(R.id.report_report_text)
            setting_report_post_text.setText("이 결재톡톡 신고하기")
            val setting_remove_post_text = bottomSheetView.findViewById<TextView>(R.id.remove_post_text)
            setting_remove_post_text.setText("이 결재톡톡 삭제하기")

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
                followViewModel.notification(toktokId = viewModel.tok.value!!.toktokId)
                bottomSheetDialog.cancel()
            }

            setting_notice_off!!.setOnClickListener {
                followViewModel.notification(toktokId = viewModel.tok.value!!.toktokId)
                bottomSheetDialog.cancel()
            }

            setting_storage_on!!.setOnClickListener {
                followViewModel.scrap(toktokId = viewModel.tok.value!!.toktokId)
                bottomSheetDialog.cancel()
            }

            setting_storage_off!!.setOnClickListener {
                followViewModel.scrap(toktokId = viewModel.tok.value!!.toktokId)
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

    //톡 삭제 다이얼로그
    private fun showRemovePostDialog(){
        val linkDialog : Dialog = Dialog(this);
        activityCommunityRemovePostDialogBinding = ActivityCommunityRemovePostDialogBinding.inflate(layoutInflater)

        linkDialog.setContentView(activityCommunityRemovePostDialogBinding.root)
        linkDialog.setCanceledOnTouchOutside(true)
        linkDialog.setCancelable(true)
        dialogCancelButton = activityCommunityRemovePostDialogBinding.communityDialogCancelButton
        dialogConfirmButton = activityCommunityRemovePostDialogBinding.communityDialogConfirmButton

        val text = activityCommunityRemovePostDialogBinding.communityDialog
        text.setText("이 결재톡톡을 삭제하시겠습니까?")

        dialogConfirmButton.setText("결재톡톡 삭제하기")

        /*취소버튼*/
        dialogCancelButton.setOnClickListener {
            linkDialog.dismiss()
        }

        /*확인버튼*/
        dialogConfirmButton.setOnClickListener{
            linkDialog.dismiss()
            viewModel.delete_tok(viewModel.tok.value!!.toktokId.toString())
            finish()
        }
        /*link 팝업*/
        linkDialog.show()
    }

    //사용자 신고
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
            followViewModel.accuse(accuseUserId = viewModel.tok.value!!.userId)
            linkDialog.dismiss()
        }

        /*link 팝업*/
        linkDialog.show()
    }

    //게시글 신고
    private fun showReportPostDialog(){
        val linkDialog : Dialog = Dialog(this);
        activityCommunityReportPostDialogBinding = ActivityCommunityReportPostDialogBinding.inflate(layoutInflater)

        linkDialog.setContentView(activityCommunityReportPostDialogBinding.root)
        linkDialog.setCanceledOnTouchOutside(true)
        linkDialog.setCancelable(true)
        dialogCancelButton = activityCommunityReportPostDialogBinding.communityDialogCancelButton
        dialogConfirmButton = activityCommunityReportPostDialogBinding.communityDialogConfirmButton

        val text = activityCommunityReportPostDialogBinding.communityDialog
        text.setText("이 결재톡톡을 신고하시겠습니까?")

        dialogConfirmButton.setText("결재톡톡 신고하기")

        /*취소버튼*/
        dialogCancelButton.setOnClickListener {
            linkDialog.dismiss()
        }

        /*확인버튼*/
        dialogConfirmButton.setOnClickListener{
            followViewModel.accuse(toktokId = viewModel.tok.value!!.toktokId)
            linkDialog.dismiss()
        }

        /*link 팝업*/
        linkDialog.show()
    }

    private fun live_data(){

        //댓글 좋아요 로직
        followViewModel.commentLike.observe(this) {
            commentViewModel.get_comments(toktokId = viewModel.tok.value!!.toktokId.toString())
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

        //톡 로직
        viewModel.tok.observe(this) {

            commentViewModel.get_comments(toktokId = it.toktokId.toString())

            //좋아요수
            binding.communityPostLikeNum.text = "좋아요 "+ it.likedCount.toString()
            //스크랩수
            binding.communityPostScrapNum.text = "스크랩 "+ it.scrapCount.toString()
            //방문자수
            binding.communityPostVisitorsNum.text = "조회수 "+ it.view.toString()
            //닉네임
            binding.communityPostUserName.text = it.nickname
            //직급
            binding.rank.text = level[it.level]
            //카테고리
            binding.communityPostCategory.text = categoryMap[it.category]
            //시간
            binding.communityPostTime.text = it.datetime
            //content
            binding.communityPostContent.text = it.content
            //댓글 수
            binding.commentNum.text = it.commentCount.toString()

            //프로필 이미지
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

            //이미지가 비어있는 경우
            if (it.images!!.isNotEmpty()) {
                var imageRVAdapter = CommunityImageRVAdapter(it.images!!)
                binding.imageRv.adapter = imageRVAdapter
                binding.imageRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            } else {
                binding.imageRv.isVisible = false
            }

            //링크가 비어있는 경우
            if (it.link!!.isNotEmpty()) {
                val dataRVAdapter = CommunityUploadLinkItemRVAdapter(it.link!!)
                binding.uploadLinkItem.adapter = dataRVAdapter
                binding.uploadLinkItem.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
            } else {
                binding.uploadLinkItem.isVisible = false
            }

            //태그가 비어있는 경우
            if (it.tag!!.isNotEmpty()) {
                val dataRVAdapter = UploadHashtagRVAdapter(it.tag!!)
                binding.uploadHashtagItem.adapter = dataRVAdapter
                binding.uploadHashtagItem.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
            } else {
                binding.uploadHashtagItem.isVisible = false
            }

            //투표가 있는 경우
            if (it.voteId != null) {

                //투표가 종료인지 확인
                if (it.voteIsEnd == true) {
                    viewModel.setIsEnd(true)
                } else {
                    viewModel.setIsEnd(false)
                }

                //투표 리스트 초기화
                viewModel.initVoteList()

                viewModel.votedList.observe(this) {
                    Log.d("테스트입니다", it.toString())
                    if (it.isNotEmpty()) {
                        sendVote = viewModel.votedList.value!!
                    } else {
                        viewModel.setVoted(false)
                    }
                }

                //각 선택지 투표자 수
                viewModel.setVotePeopleEachOption(CommunityVoteResult(it.votePeopleEachOption!!))
            }
        }

        //각 사람들에 대한 정보가 바뀔 경우
        viewModel.votePeopleEachOption.observe(this) {
            viewModel.setReVote(false)
        }

        //재투표를 한 경우
        viewModel.reVote.observe(this) {

            if (it == true) { //재투표를 한 경우
                binding.communityVoteLayoutComplete.communityPostVoteState.text = "투표진행중"
                binding.communityVoteLayout.communityVoteLayout.isVisible = false
                binding.communityVoteLayoutComplete.communityVoteCompleteLayout.isVisible = true
                binding.communityVoteLayoutComplete.revoteButton.isVisible = false
                binding.communityVoteLayoutComplete.closeVoteButton.isVisible = false
                binding.communityVoteLayoutComplete.voteButton.isVisible = true
                binding.communityVoteLayoutComplete.voteButton.setOnClickListener {
                    viewModel.post_vote(sendVote, viewModel.tok.value!!.voteId.toString())
                }

                //어댑터 설정
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
                        ) // 인텐트를 생성
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
                            //멀티 선택일 경우
                            if (viewModel.tok.value!!.voteIsSingle!! == false) {
                                sendVote.add(data)
                            } else { //싱글 선택일 경우
                                if (sendVote.isEmpty()) {
                                    sendVote.add(data)
                                } else {
                                    BlackToast.createToast(baseContext, "복수 선택이 불가능합니다").show()
                                    voteItemCheck.isChecked = false
                                }
                            }
                        }
                    }
                })
            } else {

                if (viewModel.isEnd.value == true) { // 투표 종료 후
                    binding.communityVoteLayoutComplete.communityPostVoteState.text = "투표 종료"
                    binding.communityVoteLayout.communityVoteLayout.isVisible = false
                    binding.communityVoteLayoutComplete.communityVoteCompleteLayout.isVisible = true
                    binding.communityVoteLayoutComplete.closeVoteButton.isVisible = false
                    binding.communityVoteLayoutComplete.revoteButton.isVisible = false
                    binding.communityVoteLayoutComplete.voteButton.isVisible = false

                    //어댑터 설정
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
                            ) // 인텐트를 생성
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

                } else if (viewModel.tok.value!!.writerOrNot == true) { // 작성자 투표
                    binding.communityVoteLayoutComplete.communityPostVoteState.text = "투표진행중"
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

                    //어댑터 설정
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
                            ) // 인텐트를 생성
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
                } else if (viewModel.isVote.value == false) { // 투표 안했을 때
                    binding.communityVoteLayoutComplete.communityPostVoteState.text = "투표진행중"
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
                                //멀티 선택일 경우
                                if (viewModel.tok.value!!.voteIsSingle!! == false) {
                                    sendVote.add(data)
                                } else { //싱글 선택일 경우
                                    if (sendVote.isEmpty()) {
                                        sendVote.add(data)
                                    } else {
                                        BlackToast.createToast(baseContext, "복수 선택이 불가능합니다").show()
                                        voteItemCheck.isChecked = false
                                    }
                                }
                            }
                        }
                    })

                } else { // 투표 이미 한 상태
                    binding.communityVoteLayoutComplete.communityPostVoteState.text = "투표진행중"
                    binding.communityVoteLayout.communityVoteLayout.isVisible = false
                    binding.communityVoteLayoutComplete.communityVoteCompleteLayout.isVisible = true
                    binding.communityVoteLayoutComplete.revoteButton.isVisible = true
                    binding.communityVoteLayoutComplete.closeVoteButton.isVisible = false
                    binding.communityVoteLayoutComplete.voteButton.isVisible = false
                    binding.communityVoteLayoutComplete.revoteButton.setOnClickListener {
                        viewModel.setReVote(true)
                    }

                    //어댑터 설정
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
                            ) // 인텐트를 생성
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

        //댓글 라이브 데이터
        commentViewModel.comments.observe(this) {

            binding.commentItem.layoutManager = LinearLayoutManager(this)
            /**대댓글 다이얼로그를 위한 파라미터 로직*/
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
                        BlackToast.createToast(baseContext, "댓글 선택이 해제되었습니다").show()
                    } else {
                        commentViewModel.setParentCommentId(data.commentId)
                        BlackToast.createToast(baseContext, "댓글이 선택되었습니다").show()
                    }
                }

                /**댓글 다이얼로그 로직*/
                override fun setting_comment(v: View, data: CommentDto, pos: Int, context: Context) {
                    val writer = viewModel.tok.value!!.writerOrNot

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
                toktokId = viewModel.tok.value!!.toktokId.toString())
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
        }

        /*link 팝업*/
        linkDialog.show()
    }
}