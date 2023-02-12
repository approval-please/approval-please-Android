package com.umc.approval.ui.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialog
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

    /*다이얼로그*/
    private lateinit var activityCommunityReportPostDialogBinding: ActivityCommunityReportPostDialogBinding
    private lateinit var activityCommunityReportUserDialogBinding: ActivityCommunityReportUserDialogBinding
    private lateinit var activityCommunityRemovePostDialogBinding: ActivityCommunityRemovePostDialogBinding

    /*다이얼로그 버튼*/
    private lateinit var dialogCancelButton : Button
    private lateinit var dialogConfirmButton : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDocumentBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //다른 곳으로 이동하는 서비스
        move_to_other()

        approve_or_reject()

        post_more()

        //서류가 들어왔을때 View 구성
        live_data()

        binding.heart.setOnClickListener {

            if (viewModel.accessToken.value == false) {
                BlackToast.createToast(this, "로그인이 필요한 서비스 입니다").show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                followViewModel.like(documentId = viewModel.document.value!!.documentId)
            }
        }

        //작성 누를 시 댓글 작성
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
                BlackToast.createToast(this, "로그인이 필요한 서비스 입니다").show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        
        //보고서 작성 버튼
        binding.reportWriteButton.setOnClickListener {
            val intent = Intent(this, CommunityUploadActivity::class.java)

            intent.putExtra("documentId", viewModel.document.value!!.documentId.toString())
            intent.putExtra("report", true)

            startActivity(intent)

            finish()
        }

        //보고서 확인 버튼
        binding.reportCheckButton.setOnClickListener {
            /**결재서류 아이디를 넘김*/
            val intent = Intent(this, CommunityReportActivity::class.java)
            intent.putExtra("reportId", viewModel.document.value!!.reportId.toString())
            startActivity(intent)
        }
        
        binding.shareButton.setOnClickListener {
            share()
        }

        // 좋아요 누른 유저 확인
        binding.documentCommentPostLikes.setOnClickListener {
            // 결재 서류 ID를 넘김
            val intent = Intent(this, LikeActivity::class.java)
            intent.putExtra("type", "document")
            intent.putExtra("id", viewModel.document.value!!.documentId)
            startActivity(intent)
        }

        //이름 누를시 이동
        binding.name.setOnClickListener {
            if (viewModel.document.value!!.isWriter == false) {
                val intent = Intent(this, OtherpageActivity::class.java)
                intent.putExtra("userId", viewModel.document.value!!.userId)
                startActivity(intent)
            }
        }
    }

    /* 공유 버튼 누르는 경우 공유 창 발생시키는 함수 */
    private fun share(){
        var sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.setType("text/html")
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "postlink")
        startActivity(Intent.createChooser(sharingIntent, "sharing text"))
    }

    //결재 또는 반려 버튼 클릭 로직
    private fun approve_or_reject() {
        //반려버튼 클릭시 엑세스 토큰으로 로그인 상태 확인 후, 로그인 아니면 로그인 과정 거치기
        //투표를 하지 않았을때만 로직 진행
        binding.approveButton.setOnClickListener {
            if (viewModel.accessToken.value == true) {
                if (viewModel.document.value!!.isVoted == 0) {
                    val dialog = ApproveDialog()
                    dialog.show(supportFragmentManager, dialog.tag)
                }
            } else {
                BlackToast.createToast(this, "로그인이 필요한 서비스 입니다").show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        //반려버튼 클릭시 엑세스 토큰으로 로그인 상태 확인 후, 로그인 아니면 로그인 과정 거치기
        //투표를 하지 않았을때만 로직 진행
        binding.refuseButton.setOnClickListener {
            if (viewModel.accessToken.value == true) {
                if (viewModel.document.value!!.isVoted == 0) {
                    val dialog = RefuseDialog()
                    dialog.show(supportFragmentManager, dialog.tag)
                }
            } else {
                BlackToast.createToast(this, "로그인이 필요한 서비스 입니다").show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    //라이브 데이터
    private fun live_data() {

        viewModel.after.observe(this) {
            binding.approveButton.text = "승인" + it.approveCount
            binding.refuseButton.text = "반려" + it.rejectCount
        }

        //좋아요 로직
        followViewModel.like.observe(this) {
            if (it == true) {
                binding.heart.setImageResource(R.drawable.document_heart_fill)
            } else {
                binding.heart.setImageResource(R.drawable.document_comment_icon_heart)
            }
        }

        //댓글 좋아요 로직
        followViewModel.commentLike.observe(this) {
            commentViewModel.get_comments(documentId = viewModel.document.value!!.documentId.toString())
        }

        //결재 서류 라이브 데이터
        viewModel.document.observe(this) {

            commentViewModel.get_comments(documentId = it.documentId.toString())

            followViewModel.setLike(it.isLiked!!)

            //scrap했다면
            if (it.isScrap == true) {
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
            binding.documentCommentPostLikes.text = "좋아요 " + it.likedCount.toString()
            binding.documentCommentPostViews.text = "조회수 " + it.view.toString()
            binding.documentCommentPostComments.text = "댓글 " + it.commentCount.toString()
            binding.documentCommentPostTime.text = it.datetime
            binding.approveButton.text = "승인" + it.approveCount
            binding.refuseButton.text = "반려" + it.rejectCount
            binding.approveNum.text = "승인" + it.approveCount
            binding.rejectNum.text = "반려" + it.rejectCount

            //링크 처리
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

            //작성자일 경우
            if (viewModel.document.value!!.isWriter == true) {
                //만약 결재 서류가 없으면
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
                //만약 결재 서류가 없으면
                if (viewModel.document.value!!.reportId != null) {
                    binding.reportWriteButton.isVisible = false
                    binding.reportCheckButton.isVisible = true
                } else {
                    binding.reportWriteButton.isVisible = false
                    binding.reportCheckButton.isVisible = false
                }
            }

            //투표를 이미 했으면 색 세팅
            if (viewModel.document.value!!.isVoted == 1) {

                //작성자면
                if (viewModel.document.value!!.isWriter == true) {

                    //투표 다르게 보이게 설정
                    binding.approveArea.isVisible = false
                    binding.writerApprove.isVisible = true
                    binding.approval.isVisible = true
                    binding.approval.setImageResource(R.drawable.document_result_approval)

                } else { //작성자가 아닐때
                    binding.approveButtonIcon.setImageResource(R.drawable.document_approval_icon_selected)
                    binding.approveButton.setTextColor(Color.parseColor("#141414"))
                }

            } else if (viewModel.document.value!!.isVoted == 2) {

                //작성자면
                if (viewModel.document.value!!.isWriter == true) {

                    //투표 다르게 보이게 설정
                    binding.approveArea.isVisible = false
                    binding.writerApprove.isVisible = true
                    binding.approval.isVisible = true
                    binding.approval.setImageResource(R.drawable.document_result_refusal)

                } else {
                    binding.refuseButtonIcon.setImageResource(R.drawable.document_refusal_icon_selected)
                    binding.refuseButton.setTextColor(Color.parseColor("#141414"))
                }
            } else if (viewModel.document.value!!.isVoted == 0) {
                if (viewModel.document.value!!.state == 0) {

                    //투표 다르게 보이게 설정
                    binding.approveArea.isVisible = false
                    binding.writerApprove.isVisible = true
                    binding.approval.isVisible = true
                    binding.approval.setImageResource(R.drawable.document_result_approval)
                } else if (viewModel.document.value!!.state == 1) {

                    //투표 다르게 보이게 설정
                    binding.approveArea.isVisible = false
                    binding.writerApprove.isVisible = true
                    binding.approval.isVisible = true
                    binding.approval.setImageResource(R.drawable.document_result_refusal)
                }
            }
        }


        //댓글 라이브 데이터
        commentViewModel.comments.observe(this) {

            binding.documentCommentRecyclerview.layoutManager = LinearLayoutManager(this)

            /**대댓글 다이얼로그를 위한 파라미터 로직*/
            val documentCommentAdapter = ParentCommentAdapter(it, this, layoutInflater,
                viewModel.document.value!!.isWriter!!, followViewModel, commentViewModel, documentId = viewModel.document.value!!.documentId.toString())
            documentCommentAdapter.notifyDataSetChanged()
            binding.documentCommentRecyclerview.adapter = documentCommentAdapter

            documentCommentAdapter.itemClick = object : ParentCommentAdapter.ItemClick {

                override fun make_chid_comment(v: View, data: CommentDto, pos: Int) {
                    if (data.commentId.toString() == commentViewModel.commentId.value.toString()) {
                        commentViewModel.setParentCommentId(-1)
                        BlackToast.createToast(baseContext, "댓글 선택이 해제되었습니다.").show()
                    } else {
                        BlackToast.createToast(baseContext, "댓글이 선택되었습니다.").show()
                        commentViewModel.setParentCommentId(data.commentId)
                    }
                }

                override fun like(v: View, data: CommentDto, pos: Int) {
                    followViewModel.like(commentId = data.commentId)
                }

                /**댓글 다이얼로그 로직*/
                override fun setting_comment(v: View, data: CommentDto, pos: Int, context: Context) {
                    val writer = viewModel.document.value!!.isWriter

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

    //뷰 이동 로직
    private fun move_to_other() {
        //좋아요 액티비티로 이동
        binding.documentCommentPostLikes.setOnClickListener {
            startActivity(Intent(this, LikeActivity::class.java))
        }

        binding.cancel.setOnClickListener {
            finish()
        }
    }

    //시작시 로그인 상태 검증 및 서류 정보 가지고 오는 로직
    override fun onStart() {
        super.onStart()

        /**AccessToken 확인해서 로그인 상태인지 아닌지 확인*/
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

    //승인 시 승인 Api
    fun changeApproveButton() {

        if (viewModel.document.value!!.isWriter == true) {

            //투표 다르게 보이게 설정
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

    //반려 시 반려 Api
    fun changeRefuseButton(){

        if (viewModel.document.value!!.isWriter == true) {

            //투표 다르게 보이게 설정
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

    /* 문자열로 직급 반환하는 함수 */
    private fun setRank(rankInt : Int?) : String?{
        var rank : String? = null
        when(rankInt){
            0->{ rank = "사원" }
            1->{ rank = "주임" }
            2->{ rank = "대리" }
            3->{ rank = "과장" }
            4->{ rank = "차장" }
            5->{ rank = "부장" }
        }
        return rank
    }

    //다이얼로그
    //다이얼로그 로직
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
            setting_notice_on_text.setText("이 결재서류 알람 받기")
            val setting_notice_off_text = bottomSheetView.findViewById<TextView>(R.id.notice_off_text)
            setting_notice_off_text.setText("이 결재서류 알람 끄기")
            val setting_storage_on_text = bottomSheetView.findViewById<TextView>(R.id.storage_on_text)
            setting_storage_on_text.setText("이 결재서류 보관함에 넣기")
            val setting_storage_off_text = bottomSheetView.findViewById<TextView>(R.id.storage_off_text)
            setting_storage_off_text.setText("이 결재서류 보관함에서 빼기")
            val setting_report_post_text = bottomSheetView.findViewById<TextView>(R.id.report_report_text)
            setting_report_post_text.setText("이 결재서류 신고하기")
            val setting_remove_post_text = bottomSheetView.findViewById<TextView>(R.id.remove_post_text)
            setting_remove_post_text.setText("이 결재서류 삭제하기")

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
                followViewModel.notification(documentId = viewModel.document.value!!.documentId)
                bottomSheetDialog.cancel()
                BlackToast.createToast(this, "이 결재서류의 알람을 받아요.").show()
            }

            setting_notice_off!!.setOnClickListener {
                followViewModel.notification(documentId = viewModel.document.value!!.documentId)
                bottomSheetDialog.cancel()
                BlackToast.createToast(this, "이 결재서류의 알람을 더 이상 받지 않아요.").show()
            }

            setting_storage_on!!.setOnClickListener {
                followViewModel.scrap(documentId = viewModel.document.value!!.documentId)
                bottomSheetDialog.cancel()
                BlackToast.createToast(this, "이 결재서류를 보관함에 넣었어요.").show()
            }

            setting_storage_off!!.setOnClickListener {
                followViewModel.scrap(documentId = viewModel.document.value!!.documentId)
                bottomSheetDialog.cancel()
                BlackToast.createToast(this, "이 결재서류를 보관함에서 뺐어요.").show()
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
        text.setText("결재서류를 작성한 경우,\n보고서도 함께 삭제됩니다.\n\n이 결재서류를 삭제하시겠습니까?\n")

        dialogConfirmButton.setText("결재서류 삭제하기")

        /*취소버튼*/
        dialogCancelButton.setOnClickListener {
            linkDialog.dismiss()
        }

        /*확인버튼*/
        dialogConfirmButton.setOnClickListener{
            linkDialog.dismiss()
            viewModel.delete_document(viewModel.document.value!!.documentId.toString())
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

        val text = activityCommunityReportUserDialogBinding.communityDialog
        text.setText("이 사용자를 신고하시겠습니까?")

        /*취소버튼*/
        dialogCancelButton.setOnClickListener {
            linkDialog.dismiss()
        }

        /*확인버튼*/
        dialogConfirmButton.setOnClickListener{
            followViewModel.accuse(accuseUserId = viewModel.document.value!!.userId)
            linkDialog.dismiss()
            BlackToast.createToast(this, "신고가 접수되었습니다.").show()
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
        val text = activityCommunityReportPostDialogBinding.communityDialog
        text.setText("이 결재서류를 신고하시겠습니까?")

        dialogCancelButton = activityCommunityReportPostDialogBinding.communityDialogCancelButton
        dialogConfirmButton = activityCommunityReportPostDialogBinding.communityDialogConfirmButton

        dialogConfirmButton.setText("결재서류 신고하기")

        /*취소버튼*/
        dialogCancelButton.setOnClickListener {
            linkDialog.dismiss()
        }

        /*확인버튼*/
        dialogConfirmButton.setOnClickListener{
            followViewModel.accuse(documentId = viewModel.document.value!!.documentId)
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
                documentId = viewModel.document.value!!.documentId.toString())
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