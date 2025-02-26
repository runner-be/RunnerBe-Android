package com.applemango.presentation.ui.screen.fragment.chat.detail.image.detail

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.applemango.presentation.databinding.ItemImageDetailViewHolderBinding
import com.bumptech.glide.Glide

class ImageDetailViewPagerAdapter: ListAdapter<Uri, ImageDetailViewPagerAdapter.ImageDetailViewHolder>(imageDetailDiffCallBack) {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ImageDetailViewHolder {
        return ImageDetailViewHolder(
            ItemImageDetailViewHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageDetailViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ImageDetailViewHolder(val binding: ItemImageDetailViewHolderBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Uri) {
            Glide.with(itemView.context)
                .load(item)
                .into(binding.imageView)
        }
    }

    companion object {
        private val imageDetailDiffCallBack = object : DiffUtil.ItemCallback<Uri>() {
            override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean {
                return oldItem == newItem
            }
        }
    }
}