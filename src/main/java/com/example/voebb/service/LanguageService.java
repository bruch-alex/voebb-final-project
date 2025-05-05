package com.example.voebb.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public interface LanguageService {

    /**
     * Get a message in the current locale
     * @param code The message code from properties file
     * @return The localized message
     */
    String getMessage(String code);

    /**
     * Get a message with arguments in the current locale
     * @param code The message code from properties file
     * @param args Arguments to be inserted into the message
     * @return The localized message with arguments
     */
    String getMessage(String code, Object[] args);

    /**
     * Get a message in a specific locale
     * @param code The message code from properties file
     * @param locale The specific locale to use
     * @return The localized message
     */
    String getMessage(String code, Locale locale);

    /**
     * Set the current locale for the user session
     * @param request The HTTP request
     * @param response The HTTP response
     * @param language The language code (e.g., "en", "de", "ru")
     */
    void setLocale(HttpServletRequest request, HttpServletResponse response, String language);

    /**
     * Get all available languages
     * @return Array of supported language codes
     */
    String[] getAvailableLanguages();
}