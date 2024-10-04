package com.applemango.runnerbe.presentation.screen.fragment.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.R
import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.presentation.model.PostIncomingType
import com.applemango.runnerbe.presentation.model.listener.PostClickListener

class PostAdapter(
    private val dataList: ObservableArrayList<Posting>,
    private val clickListener: PostClickListener,
    private val type: PostIncomingType
    ) : RecyclerView.Adapter<PostViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.item_post, parent, false
            ), clickListener
        )
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(dataList[position], type)
    }

}