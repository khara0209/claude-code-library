package com.example.library.controller

import com.example.library.entity.Book
import com.example.library.service.BookService
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(BookController::class)
class BookControllerTest {
    
    @Autowired
    private lateinit var mockMvc: MockMvc
    
    @MockBean
    private lateinit var bookService: BookService
    
    @Test
    fun `list should return books list view`() {
        val books = listOf(
            Book(1L, "Test Book 1", "Author 1"),
            Book(2L, "Test Book 2", "Author 2")
        )
        whenever(bookService.findAll()).thenReturn(books)
        
        mockMvc.perform(get("/books"))
            .andExpect(status().isOk)
            .andExpect(view().name("books/list"))
            .andExpect(model().attribute("books", books))
        
        verify(bookService).findAll()
    }
    
    @Test
    fun `newBook should return form view with empty book`() {
        mockMvc.perform(get("/books/new"))
            .andExpect(status().isOk)
            .andExpect(view().name("books/form"))
            .andExpect(model().attributeExists("book"))
    }
    
    @Test
    fun `detail should return book detail view when book exists`() {
        val book = Book(1L, "Test Book", "Test Author")
        whenever(bookService.findById(1L)).thenReturn(book)
        
        mockMvc.perform(get("/books/1"))
            .andExpect(status().isOk)
            .andExpect(view().name("books/detail"))
            .andExpect(model().attribute("book", book))
        
        verify(bookService).findById(1L)
    }
    
    @Test
    fun `detail should redirect when book not found`() {
        whenever(bookService.findById(1L)).thenReturn(null)
        
        mockMvc.perform(get("/books/1"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/books"))
        
        verify(bookService).findById(1L)
    }
    
    @Test
    fun `edit should return form view when book exists`() {
        val book = Book(1L, "Test Book", "Test Author")
        whenever(bookService.findById(1L)).thenReturn(book)
        
        mockMvc.perform(get("/books/1/edit"))
            .andExpect(status().isOk)
            .andExpect(view().name("books/form"))
            .andExpect(model().attribute("book", book))
        
        verify(bookService).findById(1L)
    }
    
    @Test
    fun `edit should redirect when book not found`() {
        whenever(bookService.findById(1L)).thenReturn(null)
        
        mockMvc.perform(get("/books/1/edit"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/books"))
        
        verify(bookService).findById(1L)
    }
    
    @Test
    fun `save should redirect to books list with success message`() {
        val book = Book(title = "New Book", author = "New Author")
        whenever(bookService.save(any<Book>())).thenReturn(book)
        
        mockMvc.perform(post("/books")
            .param("title", "New Book")
            .param("author", "New Author"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/books"))
            .andExpect(flash().attributeExists("message"))
        
        verify(bookService).save(any<Book>())
    }
    
    @Test
    fun `save should redirect with error message when exception occurs`() {
        whenever(bookService.save(any<Book>())).thenThrow(RuntimeException("Save failed"))
        
        mockMvc.perform(post("/books")
            .param("title", "New Book")
            .param("author", "New Author"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/books"))
            .andExpect(flash().attributeExists("error"))
        
        verify(bookService).save(any<Book>())
    }
    
    @Test
    fun `delete should redirect with success message when deletion succeeds`() {
        whenever(bookService.deleteById(1L)).thenReturn(true)
        
        mockMvc.perform(post("/books/1/delete"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/books"))
            .andExpect(flash().attributeExists("message"))
        
        verify(bookService).deleteById(1L)
    }
    
    @Test
    fun `delete should redirect with error message when deletion fails`() {
        whenever(bookService.deleteById(1L)).thenReturn(false)
        
        mockMvc.perform(post("/books/1/delete"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/books"))
            .andExpect(flash().attributeExists("error"))
        
        verify(bookService).deleteById(1L)
    }
}