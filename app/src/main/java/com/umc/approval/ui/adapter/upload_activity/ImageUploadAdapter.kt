package com.umc.approval.ui.adapter.upload_activity

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.umc.approval.databinding.UploadImageItemBinding

class ImageUploadAdapter(private val items : List<Uri>) : RecyclerView.Adapter<ImageUploadAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: UploadImageItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun binding(uri: Uri) {
            val sting = uri.toString()
            if (sting == "") {
                binding.cancel.isVisible = false
            } else {
                binding.image.load(uri)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = UploadImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}