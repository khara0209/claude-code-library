package com.example.library.controller

import com.example.library.service.BookService
import com.example.library.service.LoanService
import com.example.library.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/loans")
class LoanController(
    private val loanService: LoanService,
    private val bookService: BookService,
    private val userService: UserService
) {
    
    private val logger = LoggerFactory.getLogger(LoanController::class.java)
    
    @GetMapping
    fun list(model: Model): String {
        val loans = loanService.findAll()
        val loansWithDetails = loans.map { loan ->
            val book = bookService.findById(loan.bookId)
            val user = userService.findById(loan.userId)
            mapOf(
                "loan" to loan,
                "book" to book,
                "user" to user
            )
        }
        model.addAttribute("loansWithDetails", loansWithDetails)
        return "loans/list"
    }
    
    @GetMapping("/new")
    fun newLoan(
        @RequestParam(required = false) bookId: Long?,
        @RequestParam(required = false) userId: Long?,
        model: Model
    ): String {
        model.addAttribute("availableBooks", bookService.findAvailableBooks())
        model.addAttribute("users", userService.findAll())
        model.addAttribute("preselectedBookId", bookId)
        model.addAttribute("preselectedUserId", userId)
        return "loans/form"
    }
    
    @PostMapping
    fun save(
        @RequestParam bookId: Long,
        @RequestParam userId: Long,
        @RequestParam(defaultValue = "14") loanPeriodDays: Int,
        redirectAttributes: RedirectAttributes
    ): String {
        try {
            logger.info("貸し出し処理開始: bookId=$bookId, userId=$userId, period=$loanPeriodDays")
            val loan = loanService.loanBook(bookId, userId, loanPeriodDays)
            if (loan != null) {
                logger.info("貸し出し成功: loanId=${loan.id}")
                redirectAttributes.addFlashAttribute("message", "書籍を貸し出しました")
            } else {
                logger.warn("貸し出し失敗: 書籍が利用できません bookId=$bookId")
                redirectAttributes.addFlashAttribute("error", "貸し出しに失敗しました（書籍が利用できません）")
            }
        } catch (e: Exception) {
            logger.error("貸し出しエラー: ${e.message}", e)
            redirectAttributes.addFlashAttribute("error", "貸し出しに失敗しました: ${e.message}")
        }
        return "redirect:/loans"
    }
    
    @PostMapping("/{id}/return")
    fun returnBook(@PathVariable id: Long, redirectAttributes: RedirectAttributes): String {
        try {
            logger.info("返却処理開始: loanId=$id")
            if (loanService.returnBook(id)) {
                logger.info("返却成功: loanId=$id")
                redirectAttributes.addFlashAttribute("message", "書籍を返却しました")
            } else {
                logger.warn("返却失敗: loanId=$id")
                redirectAttributes.addFlashAttribute("error", "返却に失敗しました")
            }
        } catch (e: Exception) {
            logger.error("返却エラー: ${e.message}", e)
            redirectAttributes.addFlashAttribute("error", "返却に失敗しました: ${e.message}")
        }
        return "redirect:/loans"
    }
}