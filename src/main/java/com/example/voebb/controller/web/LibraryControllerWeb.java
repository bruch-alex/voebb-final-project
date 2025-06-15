package com.example.voebb.controller.web;

import com.example.voebb.service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/libraries")
public class LibraryControllerWeb {
    private final LibraryService libraryService;

    @GetMapping
    public String getIndexPage(){
        return "public/library/index";
    }

    @GetMapping("/{id}")
    public String getLibraryInfoPage(@PathVariable Long id,
            Model model){
        model.addAttribute("editLibraryDTO", libraryService.getLibraryById(id));
        return "public/library/info";
    }

}
