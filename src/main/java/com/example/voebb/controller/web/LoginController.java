package com.example.voebb.controller.web;

import com.example.voebb.model.dto.user.UserRegistrationDTO;
import com.example.voebb.service.impl.CustomUserDetailsService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {
    private final CustomUserDetailsService customUserDetailsService;

    public LoginController(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("userRegistrationDTO", new UserRegistrationDTO());
        return "public/login-register/register-page";
    }

    @PostMapping("/register")
    public String postRegisterPage(@ModelAttribute("userRegistrationDTO") @Valid UserRegistrationDTO userRegistrationDTO,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "public/login-register/register-page";
        }

        if (customUserDetailsService.emailExists(userRegistrationDTO.getEmail())) {
            bindingResult.rejectValue("email", "error.email", "Email is already taken");
            return "public/login-register/register-page";
        }

        if (customUserDetailsService.phoneNumberExists(userRegistrationDTO.getPhoneNumber())) {
            bindingResult.rejectValue("phoneNumber", "error.phoneNumber", "Phone number is already taken");
            return "public/login-register/register-page";
        }

        try {
            customUserDetailsService.registerUser(userRegistrationDTO);
            redirectAttributes.addFlashAttribute("success", "Register successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/";
    }

    @GetMapping("/login")
    public String getLoginPage(@RequestParam(value = "error", required = false) String error,
                               Model model) {
        if (error != null) {
            model.addAttribute("loginError", "Invalid username or password.");
        }

        return "public/login-register/login-page";
    }
}
