package com.umc.approval.ui.fragment.mypage.follow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.umc.approval.databinding.FragmentFollowBinding
import com.umc.approval.databinding.FragmentFollowingBinding
import com.umc.approval.databinding.FragmentNotificationBinding
import com.umc.approval.ui.adapter.follow_fragment.FollowerAdapter
import com.umc.approval.ui.adapter.follow_fragment.FollowerItem
import com.umc.approval.ui.adapter.follow_fragment.FollowingAdapter
import com.umc.approval.ui.adapter.follow_fragment.FollowingItem

/**
 * FollowFragment Tablayout 아래에 표시될 팔로잉 목록 Fragment
 * */
class FollowingFragment : Fragment() {

    private var _binding : FragmentFollowingBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onStart() {
        super.onStart()
        binding.followingRecyclerview.layoutManager = LinearLayoutManager(this.context)
        val itemList = ArrayList<FollowingItem>()
        for(i in 0..5){
            itemList.add(FollowingItem("김부장", 0, true))
        }
        for(i in 6..10){
            itemList.add(FollowingItem("이차장", 1, false))
        }
        val followingAdapter = FollowingAdapter(itemList)
        binding.followingRecyclerview.adapter = followingAdapter
    }

    /**
     * viewBinding이 더이상 필요 없을 경우 null 처리 필요
     */
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}