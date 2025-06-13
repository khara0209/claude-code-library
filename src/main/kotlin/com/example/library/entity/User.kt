package com.example.library.entity

import java.time.LocalDateTime

data class User(
    var id: Long? = null,
    var name: String = "",
    var email: String = "",
    var phone: String? = null,
    var createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null
)