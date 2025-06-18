package com.example.library.service

import com.example.library.entity.User
import com.example.library.mapper.UserMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(private val userMapper: UserMapper) {
    
    fun findAll(): List<User> = userMapper.findAll()
    
    fun findById(id: Long): User? = userMapper.findById(id)
    
    fun findByEmail(email: String): User? = userMapper.findByEmail(email)
    
    fun save(user: User): User {
        return if (user.id == null) {
            // Check for email uniqueness before inserting new user
            val existingUser = userMapper.findByEmail(user.email)
            if (existingUser != null) {
                throw RuntimeException("Email address '${user.email}' is already registered")
            }
            userMapper.insert(user)
            user
        } else {
            // Check for email uniqueness before updating existing user
            val existingUser = userMapper.findByEmail(user.email)
            if (existingUser != null && existingUser.id != user.id) {
                throw RuntimeException("Email address '${user.email}' is already registered")
            }
            userMapper.update(user)
            user
        }
    }
    
    fun deleteById(id: Long): Boolean = userMapper.deleteById(id) > 0
}