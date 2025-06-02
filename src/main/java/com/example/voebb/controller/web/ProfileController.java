package com.example.voebb.controller.web;

import com.example.voebb.model.dto.user.UserUpdateDTO;
import com.example.voebb.service.impl.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {

    private final CustomUserDetailsService userDetailsService;

    @GetMapping
    public String getProfilePage(Model model, Principal principal) {
        model.addAttribute("userUpdateDTO", userDetailsService.getUserDTOByUsername(principal.getName()));
        return "public/user/profile";
    }

    @PostMapping("/edit")
    public String postEditUser(@ModelAttribute("userUpdateDTO") @Valid UserUpdateDTO userUpdateDTO,
                               BindingResult bindingResult,
                               Principal principal,
                               HttpServletRequest request,
                               HttpServletResponse response,
                               Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userUpdateDTO", userUpdateDTO);
            return "public/user/profile"; // Show form again with validation errors
        }
        userDetailsService.updateUserInfo(userUpdateDTO, principal.getName(), request, response);
        return "redirect:/profile";
    }
}
