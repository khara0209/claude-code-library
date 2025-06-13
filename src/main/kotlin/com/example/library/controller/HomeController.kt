package com.example.library.controller

import com.example.library.service.BookService
import com.example.library.service.LoanService
import com.example.library.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomeController(
    private val bookService: BookService,
    private val userService: UserService,
    private val loanService: LoanService
) {
    
    @GetMapping("/")
    fun index(model: Model): String {
        model.addAttribute("totalBooks", bookService.findAll().size)
        model.addAttribute("availableBooks", bookService.findAvailableBooks().size)
        model.addAttribute("totalUsers", userService.findAll().size)
        model.addAttribute("activeLoans", loanService.findActiveLoans().size)
        return "index"
    }
}