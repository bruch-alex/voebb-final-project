package com.example.voebb.service;

import com.example.voebb.service.impl.LanguageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LanguageServiceTest {

    @Mock
    private MessageSource messageSource;

    @Mock
    private LocaleResolver localeResolver;

    @InjectMocks
    private LanguageServiceImpl languageService;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    public void testGetMessage() {
        // Arrange
        String messageCode = "label.welcome";
        String expectedMessage = "Welcome to VÖBB";
        when(messageSource.getMessage(eq(messageCode), isNull(), any(Locale.class)))
                .thenReturn(expectedMessage);

        // Act
        String actualMessage = languageService.getMessage(messageCode);

        // Assert
        assertEquals(expectedMessage, actualMessage);
        verify(messageSource, times(1)).getMessage(eq(messageCode), isNull(), any(Locale.class));
    }

    @Test
    public void testGetMessageWithArgs() {
        // Arrange
        String messageCode = "book.borrow.success";
        Object[] args = new Object[]{"Harry Potter"};
        String expectedMessage = "Book Harry Potter borrowed successfully";
        when(messageSource.getMessage(eq(messageCode), eq(args), any(Locale.class)))
                .thenReturn(expectedMessage);

        // Act
        String actualMessage = languageService.getMessage(messageCode, args);

        // Assert
        assertEquals(expectedMessage, actualMessage);
        verify(messageSource, times(1)).getMessage(eq(messageCode), eq(args), any(Locale.class));
    }

    @Test
    public void testSetLocale() {
        // Arrange
        String language = "en";

        // Act
        languageService.setLocale(request, response, language);

        // Assert
        verify(localeResolver, times(1)).setLocale(eq(request), eq(response), any(Locale.class));
    }

    @Test
    public void testSetUnsupportedLocale() {
        // Arrange
        String language = "fr"; // Not in supported languages

        // Act
        languageService.setLocale(request, response, language);

        // Assert - Should not call localeResolver if language not supported
        verify(localeResolver, times(0)).setLocale(eq(request), eq(response), any(Locale.class));
    }

    @Test
    public void testGetAvailableLanguages() {
        // Act
        String[] languages = languageService.getAvailableLanguages();

        // Assert
        assertNotNull(languages);
        assertEquals(3, languages.length);
        assertEquals("de", languages[0]);
        assertEquals("en", languages[1]);
        assertEquals("ru", languages[2]);
    }
}