package com.umc.approval.ui.fragment.approval

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.umc.approval.R
import com.umc.approval.databinding.FragmentApprovalPaperListBinding
import com.umc.approval.ui.adapter.approval_fragment.ApprovalVPAdapter

class ApprovalPaperListFragment: Fragment() {
    private var _binding : FragmentApprovalPaperListBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentApprovalPaperListBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val approvalTabVPAdapter = ApprovalVPAdapter(this)

        binding.vpApprovalPaperList.adapter = approvalTabVPAdapter

        val tabTitleArray = arrayOf(
            "전체",
            "관심",
        )

        // 탭 레이아웃과 뷰페이저 연결
        TabLayoutMediator(binding.tabLayout, binding.vpApprovalPaperList) { tab, position ->
            tab.text = tabTitleArray[position]
        }.attach()

        // 상태 선택 스피너
        val statusSpinnerList = arrayOf(
            "전체",
            "승인완료",
            "승인대기중",
            "승인됨",
            "반려됨",
        )

        val statusSpinner = binding.spStatus
        statusSpinner.adapter = SpinnerAdapter(statusSpinnerList)
        statusSpinner.onItemSelectedListener = StatusSpinnerActivity()

        // 정렬 방식 선택 스피너
        val sortSpinnerList = arrayOf(
            "최신순",
            "인기순",
        )

        val sortSpinner = binding.spSort
        sortSpinner.adapter = SpinnerAdapter(sortSpinnerList)
        statusSpinner.onItemSelectedListener = SortSpinnerActivity()
    }

    /**
     * viewBinding이 더이상 필요 없을 경우 null 처리 필요
     */
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    inner class StatusSpinnerActivity : Activity(), AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
            when (pos) {
                0 -> {
                    // 스피너 클릭 이벤트 정의
                    Log.d("로그", "스피너 아이템 클릭")
                }
                1 -> {
                    Log.d("로그", "스피너 아이템 클릭")
                }
                else -> {
                    Log.d("로그", "스피너 아이템 클릭")
                }
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>) {
            // Another interface callback
        }
    }

    inner class SortSpinnerActivity : Activity(), AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
            when (pos) {
                0 -> {
                    // 스피너 클릭 이벤트 정의
                    Log.d("로그", "스피너 아이템 클릭")
                }
                1 -> {
                    Log.d("로그", "스피너 아이템 클릭")
                }
                else -> {
                    Log.d("로그", "스피너 아이템 클릭")
                }
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>) {
            // Another interface callback
        }
    }

    inner class SpinnerAdapter(spinnerItem: Array<String>): BaseAdapter() {
        val context = requireContext()
        val data = spinnerItem
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        override fun getCount() = data.size

        override fun getItem(position: Int): Any {
            return data[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        // 스피너 클릭 전에 보여지는 레이아웃
        @SuppressLint("ViewHolder", "MissingInflatedId")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var rootView = LayoutInflater.from(context).inflate(R.layout.approval_fragment_spinner_custom, parent, false)
            val text = rootView.findViewById<TextView>(R.id.spinner_item_text)
            val icon = rootView.findViewById<ImageView>(R.id.spinner_arrow)

            text.text = data[position]
            icon.setImageResource(R.drawable.approval_fragment_spinner_arrow)

            return rootView
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var rootView = LayoutInflater.from(context).inflate(R.layout.approval_fragment_spinner_item, parent, false)
            val text = rootView.findViewById<TextView>(R.id.spinner_item_text)

            text.text = data[position]

            return rootView
        }

    }
}