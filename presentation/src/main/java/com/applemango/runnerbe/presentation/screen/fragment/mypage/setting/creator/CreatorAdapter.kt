package com.applemango.runnerbe.presentation.screen.fragment.mypage.setting.creator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.applemango.runnerbe.R
import com.applemango.runnerbe.presentation.model.type.CreatorImageAndPosition

class CreatorAdapter : ListAdapter<CreatorImageAndPosition, CreatorViewHolder>(creatorDiffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreatorViewHolder {
        return CreatorViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_creator,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CreatorViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object {
        private val creatorDiffUtil = object: DiffUtil.ItemCallback<CreatorImageAndPosition>() {
            override fun areItemsTheSame(oldItem: CreatorImageAndPosition, newItem: CreatorImageAndPosition): Boolean {
                return oldItem.creatorName == newItem.creatorName
            }

            override fun areContentsTheSame(oldItem: CreatorImageAndPosition, newItem: CreatorImageAndPosition): Boolean {
                return oldItem == newItem
            }
        }
    }
}