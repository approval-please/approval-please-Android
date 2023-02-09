package com.umc.approval.check.collie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.forEachIndexed
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.umc.approval.R
import com.umc.approval.databinding.ActivityOtherpageBinding

class OtherpageActivity : AppCompatActivity() {
    lateinit var binding: ActivityOtherpageBinding
    lateinit var tab1 : OtherpageDocumentFragment
    lateinit var tab2 : OtherpageCommunityFragment
    var userpoint = 100.0f
    var rankpoint = 250.0f
    var progress = userpoint / rankpoint * 100.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtherpageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }

    // 시작 시 로그인 상태 검증 및 좋아요 누른 유저 목록 조회
    override fun onStart() {
        super.onStart()
        tab1 = OtherpageDocumentFragment()
        tab2 = OtherpageCommunityFragment()
        /* 첫 번째 탭 선택 후 font bold 처리, 해당 Fragment 표시 */
        val viewGroup = binding.otherpageTabLayout.getChildAt(0) as ViewGroup
        val viewGroupTab = viewGroup.getChildAt(0) as ViewGroup
        viewGroupTab.forEachIndexed { index, view ->
            val tabViewChild = viewGroupTab.getChildAt(index)
            if (tabViewChild is TextView){
                tabViewChild.typeface = ResourcesCompat.getFont(this, R.font.bold)
            }
        }
        replaceView(tab1)
        binding.pointNum1.text = userpoint.toInt().toString()
        binding.pointNum2.text = " / " + rankpoint.toInt().toString()
        binding.otherpageProgressbar.progress = progress.toInt()

        binding.otherpageTabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                /* 선택된 탭의 font bold로 처리 */
                val viewGroupTab = viewGroup.getChildAt(tab?.position!!.toInt()) as ViewGroup
                viewGroupTab.forEachIndexed { index, view ->
                    val tabViewChild = viewGroupTab.getChildAt(index)
                    if (tabViewChild is TextView){
                        tabViewChild.typeface = ResourcesCompat.getFont(this@OtherpageActivity, R.font.bold)
                    }
                }
                when(tab?.position){
                    0 -> {
                        replaceView(tab1)
                    }
                    1 -> {
                        replaceView(tab2)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                /* 미선택된 탭의 font 다시 medium으로 돌아가도록 처리 */
                val viewGroup = binding.otherpageTabLayout.getChildAt(0) as ViewGroup
                val viewGroupTab = viewGroup.getChildAt(tab?.position!!.toInt()) as ViewGroup
                viewGroupTab.forEachIndexed{ index, view ->
                    val tabViewChild = viewGroupTab.getChildAt(index)
                    if (tabViewChild is TextView){
                        tabViewChild.typeface = ResourcesCompat.getFont(this@OtherpageActivity, R.font.medium)
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
        binding.otherpageProgressbar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                binding.otherpageProgressbar.progress = progress.toInt()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })
    }
    // 탭 변경 함수
    private fun replaceView(tab: Fragment){
        var selectedFragment: Fragment? = null
        selectedFragment = tab
        selectedFragment.let {
            supportFragmentManager?.beginTransaction()!!
                .replace(binding.otherpageTabArea.id, it).commit()
        }
    }
}