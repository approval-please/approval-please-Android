package com.umc.approval.ui.adapter.document_comment_activity
// 기본 댓글 내용 데이터
data class DocumentCommentItem (val profileImg : String, val name : String, val rank : Int, val content : String, val date : String, val likes : Int,
                                val isWriter : Boolean?, val isLike : Boolean?)