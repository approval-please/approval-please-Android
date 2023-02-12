package com.umc.approval.ui.adapter.document_comment_activity

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.umc.approval.App
import com.umc.approval.R
import com.umc.approval.data.dto.comment.get.CommentDto
import com.umc.approval.data.dto.comment.get.CommentListDto
import com.umc.approval.databinding.ActivityCommunityRemovePostDialogBinding
import com.umc.approval.databinding.ActivityCommunityReportPostDialogBinding
import com.umc.approval.databinding.ActivityCommunityReportUserDialogBinding
import com.umc.approval.databinding.DocumentCommentRecyclerviewItemBinding
import com.umc.approval.ui.viewmodel.comment.CommentViewModel
import com.umc.approval.ui.viewmodel.community.CommunityViewModel
import com.umc.approval.ui.viewmodel.follow.FollowViewModel
import com.umc.approval.util.Utils
import com.umc.approval.util.Utils.level

class ParentCommentAdapter(val itemList : CommentListDto, val context: Context,
                           val layoutInflater: LayoutInflater, val writer: Boolean?=false,
                            val followViewModel: FollowViewModel, val commentViewModel: CommentViewModel,
                            val documentId: String?=null, val toktokId:String?=null, val reportId:String?=null) : RecyclerView.Adapter<ParentCommentAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: DocumentCommentRecyclerviewItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun binding(data: CommentDto) {

            //자식 댓글 처리
            if(data.childComment == null || data.childComment.isEmpty()){
                binding.child.isVisible = false
            }else{
                val dataRVAdapter = ChildCommentAdapter(data.childComment, context)
                binding.child.adapter = dataRVAdapter
                binding.child.layoutManager = LinearLayoutManager(App.context(), RecyclerView.VERTICAL, false)

                /**대댓글 다이얼로그 로직*/
                dataRVAdapter.itemClick = object : ChildCommentAdapter.ItemClick {

                    override fun like(v: View, data: CommentDto, pos: Int) {
                        followViewModel.like(commentId = data.commentId)
                    }

                    override fun setting_comment(v: View, data: CommentDto, pos: Int, context: Context) {

                        val bottomSheetView =
                            layoutInflater.inflate(R.layout.community_comment_selector_dialog, null)
                        val bottomSheetDialog = BottomSheetDialog(context)
                        bottomSheetDialog.setContentView(bottomSheetView)
                        bottomSheetDialog.show()

                        //dialog의 view Component 접근
                        val setting_report_post = bottomSheetView.findViewById<LinearLayout>(R.id.setting_report_post)
                        val setting_report_user = bottomSheetView.findViewById<LinearLayout>(R.id.setting_report_user)
                        val setting_remove_post = bottomSheetView.findViewById<LinearLayout>(R.id.setting_remove_post)

                        // visible 처리
                        if(data.isMy == true){
                            setting_report_post.isVisible = false
                            setting_report_user.isVisible = false
                            setting_remove_post.isVisible = true
                        }else{
                            setting_report_post.isVisible = true
                            setting_report_user.isVisible = true
                            setting_remove_post.isVisible = false
                        }

                        setting_report_post!!.setOnClickListener {
                            showReportCommentDialog(data.commentId)
                            bottomSheetDialog.cancel()
                        }

                        setting_report_user!!.setOnClickListener {
                            showReportCommentUserDialog(data.userId)
                            bottomSheetDialog.cancel()
                        }

                        setting_remove_post!!.setOnClickListener {
                            showRemoveCommentDialog(data.commentId)
                            bottomSheetDialog.cancel()
                        }
                    }
                }
            }

            //프로필 이미지 있을시
            if (data.profileImage != null) {
                binding.documentCommentItemProfilepic.load(data.profileImage)
            } else {
                binding.documentCommentItemProfilepic.load(Utils.levelImage[data.level])
            }

            //닉네임
            binding.documentCommentItemName.text = data.nickname

            //직급
            binding.documentCommentItemRank.text = level[data.level]

            //내용
            binding.documentCommentItemContent.text = data.content

            //날짜
            binding.documentCommentItemDate.text = data.datetime

            //작성자일시
            if(data.isWriter == true){
                binding.documentCommentItemRank.text = "글쓴이"
                binding.documentCommentItemRank.setBackgroundResource(R.drawable.writer_background)
                binding.documentCommentItemRank.setTextColor(Color.parseColor("#6C39FF"))
                binding.documentCommentItemName.setTextColor(Color.parseColor("#6C39FF"))
            }

            //라이크
            if(data.isLike == true){
                binding.documentCommentItemLikebtn.setImageResource(R.drawable.document_comment_recyclerview_icon_like_selected)
            }

            binding.documentCommentItemLikes.text = data.likeCount.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = DocumentCommentRecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(itemList.content[position])

        if (itemClick != null){
            holder.binding.checkBtn.setOnClickListener(View.OnClickListener {
                itemClick?.make_chid_comment(it, itemList.content[position], position)
            })

            //라이크
            holder.binding.documentCommentItemLikebtn.setOnClickListener(View.OnClickListener {
                itemClick?.like(it, itemList.content[position], position)
            })

            /**대댓글 다이얼로그 로직*/
            holder.binding.setting.setOnClickListener(View.OnClickListener {
                itemClick?.setting_comment(it, itemList.content[position], position, context)
            })
        }
    }

    /**RV item click event*/
    interface ItemClick{ //인터페이스
        fun make_chid_comment(v: View, data: CommentDto, pos: Int)
        fun setting_comment(v: View, data: CommentDto, pos: Int, context: Context)
        fun like(v: View, data: CommentDto, pos: Int)
    }

    var itemClick: ItemClick? = null

    override fun getItemCount(): Int {
        return itemList.content.size
    }

    /**대댓글 다이얼로그 로직*/
    private fun showRemoveCommentDialog(commentId: Int) {
        val linkDialog : Dialog = Dialog(context);
        val activityCommunityRemovePostDialogBinding = ActivityCommunityRemovePostDialogBinding.inflate(layoutInflater)

        linkDialog.setContentView(activityCommunityRemovePostDialogBinding.root)
        linkDialog.setCanceledOnTouchOutside(true)
        linkDialog.setCancelable(true)
        val dialogCancelButton = activityCommunityRemovePostDialogBinding.communityDialogCancelButton
        val dialogConfirmButton = activityCommunityRemovePostDialogBinding.communityDialogConfirmButton

        val text = activityCommunityRemovePostDialogBinding.communityDialog
        text.setText("이 댓글을 삭제하시겠습니까?")

        dialogConfirmButton.setText("댓글 삭제하기")

        /*취소버튼*/
        dialogCancelButton.setOnClickListener {
            linkDialog.dismiss()
        }

        /*확인버튼*/
        dialogConfirmButton.setOnClickListener{
            linkDialog.dismiss()
            commentViewModel.delete_comments(commentId = commentId.toString(),
                documentId = documentId, toktokId = toktokId, reportId = reportId)
            linkDialog.dismiss()
        }
        /*link 팝업*/
        linkDialog.show()
    }

    //사용자 신고
    private fun showReportCommentUserDialog(userId: Int) {
        val linkDialog : Dialog = Dialog(context);
        val activityCommunityReportUserDialogBinding = ActivityCommunityReportUserDialogBinding.inflate(layoutInflater)

        linkDialog.setContentView(activityCommunityReportUserDialogBinding.root)
        linkDialog.setCanceledOnTouchOutside(true)
        linkDialog.setCancelable(true)
        val dialogCancelButton = activityCommunityReportUserDialogBinding.communityDialogCancelButton
        val dialogConfirmButton = activityCommunityReportUserDialogBinding.communityDialogConfirmButton

        val text = activityCommunityReportUserDialogBinding.communityDialog
        text.setText("이 사용자를 신고하시겠습니까?")

        dialogConfirmButton.setText("사용자 신고하기")

        /*취소버튼*/
        dialogCancelButton.setOnClickListener {
            linkDialog.dismiss()
        }

        /*확인버튼*/
        dialogConfirmButton.setOnClickListener{
            followViewModel.accuse(accuseUserId = userId)
            linkDialog.dismiss()
        }

        /*link 팝업*/
        linkDialog.show()
    }

    //댓글 신고
    private fun showReportCommentDialog(commentId: Int) {
        val linkDialog : Dialog = Dialog(context);
        val activityCommunityReportPostDialogBinding = ActivityCommunityReportPostDialogBinding.inflate(layoutInflater)

        linkDialog.setContentView(activityCommunityReportPostDialogBinding.root)
        linkDialog.setCanceledOnTouchOutside(true)
        linkDialog.setCancelable(true)
        val dialogCancelButton = activityCommunityReportPostDialogBinding.communityDialogCancelButton
        val dialogConfirmButton = activityCommunityReportPostDialogBinding.communityDialogConfirmButton

        val text = activityCommunityReportPostDialogBinding.communityDialog
        text.setText("이 댓글을 신고하시겠습니까?")

        dialogConfirmButton.setText("댓글 신고하기")

        /*취소버튼*/
        dialogCancelButton.setOnClickListener {
            linkDialog.dismiss()
        }

        /*확인버튼*/
        dialogConfirmButton.setOnClickListener{
            followViewModel.accuse(commentId = commentId)
            linkDialog.dismiss()
        }

        /*link 팝업*/
        linkDialog.show()
    }
}