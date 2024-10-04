package com.applemango.runnerbe.presentation.screen.fragment.chat.detail

import android.view.LayoutInflater
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.ItemOtherImageTalkBinding
import com.applemango.runnerbe.databinding.ItemOtherMessageTalkBinding
import com.applemango.runnerbe.databinding.ItemOtherTalkContainerBinding
import com.applemango.runnerbe.presentation.screen.fragment.chat.detail.uistate.RunningTalkItem
import com.applemango.runnerbe.presentation.screen.fragment.chat.detail.uistate.RunningTalkUiState
import com.applemango.runnerbe.util.dpToPx
import com.applemango.runnerbe.util.glide.GranularRoundedAndBorderTransform
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class RunningTalkDetailOtherContainerViewHolder(
    val binding: ItemOtherTalkContainerBinding,
    private val listener: RunningTalkDetailListClickListener
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: RunningTalkUiState.OtherRunningTalkUiState) {
        val context = binding.root.context
        binding.messageContainerView.removeAllViews()
        binding.nameView.text = item.writerName
        Glide.with(binding.profileImageView)
            .load(item.writerProfileImgUrl)
            .transform(CenterCrop(), RoundedCorners(200))
            .placeholder(R.drawable.ic_user_default)
            .error(R.drawable.ic_user_default)
            .into(binding.profileImageView)
        item.items.forEach {
            val itemBinding = when (it) {
                is RunningTalkItem.MessageTalkItem -> {
                    val itemUi =
                        ItemOtherMessageTalkBinding.inflate(LayoutInflater.from(context))
                    itemUi.apply {
                        if (item.items.first() == it && item.isReportMode) {
                            reportCheckBtn.visibility = View.VISIBLE
                        } else {
                            reportCheckBtn.visibility = View.GONE
                        }
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
                            if (item.isPostWriter) R.drawable.bg_other_talk_organizer
                            else R.drawable.bg_other_talk_participants,
                            null
                        )
                        if (item.items.last() == it) {
                            createDateView.text = item.createTime
                            createDateView.visibility = View.VISIBLE
                        } else {
                            createDateView.visibility = View.GONE
                        }
                    }
                    itemUi
                }

                is RunningTalkItem.ImageTalkItem -> {
                    val itemUi =
                        ItemOtherImageTalkBinding.inflate(LayoutInflater.from(context))
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
                                        0f,
                                        12f,
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
        }
    }
}