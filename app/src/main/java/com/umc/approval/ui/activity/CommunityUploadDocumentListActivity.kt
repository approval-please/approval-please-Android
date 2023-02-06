package com.umc.approval.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.data.dto.approval.get.ApprovalPaper
import com.umc.approval.data.dto.approval.get.DocumentWithReportDto
import com.umc.approval.databinding.ActivityCommunityUploadDocumentListBinding
import com.umc.approval.ui.adapter.community_upload_activity.CommunityUploadDocumentItemRVAdapter
import com.umc.approval.ui.viewmodel.approval.DocumentWithReportViewModel

class CommunityUploadDocumentListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCommunityUploadDocumentListBinding

    private val viewModel by viewModels<DocumentWithReportViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityUploadDocumentListBinding.inflate(layoutInflater)

        var view = binding.root
        setContentView(view)

        viewModel.get_documents()

        //서류 가져온 후 설정
        viewModel.documents.observe(this) {

            if (it.content != null) {
                binding.tvDepartmentCount.text = it.content.size.toString()
                if (it.content.isNotEmpty()) {
                    val dataRVAdapter = CommunityUploadDocumentItemRVAdapter(it)
                    binding.uploadDocumentItem.adapter = dataRVAdapter
                    binding.uploadDocumentItem.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

                    // 클릭 이벤트 처리
                    dataRVAdapter.setOnItemClickListener(object: CommunityUploadDocumentItemRVAdapter.OnItemClickListner {
                        override fun onItemClick(v: View, data: DocumentWithReportDto, pos: Int) {
                            val documentIntent = Intent() // 인텐트를 생성
                            documentIntent.putExtra("documentId", data.documentId)
                            setResult(RESULT_OK, documentIntent)
                            finish()
                        }
                    })
                }
            }
        }

        binding.uploadCancelBtn.setOnClickListener{
            finish()
        }
    }
}
