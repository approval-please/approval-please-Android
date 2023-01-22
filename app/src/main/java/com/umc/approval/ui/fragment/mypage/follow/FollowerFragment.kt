package com.umc.approval.ui.fragment.mypage.follow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.umc.approval.databinding.FragmentFollowBinding
import com.umc.approval.databinding.FragmentFollowerBinding
import com.umc.approval.databinding.FragmentNotificationBinding
import com.umc.approval.ui.adapter.follow_fragment.FollowerAdapter
import com.umc.approval.ui.adapter.follow_fragment.FollowerItem

/**
 * FollowFragment Tablayout 아래에 표시될 팔로워 목록 Fragment
 * */
class FollowerFragment : Fragment() {

    private var _binding : FragmentFollowerBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentFollowerBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onStart() {
        super.onStart()
        binding.followerRecyclerview.layoutManager = LinearLayoutManager(this.context)
        val itemList = ArrayList<FollowerItem>()
        for(i in 0..5){
            itemList.add(FollowerItem("김부장", 0))
        }
        for(i in 6..10){
            itemList.add(FollowerItem("이차장", 1))
        }
        val followerAdapter = FollowerAdapter(itemList)
        binding.followerRecyclerview.adapter = followerAdapter
    }

    /**
     * viewBinding이 더이상 필요 없을 경우 null 처리 필요
     */
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}