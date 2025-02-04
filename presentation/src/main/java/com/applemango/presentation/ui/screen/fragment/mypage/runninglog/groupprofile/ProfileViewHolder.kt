package com.applemango.presentation.ui.screen.fragment.mypage.runninglog.groupprofile

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.applemango.presentation.ui.model.JoinedRunnerModel
import com.applemango.presentation.ui.screen.dialog.stamp.StampItem
import com.applemango.presentation.ui.screen.fragment.mypage.runninglog.otheruser.OtherUserProfileClickListener
import com.applemango.presentation.R
import com.applemango.presentation.databinding.ItemGroupProfileBinding

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
                StampItem.getStampItemByCode(itemView.context, item.stampCode)
            )
            updateSelectedPosition(bindingAdapterPosition)
        }

        if (bindingAdapterPosition == selectedPosition) {
            binding.flProfile.setBackgroundResource(R.drawable.bg_g5_circle_shape_primary_stroke)
        } else {
            binding.flProfile.setBackgroundResource(R.drawable.bg_g7_circle_shape_no_stroke)
        }

        binding.item = item
        binding.stamp = StampItem.getStampItemByCode(itemView.context, item.stampCode)
        binding.listener = listener
        binding.clickListener = otherUserProfileClickListener
    }
}