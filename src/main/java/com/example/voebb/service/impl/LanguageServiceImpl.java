package com.example.voebb.service.impl;

import com.example.voebb.model.entity.Language;
import com.example.voebb.repository.LanguageRepo;
import com.example.voebb.service.LanguageService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepo languageRepo;

    public LanguageServiceImpl(LanguageRepo languageRepo) {
        this.languageRepo = languageRepo;
    }

    @Transactional
    @Override
    public Language findOrCreate(String name) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Language name must be non-empty");
        }

        String sanitizedName = name.trim();

        return languageRepo.findByNameIgnoreCase(sanitizedName)
                .orElseGet(() -> {
                    Language lang = new Language();
                    lang.setName(sanitizedName);
                    return languageRepo.save(lang);
                });
    }

    @Override
    public List<Language> findAll() {
        return languageRepo.findAll() ;
    }

    @Override
    public List<Language> findLanguagesByIds(List<Long> ids) {
        return languageRepo.findAllById(ids);
    }
}
