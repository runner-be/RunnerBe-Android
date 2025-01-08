package kr.devkyu.domain.entity

data class BookmarksEntity(
    var bookMarkList: List<PostingEntity>?
): BaseEntity()
