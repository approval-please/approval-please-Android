package com.umc.approval.ui.fragment.mypage.follow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.google.android.material.tabs.TabLayout
import com.umc.approval.R
import com.umc.approval.databinding.FragmentFollowBinding
import com.umc.approval.databinding.FragmentNotificationBinding
import com.umc.approval.ui.viewmodel.follow.FollowViewModel

/**
 * 팔로우 / 팔로잉 목록 Fragment View
 * */
class FollowFragment : Fragment() {

    private var _binding : FragmentFollowBinding? = null
    private val binding get() = _binding!!
    lateinit var tab1 : FollowerFragment
    lateinit var tab2 : FollowingFragment

    /**Follow view model*/
    private val viewModel by viewModels<FollowViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel.my_followings()

        viewModel.followings.observe(viewLifecycleOwner) {
            binding.followNickname.setText(it.nickname.toString())
        }

        return view
    }

    override fun onStart() {
        super.onStart()

        tab1 = FollowerFragment()
        tab2 = FollowingFragment()

        replaceView(tab1)

        binding.followTabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position){
                    0 -> { replaceView(tab1) }
                    1 -> { replaceView(tab2) }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
        binding.followBackarrow.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_followFragment_to_fragment_mypage)
        }
    }

    /**
     * viewBinding이 더이상 필요 없을 경우 null 처리 필요
     */
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    // 탭 변경 함수
    private fun replaceView(tab: Fragment){
        var selectedFragment: Fragment? = null
        selectedFragment = tab
        selectedFragment.let {
            activity?.supportFragmentManager?.beginTransaction()!!
                .replace(binding.notificationTaparea.id, it).commit()
        }
    }
}