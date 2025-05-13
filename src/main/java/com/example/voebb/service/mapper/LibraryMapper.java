package com.example.voebb.service.mapper;

import com.example.voebb.model.dto.LibraryDTO;
import com.example.voebb.model.entity.Address;
import com.example.voebb.model.entity.Library;
import org.springframework.stereotype.Component;

@Component
public class LibraryMapper {

    public LibraryDTO convertToDTO(Library library) {
        if (library == null) {
            return null;
        }

        Address address = library.getAddress();
        LibraryDTO dto = new LibraryDTO();
        dto.setId(library.getId());
        dto.setName(library.getName());
        dto.setDescription(library.getDescription());

        if (address != null) {
            dto.setCity(address.getCity());
            dto.setDistrict(address.getDistrict());
            dto.setPostcode(address.getPostcode());
            dto.setStreet(address.getStreet());
            dto.setHouseNr(address.getHouseNr());
            dto.setOsmLink(address.getOsmLink());
        }

        return dto;
    }

    public Library convertToEntity(LibraryDTO dto) {
        if (dto == null) {
            return null;
        }

        Library library = new Library();
        library.setId(dto.getId());
        library.setName(dto.getName());
        library.setDescription(dto.getDescription());

        Address address = new Address();
        address.setCity(dto.getCity());
        address.setDistrict(dto.getDistrict());
        address.setPostcode(dto.getPostcode());
        address.setStreet(dto.getStreet());
        address.setHouseNr(dto.getHouseNr());
        address.setOsmLink(dto.getOsmLink());
        library.setAddress(address);

        return library;
    }
}