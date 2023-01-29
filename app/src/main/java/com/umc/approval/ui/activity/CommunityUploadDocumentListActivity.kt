package com.umc.approval.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.data.dto.approval.get.ApprovalPaper
import com.umc.approval.databinding.ActivityCommunityUploadDocumentListBinding
import com.umc.approval.ui.adapter.community_upload_activity.CommunityUploadDocumentItemRVAdapter

class CommunityUploadDocumentListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCommunityUploadDocumentListBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityUploadDocumentListBinding.inflate(layoutInflater)

        var view = binding.root
        setContentView(view)

        binding.uploadCancelBtn.setOnClickListener{
            finish()
        }

        val documentList: ArrayList<ApprovalPaper> = arrayListOf()  // 샘플 데이터

        documentList.apply {
            add(
                ApprovalPaper(
                    2, 0, "30분전",
                    mutableListOf(),
                    "아이폰 14 Pro", "새로 출시된 아이폰 골드입니다", mutableListOf("기계", "환경 "),
                    1000, 32, 12
                )
            )

            add(
                ApprovalPaper(
                    1, 0, "30분전",
                    mutableListOf("https://s.pstatic.net/static/www/mobile/edit/2016/0705/mobile_212852414260.png"),
                    "아이폰 14 Pro", "새로 출시된 아이폰 골드입니다", mutableListOf("기계", "환경 "),
                    1000, 32, 12
                )
            )

            add(
                ApprovalPaper(
                    2, 0, "30분전",
                    mutableListOf(),
                    "아이폰 14 Pro", "새로 출시된 아이폰 골드입니다", mutableListOf("기계", "환경 "),
                    1000, 32, 12
                )
            )

            add(
                ApprovalPaper(
                    1, 0, "30분전",
                    mutableListOf("https://s.pstatic.net/static/www/mobile/edit/2016/0705/mobile_212852414260.png"),
                    "아이폰 14 Pro", "새로 출시된 아이폰 골드입니다", mutableListOf("기계", "환경 "),
                    1000, 32, 12
                )
            )

            add(
                ApprovalPaper(
                    1, 0, "30분전",
                    mutableListOf("https://s.pstatic.net/static/www/mobile/edit/2016/0705/mobile_212852414260.png"),
                    "아이폰 14 Pro", "새로 출시된 아이폰 골드입니다", mutableListOf("기계", "환경 "),
                    1000, 32, 12
                )
            )
        }

        binding.tvDepartmentCount.text = documentList.size.toString()

        val dataRVAdapter = CommunityUploadDocumentItemRVAdapter(documentList)
        binding.uploadDocumentItem.adapter = dataRVAdapter
        binding.uploadDocumentItem.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        // 클릭 이벤트 처리
        dataRVAdapter.setOnItemClickListener(object: CommunityUploadDocumentItemRVAdapter.OnItemClickListner {
            override fun onItemClick(v: View, data: ApprovalPaper, pos: Int) {
                val documentIntent = Intent() // 인텐트를 생성
                documentIntent.putExtra("title", data.title)
                setResult(RESULT_OK, documentIntent)
                finish()
            }
        })
    }

}
