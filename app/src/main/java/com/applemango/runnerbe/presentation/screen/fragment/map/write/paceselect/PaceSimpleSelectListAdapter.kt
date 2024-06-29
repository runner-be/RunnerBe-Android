package com.applemango.runnerbe.presentation.screen.fragment.map.write.paceselect

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.applemango.runnerbe.R
import com.applemango.runnerbe.presentation.model.listener.PaceSelectListener
import com.applemango.runnerbe.presentation.screen.fragment.mypage.paceinfo.PaceSelectItem

class PaceSimpleSelectListAdapter(
    private val listener: PaceSelectListener
) : ListAdapter<PaceSelectItem, PaceSimpleSelectListViewHolder>(PaceSelectItemCallBack()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PaceSimpleSelectListViewHolder {
        return PaceSimpleSelectListViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_pace_radio,
                parent,
                false
            ), listener = listener
        )
    }
    override fun onBindViewHolder(holder: PaceSimpleSelectListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class PaceSelectItemCallBack: DiffUtil.ItemCallback<PaceSelectItem>() {
    override fun areItemsTheSame(oldItem: PaceSelectItem, newItem: PaceSelectItem): Boolean = oldItem.pace == newItem.pace


    override fun areContentsTheSame(oldItem: PaceSelectItem, newItem: PaceSelectItem): Boolean {
        return oldItem.isSelected == newItem.isSelected
    }

}