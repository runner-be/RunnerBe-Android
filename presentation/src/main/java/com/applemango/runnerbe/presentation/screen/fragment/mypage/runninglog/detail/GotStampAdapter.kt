package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.applemango.runnerbe.databinding.ItemGotStampBinding
import com.applemango.runnerbe.presentation.model.MemberStampModel

class GotStampAdapter : ListAdapter<MemberStampModel, GotStampViewHolder>(
    GOT_STAMP_DIFF_UTIL
) {
    private var listener : GotStampClickListener? = null



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GotStampViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return GotStampViewHolder(ItemGotStampBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: GotStampViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item, listener)
        }
    }

    fun initGotStampClickListener(listener : GotStampClickListener?) {
        this.listener = listener
    }

    companion object {
        private val GOT_STAMP_DIFF_UTIL = object: DiffUtil.ItemCallback<MemberStampModel>() {
            override fun areItemsTheSame(oldItem: MemberStampModel, newItem: MemberStampModel): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: MemberStampModel, newItem: MemberStampModel): Boolean {
                return oldItem.nickname == newItem.nickname
            }

        }
    }
}