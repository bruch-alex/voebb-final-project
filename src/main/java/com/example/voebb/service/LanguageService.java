package com.example.voebb.service;

import com.example.voebb.model.entity.Language;

import java.util.List;

public interface LanguageService {

    Language findOrCreate(String name);

    List<Language> findAll();
    List<Language> findLanguagesByIds(List<Long> ids);
}
