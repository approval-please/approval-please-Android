package com.umc.approval.ui.fragment.mypage.follow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.approval.databinding.FragmentFollowerBinding
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

        //초기 데이터 get
        viewModel.init_followers()

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