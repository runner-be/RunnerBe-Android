package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.groupprofile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.applemango.runnerbe.databinding.ItemGroupProfileBinding
import com.applemango.runnerbe.presentation.model.JoinedRunnerModel
import com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.otheruser.OtherUserProfileClickListener

class ProfileAdapter: ListAdapter<JoinedRunnerModel, ProfileViewHolder>(
    profileDiffUtil
) {
    private var otherUserProfileClickListener: OtherUserProfileClickListener? = null
    private var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ProfileViewHolder(
            ItemGroupProfileBinding.inflate(inflater, parent, false),
            updateSelectedPosition = { position -> updateSelectedPosition(position) }
        )
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item, selectedPosition, otherUserProfileClickListener)
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
        val updatedList = currentList.toMutableList().apply {
            this[position] = this[position].copy(stampCode = newStampCode)
        }
        submitList(updatedList)
    }

    companion object {
        private val profileDiffUtil = object : DiffUtil.ItemCallback<JoinedRunnerModel>() {
            override fun areItemsTheSame(
                oldItem: JoinedRunnerModel,
                newItem: JoinedRunnerModel
            ): Boolean {
                return oldItem.userId == newItem.userId
            }

            override fun areContentsTheSame(
                oldItem: JoinedRunnerModel,
                newItem: JoinedRunnerModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}