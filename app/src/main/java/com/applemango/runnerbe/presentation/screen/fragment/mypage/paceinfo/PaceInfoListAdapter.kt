package com.applemango.runnerbe.presentation.screen.fragment.mypage.paceinfo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import com.applemango.runnerbe.R
import com.applemango.runnerbe.presentation.model.listener.PaceSelectListener
import com.applemango.runnerbe.presentation.screen.fragment.map.write.paceselect.PaceSelectItemCallBack

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