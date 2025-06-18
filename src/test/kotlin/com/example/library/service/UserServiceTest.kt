package com.example.library.service

import com.example.library.entity.User
import com.example.library.mapper.UserMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*

@ExtendWith(MockitoExtension::class)
class UserServiceTest {
    
    @Mock
    private lateinit var userMapper: UserMapper
    
    private lateinit var userService: UserService
    
    @BeforeEach
    fun setUp() {
        userService = UserService(userMapper)
    }
    
    @Test
    fun `findAll should return all users`() {
        val users = listOf(
            User(1L, "Test User 1", "test1@example.com"),
            User(2L, "Test User 2", "test2@example.com")
        )
        whenever(userMapper.findAll()).thenReturn(users)
        
        val result = userService.findAll()
        
        assertEquals(2, result.size)
        assertEquals("Test User 1", result[0].name)
        assertEquals("test1@example.com", result[0].email)
        verify(userMapper).findAll()
    }
    
    @Test
    fun `findById should return user when exists`() {
        val user = User(1L, "Test User", "test@example.com")
        whenever(userMapper.findById(1L)).thenReturn(user)
        
        val result = userService.findById(1L)
        
        assertNotNull(result)
        assertEquals("Test User", result?.name)
        assertEquals("test@example.com", result?.email)
        verify(userMapper).findById(1L)
    }
    
    @Test
    fun `findById should return null when not exists`() {
        whenever(userMapper.findById(1L)).thenReturn(null)
        
        val result = userService.findById(1L)
        
        assertNull(result)
        verify(userMapper).findById(1L)
    }
    
    @Test
    fun `findByEmail should return user when email exists`() {
        val user = User(1L, "Test User", "test@example.com")
        whenever(userMapper.findByEmail("test@example.com")).thenReturn(user)
        
        val result = userService.findByEmail("test@example.com")
        
        assertNotNull(result)
        assertEquals("Test User", result?.name)
        assertEquals("test@example.com", result?.email)
        verify(userMapper).findByEmail("test@example.com")
    }
    
    @Test
    fun `findByEmail should return null when email not exists`() {
        whenever(userMapper.findByEmail("nonexistent@example.com")).thenReturn(null)
        
        val result = userService.findByEmail("nonexistent@example.com")
        
        assertNull(result)
        verify(userMapper).findByEmail("nonexistent@example.com")
    }
    
    @Test
    fun `save should insert new user when id is null`() {
        val user = User(null, "New User", "new@example.com")
        whenever(userMapper.insert(user)).thenReturn(1)
        
        val result = userService.save(user)
        
        assertEquals("New User", result.name)
        assertEquals("new@example.com", result.email)
        verify(userMapper).insert(user)
        verify(userMapper, never()).update(any())
    }
    
    @Test
    fun `save should update existing user when id is provided`() {
        val user = User(1L, "Updated User", "updated@example.com")
        whenever(userMapper.update(user)).thenReturn(1)
        
        val result = userService.save(user)
        
        assertEquals("Updated User", result.name)
        assertEquals("updated@example.com", result.email)
        verify(userMapper).update(user)
        verify(userMapper, never()).insert(any())
    }
    
    @Test
    fun `deleteById should return true when delete succeeds`() {
        whenever(userMapper.deleteById(1L)).thenReturn(1)
        
        val result = userService.deleteById(1L)
        
        assertTrue(result)
        verify(userMapper).deleteById(1L)
    }
    
    @Test
    fun `deleteById should return false when delete fails`() {
        whenever(userMapper.deleteById(1L)).thenReturn(0)
        
        val result = userService.deleteById(1L)
        
        assertFalse(result)
        verify(userMapper).deleteById(1L)
    }
}