package com.example.library.mapper

import com.example.library.entity.Loan
import org.apache.ibatis.annotations.*

@Mapper
interface LoanMapper {
    
    @Select("SELECT * FROM loans ORDER BY id DESC")
    fun findAll(): List<Loan>
    
    @Select("SELECT * FROM loans WHERE id = #{id}")
    fun findById(id: Long): Loan?
    
    @Select("SELECT * FROM loans WHERE status = 'ACTIVE' ORDER BY due_date")
    fun findActiveLoans(): List<Loan>
    
    @Select("SELECT * FROM loans WHERE user_id = #{userId} ORDER BY loan_date DESC")
    fun findByUserId(userId: Long): List<Loan>
    
    @Select("SELECT * FROM loans WHERE book_id = #{bookId} ORDER BY loan_date DESC")
    fun findByBookId(bookId: Long): List<Loan>
    
    @Insert("""
        INSERT INTO loans (book_id, user_id, loan_date, due_date, status)
        VALUES (#{bookId}, #{userId}, #{loanDate}, #{dueDate}, #{status})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    fun insert(loan: Loan): Int
    
    @Update("""
        UPDATE loans SET 
        return_date = #{returnDate}, 
        status = #{status},
        updated_at = CURRENT_TIMESTAMP
        WHERE id = #{id}
    """)
    fun updateReturn(id: Long, returnDate: java.time.LocalDate, status: String): Int
    
    @Delete("DELETE FROM loans WHERE id = #{id}")
    fun deleteById(id: Long): Int
}