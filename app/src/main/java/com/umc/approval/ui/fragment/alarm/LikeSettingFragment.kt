package com.umc.approval.ui.fragment.alarm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.umc.approval.R
import com.umc.approval.databinding.FragmentLikeSettingBinding

/**like fragment*/
class LikeSettingFragment : Fragment() {

    private var _binding : FragmentLikeSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLikeSettingBinding.inflate(inflater, container, false)
        val view = binding.root

        /**alarm fragment로 back*/
        binding.backToAlarmFragment.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_likeSettingFragment_to_alarmSettingFragment)
        }

        return view
    }
}