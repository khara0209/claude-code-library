package com.example.library.service

import com.example.library.entity.Book
import com.example.library.mapper.BookMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BookService(private val bookMapper: BookMapper) {
    
    fun findAll(): List<Book> = bookMapper.findAll()
    
    fun findById(id: Long): Book? = bookMapper.findById(id)
    
    fun findAvailableBooks(): List<Book> = bookMapper.findAvailableBooks()
    
    fun save(book: Book): Book {
        return if (book.id == null || book.id == 0L) {
            // 新規作成の場合
            book.id = null
            val result = bookMapper.insert(book)
            if (result > 0) {
                book
            } else {
                throw RuntimeException("書籍の保存に失敗しました")
            }
        } else {
            // 更新の場合
            val result = bookMapper.update(book)
            if (result > 0) {
                book
            } else {
                throw RuntimeException("書籍の更新に失敗しました")
            }
        }
    }
    
    fun updateAvailability(id: Long, available: Boolean): Boolean {
        return bookMapper.updateAvailability(id, available) > 0
    }
    
    fun deleteById(id: Long): Boolean = bookMapper.deleteById(id) > 0
}