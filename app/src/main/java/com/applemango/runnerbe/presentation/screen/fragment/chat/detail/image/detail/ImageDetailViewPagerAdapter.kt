package com.applemango.runnerbe.presentation.screen.fragment.chat.detail.image.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.databinding.ItemImageDetailViewHolderBinding
import com.bumptech.glide.Glide

class ImageDetailViewPagerAdapter: ListAdapter<ImageDetailUiState, ImageDetailViewHolder>(
    ImageDetailDiffCallBack()
) {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ImageDetailViewHolder {
        return ImageDetailViewHolder(
            ItemImageDetailViewHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(p0: ImageDetailViewHolder, p1: Int) {
        p0.bind(getItem(p1))
    }
}

class ImageDetailViewHolder(val binding: ItemImageDetailViewHolderBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ImageDetailUiState) {
        Glide.with(binding.imageView)
            .load(item.imageUrl)
            .into(binding.imageView)
    }

}

class ImageDetailDiffCallBack : DiffUtil.ItemCallback<ImageDetailUiState>() {
    override fun areItemsTheSame(oldItem: ImageDetailUiState, newItem: ImageDetailUiState): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ImageDetailUiState, newItem: ImageDetailUiState): Boolean {
        return oldItem.imageUrl.equals(newItem.imageUrl)
    }


}