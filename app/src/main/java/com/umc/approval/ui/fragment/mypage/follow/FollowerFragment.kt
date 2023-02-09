package com.umc.approval.ui.fragment.mypage.follow

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.approval.check.collie.OtherpageActivity
import com.umc.approval.data.dto.community.get.CommunityReport
import com.umc.approval.data.dto.mypage.FollowDto
import com.umc.approval.databinding.FragmentFollowerBinding
import com.umc.approval.ui.activity.CommunityReportActivity
import com.umc.approval.ui.activity.DocumentActivity
import com.umc.approval.ui.adapter.community_fragment.CommunityReportItemRVAdapter
import com.umc.approval.ui.adapter.follow_fragment.FollowerAdapter
import com.umc.approval.ui.viewmodel.follow.FollowViewModel

/**
 * FollowFragment Tablayout 아래에 표시될 팔로워 목록 Fragment
 * */
class FollowerFragment : Fragment() {

    private var _binding : FragmentFollowerBinding? = null
    private val binding get() = _binding!!

    /**login view model*/
    private val viewModel by viewModels<FollowViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentFollowerBinding.inflate(inflater, container, false)
        val view = binding.root

        //라이브데이터 변경 감징
        live_data()

        //텍스트 입력시마다 서버에 연결
        viewModel.my_followers()

        return view
    }

    /**followers live data*/
    private fun live_data() {
        viewModel.followers.observe(viewLifecycleOwner) {
            binding.followerRecyclerview.layoutManager = LinearLayoutManager(this.context)
            val followerAdapter = FollowerAdapter(it.followDto)
            binding.followerRecyclerview.adapter = followerAdapter

            followerAdapter.itemClick = object : FollowerAdapter.ItemClick {

                override fun follow_or_not(v: View, data: FollowDto, pos: Int) {
                    viewModel.follow(data.userId)
                }

                override fun other(v: View, data: FollowDto, pos: Int) {
                    val intent = Intent(requireContext(), OtherpageActivity::class.java)
                    intent.putExtra("userId", data.userId)
                    startActivity(intent)
                }
            }
        }

        viewModel.isFollow.observe(viewLifecycleOwner) {
            viewModel.my_followers()
        }
    }

    /**
     * viewBinding이 더이상 필요 없을 경우 null 처리 필요
     */
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}