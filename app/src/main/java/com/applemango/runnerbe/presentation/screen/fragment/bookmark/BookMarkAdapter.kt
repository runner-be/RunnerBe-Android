package com.applemango.runnerbe.presentation.screen.fragment.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.R
import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.presentation.model.listener.BookMarkClickListener
import com.applemango.runnerbe.presentation.screen.fragment.mypage.joinedrunning.JoinedRunningClickListener

class BookMarkAdapter : ListAdapter<Posting, BookMarkViewHolder>(bookmarkDiffUtil) {
    private lateinit var bookmarkListener: JoinedRunningClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookMarkViewHolder {
        return BookMarkViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_join_post_with_bookmark,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BookMarkViewHolder, position: Int) {
        val item = currentList[position]
        if (item != null) {
            holder.bind(item, bookmarkListener)
        }
    }

    fun setBookmarkClickListener(listener: JoinedRunningClickListener) {
        this.bookmarkListener = listener
    }

    companion object {
        private val bookmarkDiffUtil = object : DiffUtil.ItemCallback<Posting>() {
            override fun areItemsTheSame(oldItem: Posting, newItem: Posting): Boolean {
                return oldItem.postId == newItem.postId
            }

            override fun areContentsTheSame(oldItem: Posting, newItem: Posting): Boolean {
                return oldItem.bookMark == newItem.bookMark
            }

        }
    }
}