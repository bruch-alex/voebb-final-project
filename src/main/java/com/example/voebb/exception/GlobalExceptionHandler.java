package com.example.voebb.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    // TODO: decide on UI: popup or re-direct to error page
    @ExceptionHandler(CreatorNotFoundException.class)
    public String handleCreatorNotFound(CreatorNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/404";
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFoundException(RuntimeException ex, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return getRedirectPathFromRequest(request);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public String handleItemNotFoundException(RuntimeException ex, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return getRedirectPathFromRequest(request);
    }

    @ExceptionHandler(ItemUnavailableException.class)
    public String handleItemUnavailableException(RuntimeException ex, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return getRedirectPathFromRequest(request);
    }

    @ExceptionHandler(UserBorrowLimitExceededException.class)
    public String handleUserBorrowLimitExceededException(RuntimeException ex, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return getRedirectPathFromRequest(request);
    }

    @ExceptionHandler(ItemStatusNotFoundException.class)
    public String handleItemStatusNotFoundException(RuntimeException ex, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return getRedirectPathFromRequest(request);
    }

    @ExceptionHandler(BorrowNotFoundException.class)
    public String handleBorrowNotFoundException(RuntimeException ex, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return getRedirectPathFromRequest(request);
    }

    @ExceptionHandler(BorrowExtensionLimitReachedException.class)
    public String handleBorrowExtensionLimitReachedException(RuntimeException ex, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return getRedirectPathFromRequest(request);
    }

    private String getRedirectPathFromRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (uri.contains("/borrowings")) {
            return "redirect:/admin/borrowings";
        }
        return "redirect:/admin/reservations";

    }
    @ExceptionHandler(EmailAlreadyUsedException.class)
    public String handleEmailAlreadyUsed(EmailAlreadyUsedException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());

        return "redirect:/register";
    }


}
