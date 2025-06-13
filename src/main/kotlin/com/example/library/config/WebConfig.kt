package com.example.library.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.InitBinder
import java.beans.PropertyEditorSupport

@Configuration
@ControllerAdvice
class WebConfig {
    
    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.registerCustomEditor(String::class.java, object : PropertyEditorSupport() {
            override fun setAsText(text: String?) {
                value = if (text.isNullOrBlank()) null else text.trim()
            }
        })
    }
}