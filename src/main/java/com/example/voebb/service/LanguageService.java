package com.example.voebb.service;

import com.example.voebb.model.entity.Language;

import java.util.List;

public interface LanguageService {

    Language findOrCreate(String name);

    List<Language> getAllLanguages();

    List<Language> getLanguagesByIds(List<Long> ids);

}
