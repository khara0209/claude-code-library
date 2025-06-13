package com.example.library.entity

import java.time.LocalDate
import java.time.LocalDateTime

data class Loan(
    var id: Long? = null,
    var bookId: Long = 0,
    var userId: Long = 0,
    var loanDate: LocalDate = LocalDate.now(),
    var dueDate: LocalDate = LocalDate.now().plusDays(14),
    var returnDate: LocalDate? = null,
    var status: String = "ACTIVE",
    var createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null
)