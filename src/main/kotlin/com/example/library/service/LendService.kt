package com.example.library.service

import com.example.library.entity.Lend
import com.example.library.mapper.LendMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional
class LendService(
    private val lendMapper: LendMapper,
    private val bookService: BookService
) {
    
    fun findAll(): List<Lend> = lendMapper.findAll()
    
    fun findById(id: Long): Lend? = lendMapper.findById(id)
    
    fun findActiveLends(): List<Lend> = lendMapper.findActiveLends()
    
    fun findByUserId(userId: Long): List<Lend> = lendMapper.findByUserId(userId)
    
    fun findByBookId(bookId: Long): List<Lend> = lendMapper.findByBookId(bookId)
    
    fun lendBook(bookId: Long, userId: Long, lendPeriodDays: Int = 14): Lend? {
        val book = bookService.findById(bookId)
        if (book?.available != true) {
            return null
        }
        
        val lend = Lend(
            bookId = bookId,
            userId = userId,
            lendDate = LocalDate.now(),
            dueDate = LocalDate.now().plusDays(lendPeriodDays.toLong()),
            status = "ACTIVE"
        )
        
        val result = lendMapper.insert(lend)
        if (result > 0) {
            bookService.updateAvailability(bookId, false)
            return lend
        }
        return null
    }
    
    fun returnBook(lendId: Long): Boolean {
        val lend = lendMapper.findById(lendId)
        if (lend?.status == "ACTIVE") {
            val result = lendMapper.updateReturn(lendId, LocalDate.now(), "RETURNED")
            if (result > 0) {
                bookService.updateAvailability(lend.bookId, true)
                return true
            }
        }
        return false
    }
    
    fun deleteById(id: Long): Boolean = lendMapper.deleteById(id) > 0
}