package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.setPadding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.ItemGroupProfileBinding
import com.applemango.runnerbe.presentation.screen.dialog.stamp.StampItem
import com.bumptech.glide.Glide

class ProfileAdapter: ListAdapter<ProfileItem, ProfileAdapter.ProfileViewHolder>(profileDiffUtil) {
    private lateinit var onProfileClickListener: OnProfileClickListener
    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ProfileViewHolder(ItemGroupProfileBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item, onProfileClickListener)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateSelectedPosition(position: Int) {
        Log.e("UpdateSelectedPosition : ", "selectedPosition : $selectedPosition | newPosition: $position")
        selectedPosition = position
        notifyDataSetChanged()
    }

    fun setOnProfileClickListener(listener: OnProfileClickListener) {
        this.onProfileClickListener = listener
    }

    fun updateProfileStampByPosition(position: Int, newStamp: StampItem) {
        currentList[position].stamp = newStamp
        notifyItemChanged(position)
    }

    inner class ProfileViewHolder(
        private val binding: ItemGroupProfileBinding
    ) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: ProfileItem, onProfileClickListener: OnProfileClickListener) {
            with(binding) {
                Glide.with(root.context)
                    .load(item.image ?: R.drawable.ic_profile_default)
                    .into(ivProfile)
                tvProfileName.text = item.name
                ivLeader.visibility = if(item.isLeader) View.VISIBLE else View.GONE
                if (item.stamp == null) {
                    flStamp.visibility = View.GONE
                } else {
                    flStamp.visibility = View.VISIBLE
                    item.stamp?.image?.let {
                        ivStamp.setImageResource(it)
                    }
                }

                constProfile.setOnClickListener {
                    onProfileClickListener.onProfileClicked(bindingAdapterPosition, item.stamp)
                    updateSelectedPosition(bindingAdapterPosition)
                    if (bindingAdapterPosition == selectedPosition) {
                        binding.flProfile.setBackgroundResource(R.drawable.bg_g5_circle_shape_primary_stroke)
                    } else {
                        binding.flProfile.setBackgroundResource(R.drawable.bg_g5_circle_shape_no_stroke)
                    }
                }
            }
        }
    }

    companion object {
        private val profileDiffUtil = object : DiffUtil.ItemCallback<ProfileItem>() {
            override fun areItemsTheSame(
                oldItem: ProfileItem,
                newItem: ProfileItem
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: ProfileItem,
                newItem: ProfileItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}