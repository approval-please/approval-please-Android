package com.umc.approval.ui.activity

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.approval.databinding.ActivityCommunityTokBinding
import com.umc.approval.ui.adapter.community_post_activity.CommunityCommentRVAdapter
import com.umc.approval.ui.adapter.community_post_activity.CommunityVoteRVAdapter
import com.umc.approval.util.CommentItem
import com.umc.approval.util.VoteItem

class CommunityTokActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCommunityTokBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCommunityTokBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setVoteList()
        setComment()

    }

    private fun setVoteList(){
        val voteList : ArrayList<VoteItem> = arrayListOf()

        voteList.apply{
            add(VoteItem("스벅ㄴㅇㄹㄴㅇㄹ", true,arrayListOf("dfs","fswedf")))
            add(VoteItem("sdfsdfsd",false, arrayListOf("dfs","fsdf")))
            add(VoteItem("스벅ㄴㅇㄹㄴㅇㄹ", true,arrayListOf("dfs","fswedf")))
            add(VoteItem("스벅ㄴㅇㄹㄴㅇㄹ", true,arrayListOf("dfs","fswedf")))
        }

        val dataRVAdapter = CommunityVoteRVAdapter(voteList)
        binding.voteItem.adapter = dataRVAdapter
        binding.voteItem.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

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

        }

//        val spaceDecoration = VerticalSpaceItemDecoration(50)
//        binding.commentItem.addItemDecoration(spaceDecoration)

        val dataRVAdapter = CommunityCommentRVAdapter(commentList)
        binding.commentItem.adapter = dataRVAdapter
        binding.commentItem.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

    }

    // 아이템 간 간격 조절 기능
    inner class VerticalSpaceItemDecoration(private val height: Int) :
        RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.left = height
        }
    }

}