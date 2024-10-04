package com.applemango.runnerbe.presentation.screen.fragment.chat.detail.image.preview

import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.databinding.ItemRunningTalkDetailSelectImageBinding
import com.applemango.runnerbe.util.dpToPx
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class RunningTalkDetailImageViewHolder(
    val binding: ItemRunningTalkDetailSelectImageBinding,
    val listener: RunningTalkDetailImageSelectListener
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(imageUrl: String) {
        val option =
            MultiTransformation(CenterCrop(), RoundedCorners(12.dpToPx(binding.root.context)))
        Glide.with(binding.root.context)
            .load(imageUrl)
            .apply(RequestOptions.bitmapTransform(option))
            .into(binding.selectImageView)
        binding.imageDeleteBtn.setOnClickListener {
            listener.imageDeleteClick(adapterPosition)
        }
    }
}