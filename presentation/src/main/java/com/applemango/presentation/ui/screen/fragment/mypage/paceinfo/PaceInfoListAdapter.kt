package com.applemango.presentation.ui.screen.fragment.mypage.paceinfo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import com.applemango.presentation.ui.model.listener.PaceSelectListener
import com.applemango.presentation.ui.screen.fragment.map.write.paceselect.PaceSelectItemCallBack
import com.applemango.presentation.R

class PaceInfoListAdapter(
    private val listener: PaceSelectListener
)  : ListAdapter<PaceSelectItem, PaceInfoListViewHolder>(PaceSelectItemCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaceInfoListViewHolder {
        return PaceInfoListViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_pace_select,
                parent,
                false
            ),
            listener
        )
    }

    override fun onBindViewHolder(holder: PaceInfoListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}