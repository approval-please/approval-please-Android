package com.umc.approval.ui.adapter.home_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umc.approval.databinding.FragmentHomeBannerFrameBinding

class BannerVPAdapter(private val photoList: List<Int>): RecyclerView.Adapter<BannerVPAdapter.PhotoViewHolder>(){
    inner class PhotoViewHolder(private val binding: FragmentHomeBannerFrameBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(photoUrl: Int) {
            Glide.with(binding.root)
                .load(photoUrl)
                .into(binding.ivBanner)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val viewBinding = FragmentHomeBannerFrameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(photoList[holder.adapterPosition % photoList.size])
    }

    override fun getItemCount(): Int = Int.MAX_VALUE
}