package com.umc.approval.ui.activity

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.umc.approval.R
import com.umc.approval.data.dto.opengraph.OpenGraphDto
import com.umc.approval.databinding.ActivityCommunityRemovePostDialogBinding
import com.umc.approval.databinding.ActivityCommunityReportBinding
import com.umc.approval.databinding.ActivityCommunityReportPostDialogBinding
import com.umc.approval.databinding.ActivityCommunityReportUserDialogBinding
import com.umc.approval.ui.adapter.community_post_activity.CommunityCommentRVAdapter
import com.umc.approval.ui.adapter.community_post_activity.CommunityImageRVAdapter
import com.umc.approval.ui.adapter.community_post_activity.CommunityVoteCompleteRVAdapter
import com.umc.approval.ui.adapter.community_upload_activity.CommunityUploadLinkItemRVAdapter
import com.umc.approval.ui.adapter.upload_activity.UploadHashtagRVAdapter
import com.umc.approval.util.CommentItem
import com.umc.approval.util.VoteItem

class CommunityReportActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCommunityReportBinding

    /*다이얼로그*/
    private lateinit var activityCommunityReportPostDialogBinding: ActivityCommunityReportPostDialogBinding
    private lateinit var activityCommunityReportUserDialogBinding: ActivityCommunityReportUserDialogBinding
    private lateinit var activityCommunityRemovePostDialogBinding: ActivityCommunityRemovePostDialogBinding

    /*다이얼로그 버튼*/
    private lateinit var dialogCancelButton : Button
    private lateinit var dialogConfirmButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityReportBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        /*set RecyclerView*/
        setComment()
        setHashTag()
        setLink()
        setImage()

        /*set Content*/
        setInfo()
        setDocument()

        /*setting*/
        post_more()


    }

    /**post more*/
    private fun post_more() {
        val writer = 0
        val notice = 0
        val storage = 0

        binding.uploadSettingBtn.setOnClickListener {

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
            val setting_edit_post = bottomSheetView.findViewById<LinearLayout>(R.id.setting_edit_post)

            // visible 처리
            if(writer == 1){
                setting_report_post.isVisible = false
                setting_report_user.isVisible = false
                setting_remove_post.isVisible = true
                setting_edit_post.isVisible = true
            }else{
                setting_report_post.isVisible = true
                setting_report_user.isVisible = true
                setting_remove_post.isVisible = false
                setting_edit_post.isVisible = false
            }

            if(notice == 0){
                setting_notice_on.isVisible = true
                setting_notice_off.isVisible = false
            }else{
                setting_notice_on.isVisible = false
                setting_notice_off.isVisible = true
            }

            if(storage == 0){
                setting_storage_on.isVisible = true
                setting_storage_off.isVisible = false
            }else{
                setting_storage_on.isVisible = false
                setting_storage_off.isVisible = true
            }

            // 다이얼로그 클릭 이벤트
            setting_notice_on!!.setOnClickListener {
                bottomSheetDialog.cancel()
            }

            setting_notice_off!!.setOnClickListener {
                bottomSheetDialog.cancel()
            }

            setting_storage_on!!.setOnClickListener {
                bottomSheetDialog.cancel()
            }

            setting_storage_off!!.setOnClickListener {
                bottomSheetDialog.cancel()
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

            setting_edit_post!!.setOnClickListener {
                bottomSheetDialog.cancel()
            }

        }
    }

    private fun showRemovePostDialog(){
        val linkDialog : Dialog = Dialog(this);
        activityCommunityRemovePostDialogBinding = ActivityCommunityRemovePostDialogBinding.inflate(layoutInflater)

        linkDialog.setContentView(activityCommunityRemovePostDialogBinding.root)
        linkDialog.setCanceledOnTouchOutside(true)
        linkDialog.setCancelable(true)
        dialogCancelButton = activityCommunityRemovePostDialogBinding.communityDialogCancelButton
        dialogConfirmButton = activityCommunityRemovePostDialogBinding.communityDialogConfirmButton

        /*취소버튼*/
        dialogCancelButton.setOnClickListener {
            linkDialog.dismiss()
        }

        /*확인버튼*/
        dialogConfirmButton.setOnClickListener{
            linkDialog.dismiss()
        }
        /*link 팝업*/
        linkDialog.show()
    }

    private fun showReportUserDialog(){
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
            linkDialog.dismiss()
        }

        /*link 팝업*/
        linkDialog.show()
    }

    private fun showReportPostDialog(){
        val linkDialog : Dialog = Dialog(this);
        activityCommunityReportPostDialogBinding = ActivityCommunityReportPostDialogBinding.inflate(layoutInflater)

        linkDialog.setContentView(activityCommunityReportPostDialogBinding.root)
        linkDialog.setCanceledOnTouchOutside(true)
        linkDialog.setCancelable(true)
        dialogCancelButton = activityCommunityReportPostDialogBinding.communityDialogCancelButton
        dialogConfirmButton = activityCommunityReportPostDialogBinding.communityDialogConfirmButton

        /*취소버튼*/
        dialogCancelButton.setOnClickListener {
            linkDialog.dismiss()
        }

        /*확인버튼*/
        dialogConfirmButton.setOnClickListener{
            linkDialog.dismiss()
        }

        /*link 팝업*/
        linkDialog.show()
    }

    private fun setDocument(){
        // 좋아요, 스크랩, 조회수 설정
        binding.communityDocumentLayout.documentTitle.text = "스타벅스 텀블러" // 제목
        binding.communityDocumentLayout.documentContent.text = "내용내용내용내용내용내용" // 내용

        val tagArray : ArrayList<String> = arrayListOf()

        tagArray.apply{
            add("야호")
            add("화이팅")
        }

        if(tagArray.size == 0) binding.communityDocumentLayout.documentHashtagItem.isVisible = false
        val dataRVAdapter = UploadHashtagRVAdapter(tagArray)
        binding.communityDocumentLayout.documentHashtagItem.adapter = dataRVAdapter
        binding.communityDocumentLayout.documentHashtagItem.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)

        binding.communityDocumentLayout.documentImageCountTv.text = "+1"
        binding.communityDocumentLayout.ivApprovalReportThumbnail.load("http://www.ccdn.co.kr/news/photo/201904/571329_224596_3722.jpg")

