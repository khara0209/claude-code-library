package com.example.library.service

import com.example.library.entity.Book
import com.example.library.entity.Lend
import com.example.library.mapper.LendMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
class LendServiceTest {
    
    @Mock
    private lateinit var lendMapper: LendMapper
    
    @Mock
    private lateinit var bookService: BookService
    
    private lateinit var lendService: LendService
    
    @BeforeEach
    fun setUp() {
        lendService = LendService(lendMapper, bookService)
    }
    
    @Test
    fun `findAll should return all lends`() {
        val lends = listOf(
            Lend(1L, 1L, 1L, LocalDate.now(), LocalDate.now().plusDays(14), null, "ACTIVE"),
            Lend(2L, 2L, 2L, LocalDate.now(), LocalDate.now().plusDays(14), null, "ACTIVE")
        )
        whenever(lendMapper.findAll()).thenReturn(lends)
        
        val result = lendService.findAll()
        
        assertEquals(2, result.size)
        assertEquals("ACTIVE", result[0].status)
        verify(lendMapper).findAll()
    }
    
    @Test
    fun `findById should return lend when exists`() {
        val lend = Lend(1L, 1L, 1L, LocalDate.now(), LocalDate.now().plusDays(14), null, "ACTIVE")
        whenever(lendMapper.findById(1L)).thenReturn(lend)
        
        val result = lendService.findById(1L)
        
        assertNotNull(result)
        assertEquals(1L, result?.id)
        assertEquals("ACTIVE", result?.status)
        verify(lendMapper).findById(1L)
    }
    
    @Test
    fun `findById should return null when not exists`() {
        whenever(lendMapper.findById(1L)).thenReturn(null)
        
        val result = lendService.findById(1L)
        
        assertNull(result)
        verify(lendMapper).findById(1L)
    }
    
    @Test
    fun `findActiveLends should return only active lends`() {
        val activeLends = listOf(
            Lend(1L, 1L, 1L, LocalDate.now(), LocalDate.now().plusDays(14), null, "ACTIVE"),
            Lend(2L, 2L, 2L, LocalDate.now(), LocalDate.now().plusDays(14), null, "ACTIVE")
        )
        whenever(lendMapper.findActiveLends()).thenReturn(activeLends)
        
        val result = lendService.findActiveLends()
        
        assertEquals(2, result.size)
        assertTrue(result.all { it.status == "ACTIVE" })
        verify(lendMapper).findActiveLends()
    }
    
    @Test
    fun `findByUserId should return lends for specific user`() {
        val userLends = listOf(
            Lend(1L, 1L, 1L, LocalDate.now(), LocalDate.now().plusDays(14), null, "ACTIVE")
        )
        whenever(lendMapper.findByUserId(1L)).thenReturn(userLends)
        
        val result = lendService.findByUserId(1L)
        
        assertEquals(1, result.size)
        assertEquals(1L, result[0].userId)
        verify(lendMapper).findByUserId(1L)
    }
    
    @Test
    fun `findByBookId should return lends for specific book`() {
        val bookLends = listOf(
            Lend(1L, 1L, 1L, LocalDate.now(), LocalDate.now().plusDays(14), null, "ACTIVE")
        )
        whenever(lendMapper.findByBookId(1L)).thenReturn(bookLends)
        
        val result = lendService.findByBookId(1L)
        
        assertEquals(1, result.size)
        assertEquals(1L, result[0].bookId)
        verify(lendMapper).findByBookId(1L)
    }
    
    @Test
    fun `lendBook should create lend when book is available`() {
        val availableBook = Book(1L, "Test Book", "Test Author", available = true)
        whenever(bookService.findById(1L)).thenReturn(availableBook)
        whenever(lendMapper.insert(any())).thenReturn(1)
        whenever(bookService.updateAvailability(1L, false)).thenReturn(true)
        
        val result = lendService.lendBook(1L, 1L, 14)
        
        assertNotNull(result)
        assertEquals(1L, result?.bookId)
        assertEquals(1L, result?.userId)
        assertEquals("ACTIVE", result?.status)
        assertEquals(LocalDate.now().plusDays(14), result?.dueDate)
        
        verify(bookService).findById(1L)
        verify(lendMapper).insert(any())
        verify(bookService).updateAvailability(1L, false)
    }
    
