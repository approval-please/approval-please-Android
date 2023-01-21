package com.umc.approval.ui.fragment.mypage.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.umc.approval.databinding.FragmentFollowBinding
import com.umc.approval.databinding.FragmentNotificationBinding
import com.umc.approval.databinding.FragmentSettingBinding

/**
 * 내 정보 > 설정 Fragment View
 * */
class SettingFragment : Fragment() {

    private var _binding : FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onStart() {
        super.onStart()
        binding.settingIconLeftarrow.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
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