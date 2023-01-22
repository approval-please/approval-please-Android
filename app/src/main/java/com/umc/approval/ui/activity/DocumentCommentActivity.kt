package com.umc.approval.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.approval.databinding.ActivityDocumentCommentBinding
import com.umc.approval.ui.adapter.document_comment_activity.DocumentCommentAdapter
import com.umc.approval.ui.adapter.document_comment_activity.DocumentCommentItem

class DocumentCommentActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDocumentCommentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDocumentCommentBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setDocumentData()

        binding.documentCommentRecyclerview.layoutManager = LinearLayoutManager(this)
        val itemList = ArrayList<DocumentCommentItem>()
        for (i in 1..20){
            itemList.add(DocumentCommentItem("김부장", "댓글 내용 예시 텍스트 공간입니다. 가나다라마바사아자차카파하 ABCDEFGHIJKLMNOPQRSTUVWXYZ", "12/31 1 시간 전", 50))
        }
        val documentCommentAdapter = DocumentCommentAdapter(itemList)
        documentCommentAdapter.notifyDataSetChanged()

        binding.documentCommentRecyclerview.adapter = documentCommentAdapter
    }

    private fun setDocumentData(){
        binding.row2NameTextview.text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.\n" +
                " Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, \n" +
                "when an unknown printer took a galley of type and scrambled it to make a type specimen book. \n" +
                "It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. \n" +
                "It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, \n" +
                "and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum." +
                "Lorem Ipsum is simply dummy text of the printing and typesetting industry.\n" +
                " Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, \n" +
                "when an unknown printer took a galley of type and scrambled it to make a type specimen book. \n" +
                "It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. \n" +
                "It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, \n" +
                "and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum." +
                "Lorem Ipsum is simply dummy text of the printing and typesetting industry.\n" +
                " Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, \n" +
                "when an unknown printer took a galley of type and scrambled it to make a type specimen book. \n" +
                "It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. \n" +
                "It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, \n" +
                "and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."
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