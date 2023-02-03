package com.umc.approval.ui.activity

import android.content.Intent
import android.graphics.Color
import android.widget.Toast
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.umc.approval.App
import com.umc.approval.databinding.ActivityDocumentBinding
import com.umc.approval.ui.viewmodel.approval.DocumentViewModel
import com.umc.approval.ui.viewmodel.comment.CommentViewModel
import com.umc.approval.R
import com.umc.approval.data.dto.approval.post.AgreeMyPostDto
import com.umc.approval.data.dto.approval.post.AgreePostDto
import com.umc.approval.data.dto.comment.get.CommentDto
import com.umc.approval.data.dto.comment.post.CommentPostDto
import com.umc.approval.data.dto.community.get.CommunityReport
import com.umc.approval.ui.adapter.community_fragment.CommunityReportItemRVAdapter
import com.umc.approval.ui.adapter.document_activity.DocumentImageAdapter
import com.umc.approval.ui.adapter.document_comment_activity.DocumentCommentAdapter
import com.umc.approval.ui.adapter.document_comment_activity.DocumentCommentItem
import com.umc.approval.ui.adapter.document_comment_activity.DocumentCommentItem2
import com.umc.approval.ui.adapter.document_comment_activity.ParentCommentAdapter
import com.umc.approval.ui.fragment.document.ApproveDialog
import com.umc.approval.ui.fragment.document.RefuseDialog
import com.umc.approval.ui.fragment.mypage.MypageFragment
import com.umc.approval.util.Utils.categoryMap

class DocumentActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDocumentBinding

    /**Approval view model*/
    private val viewModel by viewModels<DocumentViewModel>()
    private val commentViewModel by viewModels<CommentViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDocumentBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //다른 곳으로 이동하는 서비스
        move_to_other()

        approve_or_reject()

        //서류가 들어왔을때 View 구성
        live_data()

        binding.heart.setOnClickListener {

            if (viewModel.accessToken.value == false) {
                Toast.makeText(this, "로그인 과정이 필요합니다", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } else {
                viewModel.document_like()
            }
        }

        //작성 누를 시 댓글 작성
        binding.writeButton.setOnClickListener {
            if (viewModel.accessToken.value != false) {
                val postComment = CommentPostDto(documentId = viewModel.document.value!!.documentId,
                    content = binding.commentEdit.text.toString(), parentCommentId = null)

                Log.d("테스트입니다", commentViewModel.commentId.value.toString())

                if (commentViewModel.commentId.value != -1) {
                    postComment.parentCommentId = commentViewModel.commentId.value
                }

                commentViewModel.post_comments(postComment, documentId = viewModel.document.value!!.documentId.toString())
                binding.commentEdit.text.clear()
                commentViewModel.setParentCommentId(-1)
            } else {
                Toast.makeText(this, "로그인 과정이 필요합니다", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
        
        //보고서 작성 버튼
        binding.reportWriteButton.setOnClickListener {
            val intent = Intent(this, CommunityUploadActivity::class.java)

            intent.putExtra("documentId", viewModel.document.value!!.documentId.toString())
            intent.putExtra("report", true)

            startActivity(intent)
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
                Toast.makeText(this, "로그인 과정이 필요합니다", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this, "로그인 과정이 필요합니다", Toast.LENGTH_SHORT).show()
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

        //로직
        viewModel.like.observe(this) {
            if (it == true) {
                binding.heart.setImageResource(R.drawable.fill_heart)
            } else {
                binding.heart.setImageResource(R.drawable.document_comment_icon_heart)
            }
        }

        //결재 서류 라이브 데이터
        viewModel.document.observe(this) {

            viewModel.setLike(it.isLiked!!)

            binding.cate.text = categoryMap[it.category]

            binding.profile.load(it.profileImage)
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
                // 리사이클러뷰 레이아웃 설정
                binding.imageRecyclerview.layoutManager = LinearLayoutManager(applicationContext)

                // 이미지 리사이클러뷰 연결
                val documentImageAdapter = DocumentImageAdapter(it.imageUrl)
                documentImageAdapter.notifyDataSetChanged()
                binding.imageRecyclerview.adapter = documentImageAdapter
            }

            //작성자일 경우
            if (viewModel.document.value!!.isWriter == true) {

                //만약 결재 서류가 없으면
                if (viewModel.document.value!!.reportId == null) {
                    binding.reportWriteButton.isVisible = true
                } else {
                    binding.reportCheckButton.isVisible = true
                }
            } else {
                //만약 결재 서류가 없으면
                if (viewModel.document.value!!.reportId != null) {
                    binding.reportCheckButton.isVisible = true
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
                if (viewModel.document.value!!.state == 1) {

                    //투표 다르게 보이게 설정
                    binding.approveArea.isVisible = false
                    binding.writerApprove.isVisible = true
                    binding.approval.isVisible = true
                    binding.approval.setImageResource(R.drawable.document_result_approval)
                } else if (viewModel.document.value!!.isVoted == 2) {

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
            val documentCommentAdapter = ParentCommentAdapter(it)
            documentCommentAdapter.notifyDataSetChanged()
            binding.documentCommentRecyclerview.adapter = documentCommentAdapter

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

        commentViewModel.get_comments(documentId = documentId.toString())
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
}