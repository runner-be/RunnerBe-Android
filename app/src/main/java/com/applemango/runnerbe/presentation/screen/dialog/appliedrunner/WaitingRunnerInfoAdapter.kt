package com.applemango.runnerbe.presentation.screen.dialog.appliedrunner

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.R
import com.applemango.runnerbe.data.dto.UserInfo
import com.applemango.runnerbe.presentation.model.listener.PostAcceptListener

class WaitingRunnerInfoAdapter(
    private val dataList: ObservableArrayList<UserInfo>,
    private val listener: PostAcceptListener
) :
    RecyclerView.Adapter<WaitingRunnerInfoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WaitingRunnerInfoViewHolder {
        return WaitingRunnerInfoViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_waiting_runner_info,
                parent,
                false
            ), listener
        )
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: WaitingRunnerInfoViewHolder, position: Int) {
        holder.bind(dataList[position])
    }
}