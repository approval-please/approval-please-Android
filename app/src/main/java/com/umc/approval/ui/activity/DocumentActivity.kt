package com.umc.approval.ui.activity

import android.content.Intent
import android.graphics.Color
import android.widget.Toast
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.umc.approval.databinding.ActivityDocumentBinding
import com.umc.approval.ui.viewmodel.approval.DocumentViewModel
import com.umc.approval.ui.viewmodel.comment.CommentViewModel
import com.umc.approval.R
import com.umc.approval.data.dto.approval.post.AgreeMyPostDto
import com.umc.approval.data.dto.approval.post.AgreePostDto
import com.umc.approval.data.dto.comment.get.CommentDto
import com.umc.approval.data.dto.comment.post.CommentPostDto
import com.umc.approval.ui.adapter.document_activity.DocumentImageAdapter
import com.umc.approval.ui.adapter.document_comment_activity.DocumentCommentAdapter
import com.umc.approval.ui.adapter.document_comment_activity.DocumentCommentItem
import com.umc.approval.ui.adapter.document_comment_activity.DocumentCommentItem2
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

        setComment()

        //작성 누를 시 댓글 작성
        binding.writeButton.setOnClickListener {
            if (viewModel.accessToken.value != false) {
                val postComment = CommentPostDto(documentId = viewModel.document.value!!.documentId,
                    content = binding.commentEdit.text.toString())
                commentViewModel.post_comments(postComment)
                binding.commentEdit.text.clear()
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

        //공유 버튼
        binding.shareButton.setOnClickListener {
            share()
        }

        //좋아요 버튼
        binding.likeButton.setOnClickListener {
            likeDocument()
        }
    }
    /* 하트 아이콘 누르는 경우 좋아요 처리하는 함수 */
    private fun likeDocument(){
        if(viewModel.accessToken.value == true){
            if(viewModel.document.value!!.isLiked == false){
                binding.likeButton.setImageResource(R.drawable.document_comment_icon_heart_selected)
                Toast.makeText(this,"결재서류에 좋아요를 눌렀습니다.", Toast.LENGTH_SHORT).show()
                val likedCount = viewModel.document.value!!.likedCount
                if(likedCount != null){
                    // 좋아요 수 변경(+1) 서버에 반영하는 로직 이후 추가
                    binding.documentCommentPostLikes.text = "좋아요 " + likedCount
                }
            }
            else{
                binding.likeButton.setImageResource(R.drawable.document_comment_icon_heart)
                Toast.makeText(this,"결재서류의 좋아요를 취소했습니다.", Toast.LENGTH_SHORT).show()
                binding.documentCommentPostLikes.text = "좋아요 " + viewModel.document.value!!.likedCount
                val likedCount = viewModel.document.value!!.likedCount
                if(likedCount != null){
                    // 좋아요 수 변경(-1) 서버에 반영하는 로직 이후 추가
                    binding.documentCommentPostLikes.text = "좋아요 " + likedCount
                }
            }
        }
        else {
            Toast.makeText(this, "로그인 과정이 필요합니다", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
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

        //결재 서류 라이브 데이터
        viewModel.document.observe(this) {

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
            binding.refuseButton.text = "승인" + it.rejectCount
            binding.approveNum.text = "승인" + it.approveCount
            binding.rejectNum.text = "반려" + it.rejectCount

            //링크 처리
            if (it.link != null) {
                binding.openGraphImage.load(it.link.image)
                binding.openGraphText.text = it.link.title
                binding.openGraphUrl.text = it.link.url
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
            }
        }


        //댓글 라이브 데이터
        // 테스트 이전 코드
        commentViewModel.comments.observe(this) {
            val itemList = ArrayList<DocumentCommentItem2>()
            for(i in 0.. it.commentCount){
                val content = it.content[i]
                if(!content.isDeleted){
                    val itemList2 = ArrayList<DocumentCommentItem>()
                    itemList2.add(DocumentCommentItem(content.profileImage, content.nickname, content.level, content.content,content.datetime, content.likeCount, content.isWriter, content.isLike))
                    itemList.add(DocumentCommentItem2(DocumentCommentItem2.TYPE_1, itemList2))
                    if(content.childComment.count() != 0){
                        val childItemList = ArrayList<DocumentCommentItem>()
                        val childComment = content.childComment
                        for(j in 0..childComment.count() - 1){
                            childItemList.add(DocumentCommentItem(childComment[j].profileImage, childComment[j].nickname, childComment[j].level, childComment[j].content, childComment[j].datetime, childComment[j].likeCount, childComment[j].isWriter, childComment[j].isLike))
                        }
                        itemList.add(DocumentCommentItem2(DocumentCommentItem2.TYPE_2, childItemList))
                    }
                }
            }
            binding.documentCommentRecyclerview.layoutManager = LinearLayoutManager(this)
            val documentCommentAdapter = DocumentCommentAdapter(itemList,this)
            documentCommentAdapter.notifyDataSetChanged()
            binding.documentCommentRecyclerview.adapter = documentCommentAdapter

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

        val documentId = intent.getStringExtra("documentId")

        viewModel.get_document_detail(documentId.toString())

        commentViewModel.get_comments(documentId = documentId.toString())
    }

    //뷰 재시작시 로그인 상태 검증 및 서류 정보 가지고 오는 로직
    override fun onResume() {
        super.onResume()

        /**AccessToken 확인해서 로그인 상태인지 아닌지 확인*/
        viewModel.checkAccessToken()

        val documentId = intent.getStringExtra("documentId")

        viewModel.get_document_detail(documentId.toString())

        commentViewModel.get_comments(documentId = documentId.toString())
    }

    private fun setComment() {
        // 서버 데이터 형식에 맞게 local 데이터 추가,
        // 적용되는 것 확인
        binding.documentCommentRecyclerview.layoutManager = LinearLayoutManager(this)
        val itemList = ArrayList<DocumentCommentItem2>()
        for (i in 1..20) {
            val itemList2 = ArrayList<DocumentCommentItem>()
            val itemList3 = ArrayList<DocumentCommentItem>()
            itemList2.add(DocumentCommentItem("", "김사원", 0, "댓글 내용 텍스트입니다 /nabcdefghijklmnopqrstuvwxyz0123456789", "12/22 1 시간 전", 50, false, false))
            itemList3.add(DocumentCommentItem("", "이주임", 1, "댓글 내용 텍스트입니다 /nabcdefghijklmnopqrstuvwxyz0123456789", "12/22 1 시간 전", 50, false, true))
            itemList3.add(DocumentCommentItem("", "이대리", 2, "댓글 내용 텍스트입니다 /nabcdefghijklmnopqrstuvwxyz0123456789", "12/22 1 시간 전", 50, false, false))
            itemList3.add(DocumentCommentItem("", "이과장", 3, "댓글 내용 텍스트입니다 /nabcdefghijklmnopqrstuvwxyz0123456789", "12/22 1 시간 전", 50, false, true))
            itemList3.add(DocumentCommentItem("", "이차장", 4, "댓글 내용 텍스트입니다 /nabcdefghijklmnopqrstuvwxyz0123456789", "12/22 1 시간 전", 50, false, false))
            itemList3.add(DocumentCommentItem("", "이부장", 5, "댓글 내용 텍스트입니다 /nabcdefghijklmnopqrstuvwxyz0123456789", "12/22 1 시간 전", 50, false, true))
            itemList3.add(DocumentCommentItem("", "글쓴이", 5, "댓글 내용 텍스트입니다 /nabcdefghijklmnopqrstuvwxyz0123456789", "12/22 1 시간 전", 50, true, false))
            itemList.add(
                DocumentCommentItem2(DocumentCommentItem2.TYPE_1, itemList2)
            )
            itemList.add(
                DocumentCommentItem2(DocumentCommentItem2.TYPE_2, itemList3)
            )
        }
        val documentCommentAdapter = DocumentCommentAdapter(itemList, this)
        documentCommentAdapter.notifyDataSetChanged()

        binding.documentCommentRecyclerview.adapter = documentCommentAdapter
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

    public fun getDocumentViewModel() : DocumentViewModel{
        return viewModel
    }

    public fun sendNeedLoginToast(){
        Toast.makeText(this, "로그인 과정이 필요합니다", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}