package com.example.voebb.controller.web;

import com.example.voebb.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/language")
public class LanguageController {

    private final LanguageService languageService;

    @Autowired
    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    /**
     * Endpoint to change the current language
     * @param language The language code (e.g., "en", "de", "ru")
     * @param request The HTTP request
     * @param response The HTTP response
     * @param referer The referring URL (to redirect back)
     * @return Redirect to the referring page or home
     */
    @PostMapping("/change")
    public String changeLanguage(
            @RequestParam("lang") String language,
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestHeader(value = "referer", required = false) String referer) {

        languageService.setLocale(request, response, language);

        // Redirect back to the referring page or home if referer is null
        return "redirect:" + (referer != null ? referer : "/");
    }
}