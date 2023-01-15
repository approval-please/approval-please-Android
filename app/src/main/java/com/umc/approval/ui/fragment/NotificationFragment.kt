package com.umc.approval.ui.fragment

import Collie.NotificationActivityFragment
import Collie.NotificationDocumentFragment
import Collie.NotificationTotalFragment
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.umc.approval.databinding.FragmentNotificationBinding
import com.umc.approval.databinding.FragmentNotificationTotalBinding

/**
 * Notification View
 * */
class NotificationFragment : Fragment() {

    private var _binding : FragmentNotificationBinding? = null
    private val binding get() = _binding!!
    lateinit var tab1 : NotificationTotalFragment
    lateinit var tab2 : NotificationActivityFragment
    lateinit var tab3 : NotificationDocumentFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onStart() {
        super.onStart()

        tab1 = NotificationTotalFragment()
        tab2 = NotificationActivityFragment()
        tab3 = NotificationDocumentFragment()

        replaceView(tab1)

        binding.notificationTabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position){
                    0 -> { replaceView(tab1) }
                    1 -> { replaceView(tab2) }
                    2 -> { replaceView(tab3) }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
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