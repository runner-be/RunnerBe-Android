package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.groupprofile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.R
import com.applemango.runnerbe.data.network.response.JoinedRunnerResult
import com.applemango.runnerbe.databinding.ItemGroupProfileBinding
import com.applemango.runnerbe.presentation.screen.dialog.stamp.getStampItemByCode
import com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.otheruser.OtherUserProfileClickListener

class ProfileAdapter: ListAdapter<JoinedRunnerResult, ProfileAdapter.ProfileViewHolder>(
    profileDiffUtil
) {
    private lateinit var otherUserProfileClickListener: OtherUserProfileClickListener
    private var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ProfileViewHolder(ItemGroupProfileBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item, otherUserProfileClickListener)
        }
    }

    private fun updateSelectedPosition(position: Int) {
        val prevPosition = selectedPosition
        selectedPosition = position

        notifyItemChanged(prevPosition)
        notifyItemChanged(position)
    }

    fun setOnProfileClickListener(listener: OtherUserProfileClickListener) {
        this.otherUserProfileClickListener = listener
    }

    fun updateProfileStampByPosition(position: Int, newStampCode: String) {
        currentList[position].stampCode = newStampCode
        notifyItemChanged(position)
    }

    inner class ProfileViewHolder(
        private val binding: ItemGroupProfileBinding
    ) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: JoinedRunnerResult, otherUserProfileClickListener: OtherUserProfileClickListener) {
            val listener = View.OnClickListener {
                otherUserProfileClickListener.onProfileClicked(bindingAdapterPosition, item.userId, getStampItemByCode(item.stampCode))
                updateSelectedPosition(bindingAdapterPosition)
            }

            if (bindingAdapterPosition == selectedPosition) {
                binding.flProfile.setBackgroundResource(R.drawable.bg_g5_circle_shape_primary_stroke)
            } else {
                binding.flProfile.setBackgroundResource(R.drawable.bg_g7_circle_shape_no_stroke)
            }

            binding.item = item
            binding.stamp = getStampItemByCode(item.stampCode)
            binding.listener = listener
            binding.clickListener = otherUserProfileClickListener
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