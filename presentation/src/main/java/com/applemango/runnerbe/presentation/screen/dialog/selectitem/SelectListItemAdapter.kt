package com.applemango.runnerbe.presentation.screen.dialog.selectitem

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.R

class SelectListItemAdapter(private val dataList: ObservableArrayList<SelectListData>) : RecyclerView.Adapter<SelectListItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectListItemViewHolder {
        return SelectListItemViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_select,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SelectListItemViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size
}