package com.umc.approval.check.collie
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.umc.approval.databinding.FragmentOtherpageBinding
import com.umc.approval.databinding.FragmentOtherpageCommunityBinding
import com.umc.approval.ui.fragment.mypage.MypageCommunityFragment
import com.umc.approval.ui.fragment.mypage.MypageDocumentFragment

/*
 MyPage View
 */
class OtherpageCommunityFragment : Fragment() {

    private var _binding : FragmentOtherpageCommunityBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentOtherpageCommunityBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onStart() {
        super.onStart()

    }

    /**
     * viewBinding이 더이상 필요 없을 경우 null 처리 필요
     */
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}