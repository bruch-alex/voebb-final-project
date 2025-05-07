package com.example.voebb.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // TODO: decide on UI: popup or re-direct to error page
    @ExceptionHandler(CreatorNotFoundException.class)
    public String handleCreatorNotFound(CreatorNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/404";
    }
}
