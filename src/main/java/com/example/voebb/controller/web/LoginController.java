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

@Controller
public class LoginController {
    private final CustomUserDetailsService customUserDetailsService;

    public LoginController(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("userRegistrationDTO", new UserRegistrationDTO("", "", "", ""));
        return "public/login_register/register-page";
    }

    @PostMapping("/register")
    public String postRegisterPage(@ModelAttribute("userRegistrationDTO") @Valid UserRegistrationDTO userRegistrationDTO,
                                   BindingResult result) {
        if (result.hasErrors()) {
            return "public/login_register/register-page";
        }
        customUserDetailsService.registerUser(userRegistrationDTO);
        return "redirect:/";
    }
}
