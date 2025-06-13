package com.example.library.mapper

import com.example.library.entity.User
import org.apache.ibatis.annotations.*

@Mapper
interface UserMapper {
    
    @Select("SELECT * FROM users ORDER BY id")
    fun findAll(): List<User>
    
    @Select("SELECT * FROM users WHERE id = #{id}")
    fun findById(id: Long): User?
    
    @Select("SELECT * FROM users WHERE email = #{email}")
    fun findByEmail(email: String): User?
    
    @Insert("""
        INSERT INTO users (name, email, phone)
        VALUES (#{name}, #{email}, #{phone})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    fun insert(user: User): Int
    
    @Update("""
        UPDATE users SET 
        name = #{name}, 
        email = #{email}, 
        phone = #{phone},
        updated_at = CURRENT_TIMESTAMP
        WHERE id = #{id}
    """)
    fun update(user: User): Int
    
    @Delete("DELETE FROM users WHERE id = #{id}")
    fun deleteById(id: Long): Int
}