    @Test
    fun `lendBook should return null when book is not available`() {
        val unavailableBook = Book(1L, "Test Book", "Test Author", available = false)
        whenever(bookService.findById(1L)).thenReturn(unavailableBook)
        
        val result = lendService.lendBook(1L, 1L, 14)
        
        assertNull(result)
        verify(bookService).findById(1L)
        verify(lendMapper, never()).insert(any())
        verify(bookService, never()).updateAvailability(anyOrNull(), anyOrNull())
    }
    
    @Test
    fun `lendBook should return null when book not found`() {
        whenever(bookService.findById(1L)).thenReturn(null)
        
        val result = lendService.lendBook(1L, 1L, 14)
        
        assertNull(result)
        verify(bookService).findById(1L)
        verify(lendMapper, never()).insert(any())
        verify(bookService, never()).updateAvailability(anyOrNull(), anyOrNull())
    }
    
    @Test
    fun `lendBook should return null when insert fails`() {
        val availableBook = Book(1L, "Test Book", "Test Author", available = true)
        whenever(bookService.findById(1L)).thenReturn(availableBook)
        whenever(lendMapper.insert(any())).thenReturn(0)
        
        val result = lendService.lendBook(1L, 1L, 14)
        
        assertNull(result)
        verify(bookService).findById(1L)
        verify(lendMapper).insert(any())
        verify(bookService, never()).updateAvailability(anyOrNull(), anyOrNull())
    }
    
    @Test
    fun `returnBook should return true when lend is active and update succeeds`() {
        val activeLend = Lend(1L, 1L, 1L, LocalDate.now(), LocalDate.now().plusDays(14), null, "ACTIVE")
        whenever(lendMapper.findById(1L)).thenReturn(activeLend)
        whenever(lendMapper.updateReturn(eq(1L), any(), eq("RETURNED"))).thenReturn(1)
        whenever(bookService.updateAvailability(1L, true)).thenReturn(true)
        
        val result = lendService.returnBook(1L)
        
        assertTrue(result)
        verify(lendMapper).findById(1L)
        verify(lendMapper).updateReturn(eq(1L), any(), eq("RETURNED"))
        verify(bookService).updateAvailability(1L, true)
    }
    
    @Test
    fun `returnBook should return false when lend is not active`() {
        val returnedLend = Lend(1L, 1L, 1L, LocalDate.now(), LocalDate.now().plusDays(14), LocalDate.now(), "RETURNED")
        whenever(lendMapper.findById(1L)).thenReturn(returnedLend)
        
        val result = lendService.returnBook(1L)
        
        assertFalse(result)
        verify(lendMapper).findById(1L)
        verify(lendMapper, never()).updateReturn(anyOrNull(), anyOrNull(), anyOrNull())
        verify(bookService, never()).updateAvailability(anyOrNull(), anyOrNull())
    }
    
    @Test
    fun `returnBook should return false when lend not found`() {
        whenever(lendMapper.findById(1L)).thenReturn(null)
        
        val result = lendService.returnBook(1L)
        
        assertFalse(result)
        verify(lendMapper).findById(1L)
        verify(lendMapper, never()).updateReturn(anyOrNull(), anyOrNull(), anyOrNull())
        verify(bookService, never()).updateAvailability(anyOrNull(), anyOrNull())
    }
    
    @Test
    fun `returnBook should return false when update fails`() {
        val activeLend = Lend(1L, 1L, 1L, LocalDate.now(), LocalDate.now().plusDays(14), null, "ACTIVE")
        whenever(lendMapper.findById(1L)).thenReturn(activeLend)
        whenever(lendMapper.updateReturn(eq(1L), any(), eq("RETURNED"))).thenReturn(0)
        
        val result = lendService.returnBook(1L)
        
        assertFalse(result)
        verify(lendMapper).findById(1L)
        verify(lendMapper).updateReturn(eq(1L), any(), eq("RETURNED"))
        verify(bookService, never()).updateAvailability(anyOrNull(), anyOrNull())
    }
    
    @Test
    fun `deleteById should return true when delete succeeds`() {
        whenever(lendMapper.deleteById(1L)).thenReturn(1)
        
        val result = lendService.deleteById(1L)
        
        assertTrue(result)
        verify(lendMapper).deleteById(1L)
    }
    
    @Test
    fun `deleteById should return false when delete fails`() {
        whenever(lendMapper.deleteById(1L)).thenReturn(0)
        
        val result = lendService.deleteById(1L)
        
        assertFalse(result)
        verify(lendMapper).deleteById(1L)
    }
}