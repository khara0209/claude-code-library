package com.example.library.service

import com.example.library.entity.Book
import com.example.library.mapper.BookMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*

@ExtendWith(MockitoExtension::class)
class BookServiceTest {
    
    @Mock
    private lateinit var bookMapper: BookMapper
    
    private lateinit var bookService: BookService
    
    @BeforeEach
    fun setUp() {
        bookService = BookService(bookMapper)
    }
    
    @Test
    fun `findAll should return all books`() {
        val books = listOf(
            Book(1L, "Test Book 1", "Author 1"),
            Book(2L, "Test Book 2", "Author 2")
        )
        whenever(bookMapper.findAll()).thenReturn(books)
        
        val result = bookService.findAll()
        
        assertEquals(2, result.size)
        assertEquals("Test Book 1", result[0].title)
        verify(bookMapper).findAll()
    }
    
    @Test
    fun `findById should return book when exists`() {
        val book = Book(1L, "Test Book", "Test Author")
        whenever(bookMapper.findById(1L)).thenReturn(book)
        
        val result = bookService.findById(1L)
        
        assertNotNull(result)
        assertEquals("Test Book", result?.title)
        verify(bookMapper).findById(1L)
    }
    
    @Test
    fun `findById should return null when not exists`() {
        whenever(bookMapper.findById(1L)).thenReturn(null)
        
        val result = bookService.findById(1L)
        
        assertNull(result)
        verify(bookMapper).findById(1L)
    }
    
    @Test
    fun `findAvailableBooks should return only available books`() {
        val availableBooks = listOf(
            Book(1L, "Available Book 1", "Author 1", available = true),
            Book(2L, "Available Book 2", "Author 2", available = true)
        )
        whenever(bookMapper.findAvailableBooks()).thenReturn(availableBooks)
        
        val result = bookService.findAvailableBooks()
        
        assertEquals(2, result.size)
        assertTrue(result.all { it.available })
        verify(bookMapper).findAvailableBooks()
    }
    
    @Test
    fun `save should insert new book when id is null`() {
        val book = Book(null, "New Book", "New Author")
        whenever(bookMapper.insert(book)).thenReturn(1)
        
        val result = bookService.save(book)
        
        assertEquals("New Book", result.title)
        verify(bookMapper).insert(book)
        verify(bookMapper, never()).update(any())
    }
    
    @Test
    fun `save should insert new book when id is 0`() {
        val book = Book(0L, "New Book", "New Author")
        whenever(bookMapper.insert(book)).thenReturn(1)
        
        val result = bookService.save(book)
        
        assertEquals("New Book", result.title)
        assertNull(result.id)
        verify(bookMapper).insert(book)
        verify(bookMapper, never()).update(any())
    }
    
    @Test
    fun `save should update existing book when id is provided`() {
        val book = Book(1L, "Updated Book", "Updated Author")
        whenever(bookMapper.update(book)).thenReturn(1)
        
        val result = bookService.save(book)
        
        assertEquals("Updated Book", result.title)
        verify(bookMapper).update(book)
        verify(bookMapper, never()).insert(any())
    }
    
    @Test
    fun `save should throw exception when insert fails`() {
        val book = Book(null, "New Book", "New Author")
        whenever(bookMapper.insert(book)).thenReturn(0)
        
        val exception = assertThrows<RuntimeException> {
            bookService.save(book)
        }
        
        assertEquals("書籍の保存に失敗しました", exception.message)
        verify(bookMapper).insert(book)
    }
    
    @Test
    fun `save should throw exception when update fails`() {
        val book = Book(1L, "Updated Book", "Updated Author")
        whenever(bookMapper.update(book)).thenReturn(0)
        
        val exception = assertThrows<RuntimeException> {
            bookService.save(book)
        }
        
        assertEquals("書籍の更新に失敗しました", exception.message)
        verify(bookMapper).update(book)
    }
    
    @Test
    fun `updateAvailability should return true when update succeeds`() {
        whenever(bookMapper.updateAvailability(1L, false)).thenReturn(1)
        
        val result = bookService.updateAvailability(1L, false)
        
        assertTrue(result)
        verify(bookMapper).updateAvailability(1L, false)
    }
    
    @Test
    fun `updateAvailability should return false when update fails`() {
        whenever(bookMapper.updateAvailability(1L, false)).thenReturn(0)
        
        val result = bookService.updateAvailability(1L, false)
        
        assertFalse(result)
        verify(bookMapper).updateAvailability(1L, false)
    }
    
    @Test
    fun `deleteById should return true when delete succeeds`() {
        whenever(bookMapper.deleteById(1L)).thenReturn(1)
        
        val result = bookService.deleteById(1L)
        
        assertTrue(result)
        verify(bookMapper).deleteById(1L)
    }
    
    @Test
    fun `deleteById should return false when delete fails`() {
        whenever(bookMapper.deleteById(1L)).thenReturn(0)
        
        val result = bookService.deleteById(1L)
        
        assertFalse(result)
        verify(bookMapper).deleteById(1L)
    }
}