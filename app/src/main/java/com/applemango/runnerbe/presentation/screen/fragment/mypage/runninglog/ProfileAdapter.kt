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
import com.applemango.runnerbe.data.network.response.JoinedRunnerResponse
import com.applemango.runnerbe.data.network.response.JoinedRunnerResult
import com.applemango.runnerbe.databinding.ItemGroupProfileBinding
import com.applemango.runnerbe.presentation.screen.dialog.stamp.StampItem
import com.applemango.runnerbe.presentation.screen.dialog.stamp.getStampItemByCode
import com.bumptech.glide.Glide

class ProfileAdapter: ListAdapter<JoinedRunnerResult, ProfileAdapter.ProfileViewHolder>(profileDiffUtil) {
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
        selectedPosition = position
        notifyDataSetChanged()
    }

    fun setOnProfileClickListener(listener: OnProfileClickListener) {
        this.onProfileClickListener = listener
    }

    fun updateProfileStampByPosition(position: Int, newStampCode: String) {
        currentList[position].stampCode = newStampCode
        notifyItemChanged(position)
    }

    inner class ProfileViewHolder(
        private val binding: ItemGroupProfileBinding
    ) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: JoinedRunnerResult, onProfileClickListener: OnProfileClickListener) {
            Log.e("runnerListFlow", item.toString())
            with(binding) {
                Glide.with(root.context)
                    .load(item.profileImageUrl ?: R.drawable.ic_profile_default)
                    .into(ivProfile)
                tvProfileName.text = item.nickname
                ivLeader.visibility = if(item.isCaptain == 1) View.VISIBLE else View.GONE
                if (item.stampCode == null) {
                    flStamp.visibility = View.GONE
                } else {
                    flStamp.visibility = View.VISIBLE
                    ivStamp.setImageResource(getStampItemByCode(item.stampCode).image)
                }

                constProfile.setOnClickListener {
                    onProfileClickListener.onProfileClicked(bindingAdapterPosition, item.userId, getStampItemByCode(item.stampCode))
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
        private val profileDiffUtil = object : DiffUtil.ItemCallback<JoinedRunnerResult>() {
            override fun areItemsTheSame(
                oldItem: JoinedRunnerResult,
                newItem: JoinedRunnerResult
            ): Boolean {
                return oldItem.nickname == newItem.nickname
            }

            override fun areContentsTheSame(
                oldItem: JoinedRunnerResult,
                newItem: JoinedRunnerResult
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}