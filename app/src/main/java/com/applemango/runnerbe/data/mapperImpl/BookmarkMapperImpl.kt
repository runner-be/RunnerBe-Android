package com.applemango.runnerbe.data.mapperImpl

import com.applemango.runnerbe.data.dto.new.BookmarkList
import com.applemango.runnerbe.data.dto.new.BookmarksDto
import com.applemango.runnerbe.data.mapper.BookmarksMapper
import com.applemango.runnerbe.data.mapper.PostingMapper
import com.applemango.runnerbe.entity.BookmarksEntity
import javax.inject.Inject

class BookmarkMapperImpl @Inject constructor(
    private val postingMapper: PostingMapper,
): BookmarksMapper {
    override fun mapToDomain(input: BookmarksDto): BookmarksEntity {
        val postings = input.result.bookMarkList?.map {
            postingMapper.mapToDomain(it)
        } ?: emptyList()
        return BookmarksEntity(
            bookmarks = postings
        )
    }

    override fun mapToData(input: BookmarksEntity): BookmarksDto {
        val postings = input.bookmarks?.map {
            postingMapper.mapToData(it)
        } ?: emptyList()
        return BookmarksDto(
            result = BookmarkList(bookMarkList = postings)
        )
    }
}