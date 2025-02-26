package com.applemango.presentation.ui.screen.dialog.stamp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.applemango.presentation.util.ToastUtil
import com.applemango.presentation.R
import com.applemango.presentation.databinding.ItemStampBinding
import com.bumptech.glide.Glide

class StampAdapter : ListAdapter<StampItem, StampAdapter.StampViewHolder>(stampDiffUtil) {
    private lateinit var onStampClickListener: OnStampClickListener
    private var isPersonalLog: Boolean = false
    var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StampViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return StampViewHolder(ItemStampBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: StampViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item, onStampClickListener, isPersonalLog)
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

    fun setIsPersonalLog(isPersonalLog: Boolean) {
        this.isPersonalLog = isPersonalLog
    }

    inner class StampViewHolder (
        private val binding: ItemStampBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StampItem, onStampClickListener: OnStampClickListener, isPersonalLog: Boolean) {
            if (isPersonalLog) {
                if (item.code == "RUN001") {
                    binding.flStamp.setOnClickListener {
                        updateSelectedPosition(bindingAdapterPosition)
                        onStampClickListener.onStampSelected(item)
                    }
                } else {
                    binding.flStamp.setOnClickListener {
                        ToastUtil.showShortToast(itemView.context, "같이 뛰면 사용할 수 있어요!")
                    }
                }
            } else {
                binding.flStamp.setOnClickListener {
                    updateSelectedPosition(bindingAdapterPosition)
                    onStampClickListener.onStampSelected(item)
                }
            }

            Glide.with(binding.root.context)
                .load(item.image)
                .into(binding.ivStamp)

            if (bindingAdapterPosition == selectedPosition) {
                binding.flStamp.setBackgroundResource(R.drawable.bg_g5_circle_shape_primary_stroke)
            } else {
                binding.flStamp.setBackgroundResource(R.drawable.bg_g5_circle_shape_no_stroke)
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