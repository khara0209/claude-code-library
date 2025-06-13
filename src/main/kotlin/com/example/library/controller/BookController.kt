package com.example.library.controller

import com.example.library.entity.Book
import com.example.library.service.BookService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/books")
class BookController(private val bookService: BookService) {
    
    private val logger = LoggerFactory.getLogger(BookController::class.java)
    
    @GetMapping
    fun list(model: Model): String {
        model.addAttribute("books", bookService.findAll())
        return "books/list"
    }
    
    @GetMapping("/new")
    fun newBook(model: Model): String {
        model.addAttribute("book", Book())
        return "books/form"
    }
    
    @GetMapping("/{id}")
    fun detail(@PathVariable id: Long, model: Model): String {
        val book = bookService.findById(id)
        if (book == null) {
            return "redirect:/books"
        }
        model.addAttribute("book", book)
        return "books/detail"
    }
    
    @GetMapping("/{id}/edit")
    fun edit(@PathVariable id: Long, model: Model): String {
        val book = bookService.findById(id)
        if (book == null) {
            return "redirect:/books"
        }
        model.addAttribute("book", book)
        return "books/form"
    }
    
    @PostMapping
    fun save(@ModelAttribute book: Book, redirectAttributes: RedirectAttributes): String {
        try {
            logger.info("書籍保存開始: $book")
            bookService.save(book)
            logger.info("書籍保存成功: ID=${book.id}")
            redirectAttributes.addFlashAttribute("message", "書籍を保存しました")
        } catch (e: Exception) {
            logger.error("書籍保存エラー: ${e.message}", e)
            redirectAttributes.addFlashAttribute("error", "書籍の保存に失敗しました: ${e.message}")
        }
        return "redirect:/books"
    }
    
    @PostMapping("/{id}/delete")
    fun delete(@PathVariable id: Long, redirectAttributes: RedirectAttributes): String {
        try {
            if (bookService.deleteById(id)) {
                redirectAttributes.addFlashAttribute("message", "書籍を削除しました")
            } else {
                redirectAttributes.addFlashAttribute("error", "書籍の削除に失敗しました")
            }
        } catch (e: Exception) {
            redirectAttributes.addFlashAttribute("error", "書籍の削除に失敗しました")
        }
        return "redirect:/books"
    }
}