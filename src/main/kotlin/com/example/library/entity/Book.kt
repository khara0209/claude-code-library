package com.example.library.entity

import java.time.LocalDateTime

data class Book(
    var id: Long? = null,
    var title: String = "",
    var author: String = "",
    var isbn: String? = null,
    var publisher: String? = null,
    var publishedYear: Int? = null,
    var category: String? = null,
    var available: Boolean = true,
    var createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null
)