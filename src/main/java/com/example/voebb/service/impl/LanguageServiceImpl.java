package com.example.voebb.service.impl;

import com.example.voebb.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Service
public class LanguageServiceImpl implements LanguageService {

    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;

    // Supported languages in the system
    private static final String[] AVAILABLE_LANGUAGES = {"de", "en", "ru"};

    @Autowired
    public LanguageServiceImpl(MessageSource messageSource, LocaleResolver localeResolver) {
        this.messageSource = messageSource;
        this.localeResolver = localeResolver;
    }

    @Override
    public String getMessage(String code) {
        return getMessage(code, null);
    }

    @Override
    public String getMessage(String code, Object[] args) {
        Locale locale = LocaleContextHolder.getLocale();
        return getMessage(code, locale, args);
    }

    @Override
    public String getMessage(String code, Locale locale) {
        return getMessage(code, locale, null);
    }

    private String getMessage(String code, Locale locale, Object[] args) {
        try {
            return messageSource.getMessage(code, args, locale);
        } catch (Exception e) {
            // If message not found, return the code as fallback
            return code;
        }
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, String language) {
        // Validate if the language is supported
        if (isSupportedLanguage(language)) {
            Locale locale = new Locale(language);
            localeResolver.setLocale(request, response, locale);
        }
    }

    @Override
    public String[] getAvailableLanguages() {
        return AVAILABLE_LANGUAGES;
    }

    private boolean isSupportedLanguage(String language) {
        for (String supportedLang : AVAILABLE_LANGUAGES) {
            if (supportedLang.equals(language)) {
                return true;
            }
        }
        return false;
    }
}