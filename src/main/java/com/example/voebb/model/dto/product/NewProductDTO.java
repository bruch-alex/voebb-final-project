package com.example.voebb.model.dto.product;

import com.example.voebb.model.dto.creator.CreatorWithRoleDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewProductDTO {

    String productType;          // sanitized String: "book", "ebook", "movie"
    String title;
    String releaseYear;
    String photo;
    String description;
    String productLinkToEmedia;

    NewBookDetailsDTO bookDetails;   // nullable for non-books

    List<CreatorWithRoleDTO> creators = new ArrayList<>();

//    List<String> countryNames;
//
//    public List<String> getCountries() {
//        return countryNames;
//    }

    List<Long> countryIds;
    List<Long> languageIds;

}
