package com.umc.approval.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.umc.approval.databinding.ActivityDocumentBinding
import com.umc.approval.ui.adapter.document_comment_activity.DocumentCommentAdapter
import com.umc.approval.ui.adapter.document_comment_activity.DocumentCommentItem
import com.umc.approval.ui.viewmodel.approval.DocumentViewModel
import com.umc.approval.ui.viewmodel.comment.CommentViewModel
import coil.load
import com.umc.approval.R
import com.umc.approval.ui.adapter.document_comment_activity.DocumentCommentItem2
import com.umc.approval.ui.fragment.document.ApproveDialog
import com.umc.approval.ui.fragment.document.RefuseDialog

class DocumentActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDocumentBinding

    /**Approval view model*/
    private val viewModel by viewModels<DocumentViewModel>()
    private val commentViewModel by viewModels<CommentViewModel>()

    // 승인, 반려 결정 여부
    var isClicked = false
    // 승인, 반려 수 test data
    var approve_num = 10
    var refuse_num = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDocumentBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //back to main activity
        binding.cancel.setOnClickListener {
            finish()
        }

        //서류
        setDocumentData()

        //댓글
        setComment()

        //이전 프래그먼트에서 데이터 가지고 오기
        val documentId = intent.getStringExtra("documentId")
        viewModel.get_document_detail(documentId.toString())

        // like activity로 이동
        binding.documentCommentPostLikes.setOnClickListener {
            startActivity(Intent(this, LikeActivity::class.java))
        }

        viewModel.get_document_detail("0")


        // 승인, 반려 버튼 클릭 처리
        setVote()
        binding.approveButton.setOnClickListener {
            if(isClicked == false){
                val dialog = ApproveDialog()
                dialog.show(supportFragmentManager,dialog.tag)
            }
        }
        binding.refuseButton.setOnClickListener {
            if(isClicked == false){
                val dialog = RefuseDialog()
                dialog.show(supportFragmentManager, dialog.tag)
            }
        }


        viewModel.document.observe(this) {

            binding.profile.load(it.profileImage)
            binding.name.text = it.nickname
            binding.title.text = it.title
            binding.content.text = it.content

            binding.openGraphImage.load(it.link.image)
            binding.openGraphText.text = it.link.title
            binding.openGraphUrl.text = it.link.url


            binding.approveButton.text = "승인" + it.approveCount
            binding.refuseButton.text = "승인" + it.rejectCount

            binding.documentCommentPostLikes.text = it.likedCount.toString()
            binding.documentCommentPostViews.text = it.view.toString()
            binding.documentCommentPostComments.text = it.commentCount.toString()

            binding.image1.load(it.imageUrl.get(0))
            binding.image2.load(it.imageUrl.get(1))
            binding.image3.load(it.imageUrl.get(2))
        }
    }

    private fun setVote(){
        binding.approveButton.text = "승인 " + approve_num.toString()
        binding.refuseButton.text = "반려 " + refuse_num.toString()
    }

    private fun setComment() {
        binding.documentCommentRecyclerview.layoutManager = LinearLayoutManager(this)
        val itemList = ArrayList<DocumentCommentItem2>()
        for (i in 1..20) {
            val itemList2 = ArrayList<DocumentCommentItem>()
            val itemList3 = ArrayList<DocumentCommentItem>()
            itemList2.add(DocumentCommentItem("김부장", "댓글 내용 텍스트입니다 /nabcdefghijklmnopqrstuvwxyz0123456789", "12/22 1 시간 전", 50))
            itemList3.add(DocumentCommentItem("이차장", "댓글 내용 텍스트입니다 /nabcdefghijklmnopqrstuvwxyz0123456789", "12/22 1 시간 전", 50))
            itemList3.add(DocumentCommentItem("이차장", "댓글 내용 텍스트입니다 /nabcdefghijklmnopqrstuvwxyz0123456789", "12/22 1 시간 전", 50))
            itemList3.add(DocumentCommentItem("이차장", "댓글 내용 텍스트입니다 /nabcdefghijklmnopqrstuvwxyz0123456789", "12/22 1 시간 전", 50))
            itemList.add(
                DocumentCommentItem2(DocumentCommentItem2.TYPE_1, itemList2)
            )
            itemList.add(
                DocumentCommentItem2(DocumentCommentItem2.TYPE_2, itemList3)
            )
        }
        val documentCommentAdapter = DocumentCommentAdapter(itemList)
        documentCommentAdapter.notifyDataSetChanged()

        binding.documentCommentRecyclerview.adapter = documentCommentAdapter
    }

    /**live data*/
    private fun setDocumentData(){

        viewModel.document.observe(this) {

            binding.content.text = viewModel.document.value!!.content

            binding.openGraphImage.load(viewModel.document.value!!.link.image)
            binding.openGraphUrl.setText(viewModel.document.value!!.link.url)
            binding.openGraphText.setText(viewModel.document.value!!.link.title)
        }
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

    public fun changeApproveButton() {
        if (isClicked == false) {
            binding.approveButtonIcon.setImageResource(R.drawable.document_approval_icon_selected)
            binding.approveButton.setTextColor(Color.parseColor("#141414"))
            approve_num++
            setVote()
            isClicked = true
        }
    }
    public fun changeRefuseButton(){
        if(isClicked == false){
            binding.refuseButtonIcon.setImageResource(R.drawable.document_refusal_icon_selected)
            binding.refuseButton.setTextColor(Color.parseColor("#141414"))
            refuse_num++
            setVote()
            isClicked = true
        }
    }
}