package com.umc.approval.ui.adapter.document_comment_activity

data class DocumentCommentItem2 (val type: Int, val list : ArrayList<DocumentCommentItem> ){
    companion object{
        const val TYPE_1 = 0; // 본 댓글 아이템
        const val TYPE_2 = 1; // 대댓글 recyclerview 아이템
    }
}