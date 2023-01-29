package com.umc.approval.ui.fragment.community

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import com.umc.approval.R
import com.umc.approval.databinding.FragmentCommunityBinding
import com.umc.approval.ui.activity.CommunityUploadActivity
import com.umc.approval.ui.adapter.community_fragment.CommunityVPAdapter

/**
 * Community View
 * */
class CommunityFragment : Fragment() {

    private var _binding : FragmentCommunityBinding? = null
    private val binding get() = _binding!!

    //view pager RV Adapter
    private lateinit var communityVPAdapter : CommunityVPAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentCommunityBinding.inflate(inflater, container, false)
        val view = binding.root

        //view pager와 탭 레이아웃 연결
        connect_view_pager()

        //add post click event
        click_add_post()

        return view
    }

    /**add post button click event*/
    private fun click_add_post() {
        binding.addPost.setOnClickListener {

            val bottomSheetView =
                layoutInflater.inflate(R.layout.community_upload_selector_dialog, null)
            val bottomSheetDialog = BottomSheetDialog(requireContext())
            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()

            //dialog의 view Component 접근
            val dialog_cancel = bottomSheetView.findViewById<ImageView>(R.id.cancel)
            val select_talk = bottomSheetView.findViewById<LinearLayout>(R.id.talk_talk)
            val select_report = bottomSheetView.findViewById<LinearLayout>(R.id.report)

            dialog_cancel!!.setOnClickListener {
                bottomSheetDialog.cancel()
            }

            select_talk!!.setOnClickListener {
                bottomSheetDialog.cancel()
                startActivity(Intent(requireContext(), CommunityUploadActivity::class.java))
            }

            select_report!!.setOnClickListener {
                bottomSheetDialog.cancel()
                startActivity(Intent(requireContext(), CommunityUploadActivity::class.java))
            }
        }
    }

    /**ViewPager Connect*/
    private fun connect_view_pager() {

        communityVPAdapter = CommunityVPAdapter(this)
        binding.viewPager.adapter = communityVPAdapter

        //탭 레이아웃 제목
        val tabTitleArray = arrayOf("결제톡톡", "결제보고서")

        //탭 레이아웃과 뷰페이저 연결
        TabLayoutMediator(binding.communityTab, binding.viewPager) { tab, position ->
            tab.text = tabTitleArray[position]
        }.attach()
    }

    /**
     * viewBinding이 더이상 필요 없을 경우 null 처리 필요
     */
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}