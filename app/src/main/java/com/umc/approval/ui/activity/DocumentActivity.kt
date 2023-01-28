package com.umc.approval.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.umc.approval.data.dto.comment.post.CommentPostDto
import com.umc.approval.databinding.ActivityDocumentBinding
import com.umc.approval.ui.adapter.document_comment_activity.DocumentCommentAdapter
import com.umc.approval.ui.adapter.document_comment_activity.DocumentCommentItem
import com.umc.approval.ui.viewmodel.approval.DocumentViewModel
import com.umc.approval.ui.viewmodel.comment.CommentViewModel

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

        //back to main activity
        binding.cancel.setOnClickListener {
            finish()
        }

        //서류
        setDocumentData()

        //댓글
        setComment()

        //이전 프래그먼트에서 데이터 가지고 오기
        viewModel.get_document_detail("0")

        //댓글 가지고 오기
        commentViewModel.get_document_comments()

        commentViewModel.post_comments(CommentPostDto(content = "123", image = "13"))

        // like activity로 이동
        binding.documentCommentPostLikes.setOnClickListener {
            startActivity(Intent(this, LikeActivity::class.java))
        }

        //초기화
        viewModel.init_document()
    }

    private fun setComment() {
        binding.documentCommentRecyclerview.layoutManager = LinearLayoutManager(this)
        val itemList = ArrayList<DocumentCommentItem>()
        for (i in 1..20) {
            itemList.add(
                DocumentCommentItem(
                    "김부장",
                    "댓글 내용 예시 텍스트 공간입니다. 가나다라마바사아자차카파하 ABCDEFGHIJKLMNOPQRSTUVWXYZ",
                    "12/31 1 시간 전",
                    50
                )
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
}