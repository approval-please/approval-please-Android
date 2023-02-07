package com.umc.approval.util

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.umc.approval.databinding.BlackToastMessageBinding
import com.umc.approval.databinding.GrayToastMessageBinding


object GrayToast {
    fun createToast(context: Context, message: String): Toast {
        val inflater = LayoutInflater.from(context)
        val binding = GrayToastMessageBinding.inflate(inflater, null, false)

        binding.tvToastMessage.text = message

        return Toast(context).apply {
            setGravity(Gravity.BOTTOM or Gravity.CENTER, 0, dpToPx(context, 98))
            duration = Toast.LENGTH_SHORT
            view = binding.root
        }
    }

    private fun dpToPx(context: Context, dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }
}