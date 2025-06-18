package com.example.library.controller

import com.example.library.entity.User
import com.example.library.service.UserService
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(UserController::class)
class UserControllerTest {
    
    @Autowired
    private lateinit var mockMvc: MockMvc
    
    @MockBean
    private lateinit var userService: UserService
    
    @Test
    fun `list should return users list view`() {
        val users = listOf(
            User(1L, "Test User 1", "test1@example.com"),
            User(2L, "Test User 2", "test2@example.com")
        )
        whenever(userService.findAll()).thenReturn(users)
        
        mockMvc.perform(get("/users"))
            .andExpect(status().isOk)
            .andExpect(view().name("users/list"))
            .andExpect(model().attribute("users", users))
        
        verify(userService).findAll()
    }
    
    @Test
    fun `newUser should return form view with empty user`() {
        mockMvc.perform(get("/users/new"))
            .andExpect(status().isOk)
            .andExpect(view().name("users/form"))
            .andExpect(model().attributeExists("user"))
    }
    
    @Test
    fun `detail should return user detail view when user exists`() {
        val user = User(1L, "Test User", "test@example.com")
        whenever(userService.findById(1L)).thenReturn(user)
        
        mockMvc.perform(get("/users/1"))
            .andExpect(status().isOk)
            .andExpect(view().name("users/detail"))
            .andExpect(model().attribute("user", user))
        
        verify(userService).findById(1L)
    }
    
    @Test
    fun `detail should redirect when user not found`() {
        whenever(userService.findById(1L)).thenReturn(null)
        
        mockMvc.perform(get("/users/1"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/users"))
        
        verify(userService).findById(1L)
    }
    
    @Test
    fun `edit should return form view when user exists`() {
        val user = User(1L, "Test User", "test@example.com")
        whenever(userService.findById(1L)).thenReturn(user)
        
        mockMvc.perform(get("/users/1/edit"))
            .andExpect(status().isOk)
            .andExpect(view().name("users/form"))
            .andExpect(model().attribute("user", user))
        
        verify(userService).findById(1L)
    }
    
    @Test
    fun `edit should redirect when user not found`() {
        whenever(userService.findById(1L)).thenReturn(null)
        
        mockMvc.perform(get("/users/1/edit"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/users"))
        
        verify(userService).findById(1L)
    }
    
    @Test
    fun `save should redirect to users list with success message`() {
        val user = User(name = "New User", email = "new@example.com")
        whenever(userService.save(any<User>())).thenReturn(user)
        
        mockMvc.perform(post("/users")
            .param("name", "New User")
            .param("email", "new@example.com"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/users"))
            .andExpect(flash().attributeExists("message"))
        
        verify(userService).save(any<User>())
    }
    
    @Test
    fun `save should redirect with error message when exception occurs`() {
        whenever(userService.save(any<User>())).thenThrow(RuntimeException("Save failed"))
        
        mockMvc.perform(post("/users")
            .param("name", "New User")
            .param("email", "new@example.com"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/users"))
            .andExpect(flash().attributeExists("error"))
        
        verify(userService).save(any<User>())
    }
    
    @Test
    fun `delete should redirect with success message when deletion succeeds`() {
        whenever(userService.deleteById(1L)).thenReturn(true)
        
        mockMvc.perform(post("/users/1/delete"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/users"))
            .andExpect(flash().attributeExists("message"))
        
        verify(userService).deleteById(1L)
    }
    
    @Test
    fun `delete should redirect with error message when deletion fails`() {
        whenever(userService.deleteById(1L)).thenReturn(false)
        
        mockMvc.perform(post("/users/1/delete"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/users"))
            .andExpect(flash().attributeExists("error"))
        
        verify(userService).deleteById(1L)
    }
}