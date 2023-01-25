package com.umc.approval.ui.fragment.notification

import com.umc.approval.ui.adapter.notification_fragment.NotificationAdapter
import com.umc.approval.ui.adapter.notification_fragment.NotificationItem
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.approval.databinding.*

/*
알림 페이지 문서 탭 Fragment
*/
class NotificationDocumentFragment : Fragment() {

    private var _binding : FragmentNotificationDocumentBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentNotificationDocumentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onStart() {
        super.onStart()
        binding.notificationDocumentRecyclerview.layoutManager = LinearLayoutManager(this.context)
        val itemList = ArrayList<NotificationItem>()
        for(i in 0..10) {
            itemList.add(
                NotificationItem(
                    NotificationItem.TYPE_3,
                    "김차장",
                    "님이 내 결재 서류를 승인했습니다.",
                    "승인 내용 예시 텍스트 abcdefghijklmnopqrstuv",
                    "2023.01.14"
                )
            )
            itemList.add(
                NotificationItem(
                    NotificationItem.TYPE_4,
                    "최사원",
                    "님이 내 결재 서류를 반려했습니다.",
                    "반려 내용 예시 텍스트 abcdefghijklmnopqrstuv",
                    "2023.01.14"
                )
            )
        }

        val notificationAdapter = NotificationAdapter(itemList)
        notificationAdapter.notifyDataSetChanged()

        binding.notificationDocumentRecyclerview.adapter = notificationAdapter
    }

    /**
     * viewBinding이 더이상 필요 없을 경우 null 처리 필요
     */
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}