package com.umc.approval.ui.activity

import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.amazonaws.regions.Regions
import com.google.android.material.tabs.TabLayoutMediator
import com.umc.approval.API
import com.umc.approval.data.dto.upload.post.ReportUploadDto
import com.umc.approval.data.dto.upload.post.TalkUploadDto
import com.umc.approval.databinding.ActivityCommunityUploadBinding
import com.umc.approval.ui.adapter.community_upload_activity.CommunityUploadVPAdapter
import com.umc.approval.ui.viewmodel.community.CommunityReportUploadViewModel
import com.umc.approval.ui.viewmodel.community.CommunityUploadViewModel
import com.umc.approval.ui.viewmodel.community.CommunityViewModel
import com.umc.approval.util.S3Util
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

class CommunityUploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommunityUploadBinding

    val viewModel by viewModels<CommunityViewModel>()

    val tokViewModel by viewModels<CommunityUploadViewModel>()

    val reportViewModel by viewModels<CommunityReportUploadViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val report = intent.getBooleanExtra("report",false)
        val documentId = intent.getStringExtra("documentId")

        if (documentId != null) {
            reportViewModel.setDocumentId(documentId.toInt())
        }

        val communityUploadVPAdapter = CommunityUploadVPAdapter(this)
        binding.uploadTabVp.adapter = communityUploadVPAdapter


        val tabTitleArray = arrayOf(
            "결재톡톡",
            "결재보고서"
        )

        TabLayoutMediator(binding.uploadCommunityTab, binding.uploadTabVp){ tab,position->
            tab.text = tabTitleArray[position]
        }.attach()

        if(report){
            binding.uploadTabVp.currentItem = 1
        }


        /**액티비티 종료 버튼*/
        binding.uploadCancelBtn.setOnClickListener {
            finish()
        }

        //제출
        binding.uploadSubmitBtn.setOnClickListener {

            if (viewModel.tabId.value.toString() == "0") {

                if (tokViewModel.category.value == 18) {
                    Toast.makeText(this, "카테고리를 선택하셔야 합니다", Toast.LENGTH_SHORT).show()
                } else {

                    if (tokViewModel.content.value == null || tokViewModel.content.value == "") {
                        Toast.makeText(this, "내용을 입력하셔야 합니다", Toast.LENGTH_SHORT).show()
                    } else {

                        CoroutineScope(Dispatchers.IO).launch {
                            if (tokViewModel.pic.value != null) {
                                tok_S3_connect()
                            }

                            val talkUploadDto = TalkUploadDto(
                                tokViewModel.category.value, tokViewModel.content.value,
                                tokViewModel.voteTitle.value, tokViewModel.voteIsSingle.value,
                                tokViewModel.voteIsAnonymous.value,
                                tokViewModel.voteOption.value, tokViewModel.opengraph_list.value,
                                tokViewModel.tags.value, tokViewModel.images.value)

                            tokViewModel.post_tok(talkUploadDto)

                            finish()
                        }
                    }
                }
            } else {

                if (viewModel.tabId.value.toString() == "1") {

                    if (reportViewModel.documentId.value == null) {
                        Toast.makeText(this, "결재서류를 선택하셔야 합니다", Toast.LENGTH_SHORT).show()
                    } else {
                        if (reportViewModel.content.value == null || reportViewModel.content.value == "") {
                            Toast.makeText(this, "내용을 입력하셔야 합니다", Toast.LENGTH_SHORT).show()
                        } else {
                            CoroutineScope(Dispatchers.IO).launch {
                                if (reportViewModel.pic.value != null) {
                                    report_S3_connect()
                                }

                                val reportUploadDto = ReportUploadDto(
                                    reportViewModel.documentId.value,
                                    reportViewModel.content.value,
                                    reportViewModel.opengraph_list.value,
                                    reportViewModel.tags.value,
                                    reportViewModel.images.value
                                )

                                reportViewModel.post_report(reportUploadDto)

                                finish()
                            }
                        }
                    }
                }
            }
        }

        //로그인 체크 서비스
//        viewModel.accessToken.observe(this) {
//            if (!it) {
//                startActivity(Intent(this, LoginActivity::class.java))
//                finish()
//                Toast.makeText(this, "로그인이 필요한 서비스 입니다", Toast.LENGTH_SHORT).show()
//            }
//        }
    }

    override fun onStart() {
        super.onStart()

        viewModel.checkAccessToken()
    }

    fun changeTitle(s: String) {
        binding.uploadTitleTv.text = s
    }

    /*S3 connect*/
    private fun tok_S3_connect(){

        for ((index,uri) in tokViewModel.pic.value!!.withIndex()) {

            val new_list = tokViewModel.images.value!![index].split("/")

            /**uri 변환*/
            val realPathFromURI = getRealPathFromURI(uri)
            val file = File(realPathFromURI)

            Log.d("new_list", new_list.toString())

            /**S3에 저장*/
            S3Util().getInstance()
                ?.setKeys(API.S3_ACCESS_KEY, API.S3_ACCESS_SECRET_KEY)
                ?.setRegion(Regions.AP_NORTHEAST_2)
                ?.uploadWithTransferUtility(
                    this,
                    "approval-please", file, new_list.get(new_list.size-1)
                )
        }
    }

    /*S3 connect*/
    private fun report_S3_connect(){

        for ((index,uri) in reportViewModel.pic.value!!.withIndex()) {

            val new_list = reportViewModel.images.value!![index].split("/")

            /**uri 변환*/
            val realPathFromURI = getRealPathFromURI(uri)
            val file = File(realPathFromURI)

            Log.d("new_list", new_list.toString())

            /**S3에 저장*/
            S3Util().getInstance()
                ?.setKeys(API.S3_ACCESS_KEY, API.S3_ACCESS_SECRET_KEY)
                ?.setRegion(Regions.AP_NORTHEAST_2)
                ?.uploadWithTransferUtility(
                    this,
                    "approval-please", file, new_list.get(new_list.size-1)
                )
        }
    }

    /*File Uri for S3 connect*/
    private fun getRealPathFromURI(uri: Uri): String {
        val buildName = Build.MANUFACTURER
        if(buildName.equals("Xiaomi")) {
            return uri.path.toString()
        }

        var columnIndex = 0
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        var cursor = contentResolver.query(uri, proj, null, null, null)

        if(cursor!!.moveToFirst()) {
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }

        return cursor.getString(columnIndex)
    }
}