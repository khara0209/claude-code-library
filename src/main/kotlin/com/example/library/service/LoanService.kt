package com.example.library.service

import com.example.library.entity.Loan
import com.example.library.mapper.LoanMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional
class LoanService(
    private val loanMapper: LoanMapper,
    private val bookService: BookService
) {
    
    fun findAll(): List<Loan> = loanMapper.findAll()
    
    fun findById(id: Long): Loan? = loanMapper.findById(id)
    
    fun findActiveLoans(): List<Loan> = loanMapper.findActiveLoans()
    
    fun findByUserId(userId: Long): List<Loan> = loanMapper.findByUserId(userId)
    
    fun findByBookId(bookId: Long): List<Loan> = loanMapper.findByBookId(bookId)
    
    fun loanBook(bookId: Long, userId: Long, loanPeriodDays: Int = 14): Loan? {
        val book = bookService.findById(bookId)
        if (book?.available != true) {
            return null
        }
        
        val loan = Loan(
            bookId = bookId,
            userId = userId,
            loanDate = LocalDate.now(),
            dueDate = LocalDate.now().plusDays(loanPeriodDays.toLong()),
            status = "ACTIVE"
        )
        
        val result = loanMapper.insert(loan)
        if (result > 0) {
            bookService.updateAvailability(bookId, false)
            return loan
        }
        return null
    }
    
    fun returnBook(loanId: Long): Boolean {
        val loan = loanMapper.findById(loanId)
        if (loan?.status == "ACTIVE") {
            val result = loanMapper.updateReturn(loanId, LocalDate.now(), "RETURNED")
            if (result > 0) {
                bookService.updateAvailability(loan.bookId, true)
                return true
            }
        }
        return false
    }
    
    fun deleteById(id: Long): Boolean = loanMapper.deleteById(id) > 0
}