//        binding.communityDocumentLayout.ivApprovalReportThumbnail.isVisible = false
//        binding.communityDocumentLayout.documentImageCountTv.isVisible = false

        binding.communityDocumentLayout.documentBtn.setOnClickListener{
            startActivity(Intent(this@CommunityReportActivity,DocumentActivity::class.java))
        }

    }

    private fun setInfo(){
        // 좋아요, 스크랩, 조회수 설정
        binding.communityPostLikeNum.text = "좋아요 "+"5"
        binding.communityPostScrapNum.text = "스크랩 "+"3"
        binding.communityPostVisitorsNum.text = "조회수 "+"123"

        binding.communityPostLikeNum.setOnClickListener{
            startActivity(Intent(this@CommunityReportActivity,LikeActivity::class.java))
        }
    }

    private fun setImage(){
        val imageList :ArrayList<String> = ArrayList()
        imageList.apply{
            add("https://w.namu.la/s/d1c8d62624a29e62662cf946385dd0d342a09bcde317f844b1aed84f15981896e4cf8233787ecaf4a6b3f99493a55755222422cf7d0a9bcbd05b824132de155e3a50f424274f2d0a3cb4b9660b05ca063d4a18258a148ed2083cf7dec61f008cfe0cb7b097dea539483847787be7b5f8")
            add("https://www.sports-g.com/wp-content/uploads/2019/03/%ED%95%98%EC%9D%B4%EB%9D%BC%EC%9D%B4%ED%8A%B8.jpg")
            add("https://img.mbn.co.kr/filewww/news/other/2022/03/14/001800002020.jpg")
        }

        if(imageList.size == 0) binding.imageRv.isVisible = false;
        var imageRVAdapter = CommunityImageRVAdapter(imageList)
        binding.imageRv.adapter = imageRVAdapter
        binding.imageRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setLink(){
        val linkList : ArrayList<OpenGraphDto> = ArrayList()

        var openGraphDto : OpenGraphDto = OpenGraphDto()

        openGraphDto.url = "naver.com"
        openGraphDto.title = "naver"
        openGraphDto.image = ""

        linkList.apply{
            add(openGraphDto)
            add(openGraphDto)
            add(openGraphDto)

        }
        val dataRVAdapter = CommunityUploadLinkItemRVAdapter(linkList)
        binding.uploadLinkItem.adapter = dataRVAdapter
        binding.uploadLinkItem.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
    }

    private fun setHashTag(){
        val tagArray : ArrayList<String> = arrayListOf()

        tagArray.apply{
            add("기계")
            add("핸드폰")
        }

        if(tagArray.size == 0) binding.uploadHashtagItem.isVisible = false

        val dataRVAdapter = UploadHashtagRVAdapter(tagArray)
        binding.uploadHashtagItem.adapter = dataRVAdapter
        binding.uploadHashtagItem.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)

    }

//    data class CommentItem(
//        val id:Int,
//        val user_nickname: String,
//        val user_rank :String,
//        val content:String,
//        val date : String,
//        val like : Int,
//        val replyComment : Int,
//    )

    private fun setComment(){
        val commentList : ArrayList<CommentItem> = arrayListOf()

        commentList.apply{
            add(CommentItem(1,"김차장","","ㅇㄹㅇㄴㄹㄴㄹㄴㅇㄹㅇㅇㄴㄹㅇㄴㄹ","2012.01.02",5,0))
            add(CommentItem(2,"김과장","","ㅈㄷㅈㄷㄱㅈㄷㄱㄴㄹㄴㅇㄹㅇㅇㄴㄹㅇㄴㄹ","2012.01.02",5,0))
            add(CommentItem(2,"김과장","","ㅈㄷㅈㄷㄱㅈㄷㄱㄴㄹㄴㅇㄹㅇㅇㄴㄹㅇㄴㄹ","2012.01.02",5,0))
            add(CommentItem(2,"김과장","","ㅈㄷㅈㄷㄱㅈㄷㄱㄴㄹㄴㅇㄹㅇㅇㄴㄹㅇㄴㄹ","2012.01.02",5,0))
            add(CommentItem(2,"김과장","","ㅈㄷㅈㄷㄱㅈㄷㄱㄴㄹㄴㅇㄹㅇㅇㄴㄹㅇㄴㄹ","2012.01.02",5,0))
            add(CommentItem(2,"김과장","","ㅈㄷㅈㄷㄱㅈㄷㄱㄴㄹㄴㅇㄹㅇㅇㄴㄹㅇㄴㄹ","2012.01.02",5,0))

        }

        val dataRVAdapter = CommunityCommentRVAdapter(commentList)
        binding.commentItem.adapter = dataRVAdapter
        binding.commentItem.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }


}