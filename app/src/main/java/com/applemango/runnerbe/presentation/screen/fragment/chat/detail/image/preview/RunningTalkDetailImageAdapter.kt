package com.applemango.runnerbe.presentation.screen.fragment.chat.detail.image.preview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.applemango.runnerbe.databinding.ItemRunningTalkDetailSelectImageBinding

class RunningTalkDetailImageAdapter(val listener: RunningTalkDetailImageSelectListener) :
    ListAdapter<String, RunningTalkDetailImageViewHolder>(RunningTalkDetailSelectImageDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RunningTalkDetailImageViewHolder {
        return RunningTalkDetailImageViewHolder(
            ItemRunningTalkDetailSelectImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), listener
        )
    }

    override fun onBindViewHolder(holder: RunningTalkDetailImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class RunningTalkDetailSelectImageDiffCallBack : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(p0: String, p1: String): Boolean = p0 == p1
    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem

}