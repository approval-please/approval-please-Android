package com.umc.approval.util

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.umc.approval.databinding.BlackToastMessageBinding

object BlackToast {
    fun createToast(context: Context, message: String): Toast {
        val inflater = LayoutInflater.from(context)
        val binding = BlackToastMessageBinding.inflate(inflater, null, false)

        binding.tvToastMessage.text = message

        // 좌우 패딩 간격 구하기
        val display = context.applicationContext.resources.displayMetrics
        val deviceWidth = display.widthPixels
        binding.layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val messageWidth = binding.layout.measuredWidth
        val padding = (deviceWidth - messageWidth - dpToPx(context, 32)) / 2 - dpToPx(context, 5)

        binding.tvToastMessage.setPadding(padding, dpToPx(context, 10), padding, dpToPx(context, 10))

        return Toast(context).apply {
            setGravity(Gravity.BOTTOM or Gravity.CENTER, 0, dpToPx(context, 73))
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