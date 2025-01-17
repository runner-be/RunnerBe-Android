package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.groupprofile

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.ItemGroupProfileBinding
import com.applemango.runnerbe.presentation.model.JoinedRunnerModel
import com.applemango.runnerbe.presentation.screen.dialog.stamp.getStampItemByCode
import com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.otheruser.OtherUserProfileClickListener

class ProfileViewHolder(
    private val binding: ItemGroupProfileBinding,
    private val updateSelectedPosition: (Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        item: JoinedRunnerModel,
        selectedPosition: Int,
        otherUserProfileClickListener: OtherUserProfileClickListener?
    ) {
        val listener = View.OnClickListener {
            otherUserProfileClickListener?.onProfileClicked(
                bindingAdapterPosition,
                item.userId,
                getStampItemByCode(item.stampCode)
            )
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