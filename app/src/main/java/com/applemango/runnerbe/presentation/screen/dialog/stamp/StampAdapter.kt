package com.applemango.runnerbe.presentation.screen.dialog.stamp

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.ItemStampBinding
import com.applemango.runnerbe.presentation.screen.dialog.stamp.StampAdapter.*
import com.bumptech.glide.Glide

class StampAdapter : ListAdapter<StampItem, StampViewHolder>(stampDiffUtil) {
    private lateinit var onStampClickListener: OnStampClickListener
    var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StampViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return StampViewHolder(ItemStampBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: StampViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item, onStampClickListener)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSelectedStamp(stamp: StampItem?) {
        val index = currentList.indexOfFirst { it == stamp }
        if (index != -1) {
            selectedPosition = index
            notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateSelectedPosition(position: Int) {
        selectedPosition = position
        notifyDataSetChanged()
    }

    fun setOnStampClickListener(listener: OnStampClickListener) {
        this.onStampClickListener = listener
    }

    inner class StampViewHolder (
        private val binding: ItemStampBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StampItem, onStampClickListener: OnStampClickListener) {
            Glide.with(binding.root.context)
                .load(item.image)
                .into(binding.ivStamp)

            if (bindingAdapterPosition == selectedPosition) {
                binding.flStamp.setBackgroundResource(R.drawable.bg_g5_circle_shape_primary_stroke)
            } else {
                binding.flStamp.setBackgroundResource(R.drawable.bg_g5_circle_shape_no_stroke)
            }
            binding.flStamp.setOnClickListener {
                updateSelectedPosition(bindingAdapterPosition)
                onStampClickListener.onStampSelected(item)
            }
        }
    }

    companion object {
        private val stampDiffUtil = object : DiffUtil.ItemCallback<StampItem>() {
            override fun areItemsTheSame(
                oldItem: StampItem,
                newItem: StampItem
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: StampItem,
                newItem: StampItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}