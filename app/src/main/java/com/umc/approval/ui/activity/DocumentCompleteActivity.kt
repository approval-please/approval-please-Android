package com.umc.approval.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.umc.approval.data.dto.opengraph.OpenGraphDto
import com.umc.approval.databinding.ActivityDocumentBinding
import com.umc.approval.databinding.ActivityDocumentCompleteBinding
import com.umc.approval.ui.adapter.document_comment_activity.DocumentCommentAdapter
import com.umc.approval.ui.adapter.document_comment_activity.DocumentCommentItem
import com.umc.approval.ui.adapter.document_comment_activity.DocumentCommentItem2

class DocumentCompleteActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDocumentCompleteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDocumentCompleteBinding.inflate(layoutInflater)
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

        // like activity로 이동
        binding.documentCommentPostLikes.setOnClickListener {
            startActivity(Intent(this, LikeActivity::class.java))
        }

        // like activity로 이동
        binding.approval.setOnClickListener {
            startActivity(Intent(this, ParticipantActivity::class.java))
        }
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

    private fun setDocumentData(){

        binding.content.text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.\n" +
                " Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, \n" +
                "when an unknown printer took a galley of type and scrambled it to make a type specimen book. \n" +
                "It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. \n" +
                "It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, \n"

        var openGraphDto = OpenGraphDto(
            "https://www.naver.com/",
            "네이버",
            "네이버",
            "네이버",
            "https://s.pstatic.net/static/www/mobile/edit/2016/0705/mobile_212852414260.png"
        )

        binding.openGraphImage.load(openGraphDto.image)
        binding.openGraphUrl.setText(openGraphDto.url)
        binding.openGraphText.setText(openGraphDto.title)
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