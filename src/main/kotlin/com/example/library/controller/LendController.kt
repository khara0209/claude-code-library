package com.example.library.controller

import com.example.library.service.BookService
import com.example.library.service.LendService
import com.example.library.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/lends")
class LendController(
    private val lendService: LendService,
    private val bookService: BookService,
    private val userService: UserService
) {
    
    private val logger = LoggerFactory.getLogger(LendController::class.java)
    
    @GetMapping
    fun list(model: Model): String {
        val lends = lendService.findAll()
        val lendsWithDetails = lends.map { lend ->
            val book = bookService.findById(lend.bookId)
            val user = userService.findById(lend.userId)
            mapOf(
                "lend" to lend,
                "book" to book,
                "user" to user
            )
        }
        model.addAttribute("lendsWithDetails", lendsWithDetails)
        return "lends/list"
    }
    
    @GetMapping("/new")
    fun newLend(
        @RequestParam(required = false) bookId: Long?,
        @RequestParam(required = false) userId: Long?,
        model: Model
    ): String {
        model.addAttribute("availableBooks", bookService.findAvailableBooks())
        model.addAttribute("users", userService.findAll())
        model.addAttribute("preselectedBookId", bookId)
        model.addAttribute("preselectedUserId", userId)
        return "lends/form"
    }
    
    @PostMapping
    fun save(
        @RequestParam bookId: Long,
        @RequestParam userId: Long,
        @RequestParam(defaultValue = "14") lendPeriodDays: Int,
        redirectAttributes: RedirectAttributes
    ): String {
        try {
            logger.info("貸し出し処理開始: bookId=$bookId, userId=$userId, period=$lendPeriodDays")
            val lend = lendService.lendBook(bookId, userId, lendPeriodDays)
            if (lend != null) {
                logger.info("貸し出し成功: lendId=${lend.id}")
                redirectAttributes.addFlashAttribute("message", "書籍を貸し出しました")
            } else {
                logger.warn("貸し出し失敗: 書籍が利用できません bookId=$bookId")
                redirectAttributes.addFlashAttribute("error", "貸し出しに失敗しました（書籍が利用できません）")
            }
        } catch (e: Exception) {
            logger.error("貸し出しエラー: ${e.message}", e)
            redirectAttributes.addFlashAttribute("error", "貸し出しに失敗しました: ${e.message}")
        }
        return "redirect:/lends"
    }
    
    @PostMapping("/{id}/return")
    fun returnBook(@PathVariable id: Long, redirectAttributes: RedirectAttributes): String {
        try {
            logger.info("返却処理開始: lendId=$id")
            if (lendService.returnBook(id)) {
                logger.info("返却成功: lendId=$id")
                redirectAttributes.addFlashAttribute("message", "書籍を返却しました")
            } else {
                logger.warn("返却失敗: lendId=$id")
                redirectAttributes.addFlashAttribute("error", "返却に失敗しました")
            }
        } catch (e: Exception) {
            logger.error("返却エラー: ${e.message}", e)
            redirectAttributes.addFlashAttribute("error", "返却に失敗しました: ${e.message}")
        }
        return "redirect:/lends"
    }
}