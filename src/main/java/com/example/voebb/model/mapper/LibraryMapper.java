package com.example.voebb.model.mapper;

import com.example.voebb.model.dto.library.CreateLibraryDTO;
import com.example.voebb.model.dto.library.EditLibraryDTO;
import com.example.voebb.model.dto.library.FullInfoLibraryDTO;
import com.example.voebb.model.entity.Library;

public class LibraryMapper {
    public static EditLibraryDTO toEditDTO(Library library) {
        return new EditLibraryDTO(
                library.getId(),
                library.getName(),
                library.getDescription(),
                library.getAddress().getPostcode(),
                library.getAddress().getCity(),
                library.getAddress().getDistrict(),
                library.getAddress().getStreet(),
                library.getAddress().getHouseNr(),
                library.getAddress().getOsmLink()
        );
    }


    public static Library toNewEntity(CreateLibraryDTO dto) {
        Library library = new Library();
        library.setName(dto.getName());
        library.setDescription(dto.getDescription());
        library.setAddress(AddressMapper.toEntity(dto));
        return library;
    }

    public static FullInfoLibraryDTO toFullInfoDTO(Library library) {
        return new FullInfoLibraryDTO(
                library.getId(),
                library.getName(),
                library.getDescription(),
                library.getAddress().getPostcode(),
                library.getAddress().getCity(),
                library.getAddress().getDistrict(),
                library.getAddress().getStreet(),
                library.getAddress().getHouseNr(),
                library.getAddress().getOsmLink()
        );
    }
}
