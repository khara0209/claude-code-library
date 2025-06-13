package com.example.library.mapper

import com.example.library.entity.Book
import org.apache.ibatis.annotations.*

@Mapper
interface BookMapper {
    
    @Select("SELECT * FROM books ORDER BY id")
    fun findAll(): List<Book>
    
    @Select("SELECT * FROM books WHERE id = #{id}")
    fun findById(id: Long): Book?
    
    @Select("SELECT * FROM books WHERE is_available = true ORDER BY id")
    fun findAvailableBooks(): List<Book>
    
    @Insert("""
        INSERT INTO books (title, author, isbn, publisher, published_year, category, is_available)
        VALUES (#{title}, #{author}, #{isbn}, #{publisher}, #{publishedYear}, #{category}, #{available})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    fun insert(book: Book): Int
    
    @Update("""
        UPDATE books SET 
        title = #{title}, 
        author = #{author}, 
        isbn = #{isbn}, 
        publisher = #{publisher}, 
        published_year = #{publishedYear}, 
        category = #{category}, 
        is_available = #{available},
        updated_at = CURRENT_TIMESTAMP
        WHERE id = #{id}
    """)
    fun update(book: Book): Int
    
    @Update("UPDATE books SET is_available = #{available} WHERE id = #{id}")
    fun updateAvailability(id: Long, available: Boolean): Int
    
    @Delete("DELETE FROM books WHERE id = #{id}")
    fun deleteById(id: Long): Int
}