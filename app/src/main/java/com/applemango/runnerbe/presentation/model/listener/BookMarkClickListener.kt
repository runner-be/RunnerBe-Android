package com.applemango.runnerbe.presentation.model.listener

import com.applemango.runnerbe.presentation.model.PostingModel

interface BookMarkClickListener {
    fun onBookMarkClick(post: PostingModel)
    fun onClick(post: PostingModel)
}