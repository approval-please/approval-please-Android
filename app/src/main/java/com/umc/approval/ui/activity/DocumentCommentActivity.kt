package com.umc.approval.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.umc.approval.databinding.ActivityDocumentCommentBinding

class DocumentCommentActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDocumentCommentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDocumentCommentBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}