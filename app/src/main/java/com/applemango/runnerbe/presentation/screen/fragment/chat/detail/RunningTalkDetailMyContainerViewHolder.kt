package com.applemango.runnerbe.presentation.screen.fragment.chat.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.ItemMeImageTalkBinding
import com.applemango.runnerbe.databinding.ItemMeMessageTalkBinding
import com.applemango.runnerbe.databinding.ItemMyTalkContainerBinding
import com.applemango.runnerbe.presentation.screen.fragment.chat.detail.uistate.RunningTalkItem
import com.applemango.runnerbe.presentation.screen.fragment.chat.detail.uistate.RunningTalkUiState
import com.applemango.runnerbe.util.dpToPx
import com.applemango.runnerbe.util.glide.GranularRoundedAndBorderTransform
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions

class RunningTalkDetailMyContainerViewHolder(
    val binding: ItemMyTalkContainerBinding,
    private val listener: RunningTalkDetailListClickListener
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: RunningTalkUiState.MyRunningTalkUiState) {
        val context = binding.root.context
        binding.messageContainerView.removeAllViews()
        item.items.forEach {
            val itemBinding = when (it) {
                is RunningTalkItem.MessageTalkItem -> {
                    val itemUi =
                        ItemMeMessageTalkBinding.inflate(LayoutInflater.from(context))
                    itemUi.apply {
                        messageView.text = it.message
                        messageView.setTextColor(
                            ResourcesCompat.getColor(
                                context.resources,
                                if (item.isPostWriter) R.color.black
                                else R.color.dark_g1,
                                null
                            )
                        )
                        messageView.background = ResourcesCompat.getDrawable(
                            context.resources,
                            if (item.isPostWriter) R.drawable.bg_my_talk_organizer
                            else R.drawable.bg_my_talk_participants,
                            null
                        )
                        if (item.items.last() == it) createDateView.text = item.createTime
                    }
                    itemUi
                }

                is RunningTalkItem.ImageTalkItem -> {
                    val itemUi =
                        ItemMeImageTalkBinding.inflate(LayoutInflater.from(context))
                    Glide.with(itemUi.talkImageView)
                        .load(it.imgUrl)
                        .apply(
                            RequestOptions()
                                .override(200.dpToPx(context), 200.dpToPx(context))
                                .transform(
                                    CenterCrop(),
                                    GranularRoundedAndBorderTransform(
                                        12f,
                                        12f,
                                        12f,
                                        0f,
                                        1f,
                                        R.color.white_20
                                    )
                                )
                        )
                        .into(itemUi.talkImageView)
                    itemUi.apply {
                        createDateView.isVisible = item.items.last() == it
                        if (item.items.last() == it) {
                            createDateView.text = item.createTime
                        }
                        talkImageView.setOnClickListener { _ ->
                            listener.imageClicked(
                                it.imgUrl,
                                item.items.map { it.messageId },
                                it.messageId
                            )
                        }
                    }
                    itemUi
                }
            }
            binding.messageContainerView.addView(itemBinding.root)
            val layoutParams = itemBinding.root.layoutParams
            layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            if (item.items.last() != it) {
                if (layoutParams is LinearLayoutCompat.LayoutParams) {
                    layoutParams.setMargins(0, 0, 0, 10.dpToPx(context))
                    itemBinding.root.layoutParams = layoutParams
                }
            }
        }
    }
}