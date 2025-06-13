package com.example.library.controller

import com.example.library.entity.User
import com.example.library.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/users")
class UserController(private val userService: UserService) {
    
    private val logger = LoggerFactory.getLogger(UserController::class.java)
    
    @GetMapping
    fun list(model: Model): String {
        model.addAttribute("users", userService.findAll())
        return "users/list"
    }
    
    @GetMapping("/new")
    fun newUser(model: Model): String {
        model.addAttribute("user", User())
        return "users/form"
    }
    
    @GetMapping("/{id}")
    fun detail(@PathVariable id: Long, model: Model): String {
        val user = userService.findById(id)
        if (user == null) {
            return "redirect:/users"
        }
        model.addAttribute("user", user)
        return "users/detail"
    }
    
    @GetMapping("/{id}/edit")
    fun edit(@PathVariable id: Long, model: Model): String {
        val user = userService.findById(id)
        if (user == null) {
            return "redirect:/users"
        }
        model.addAttribute("user", user)
        return "users/form"
    }
    
    @PostMapping
    fun save(@ModelAttribute user: User, redirectAttributes: RedirectAttributes): String {
        try {
            logger.info("ユーザー保存開始: $user")
            userService.save(user)
            logger.info("ユーザー保存成功: ID=${user.id}")
            redirectAttributes.addFlashAttribute("message", "ユーザーを保存しました")
        } catch (e: Exception) {
            logger.error("ユーザー保存エラー: ${e.message}", e)
            redirectAttributes.addFlashAttribute("error", "ユーザーの保存に失敗しました: ${e.message}")
        }
        return "redirect:/users"
    }
    
    @PostMapping("/{id}/delete")
    fun delete(@PathVariable id: Long, redirectAttributes: RedirectAttributes): String {
        try {
            if (userService.deleteById(id)) {
                redirectAttributes.addFlashAttribute("message", "ユーザーを削除しました")
            } else {
                redirectAttributes.addFlashAttribute("error", "ユーザーの削除に失敗しました")
            }
        } catch (e: Exception) {
            redirectAttributes.addFlashAttribute("error", "ユーザーの削除に失敗しました")
        }
        return "redirect:/users"
    }
}