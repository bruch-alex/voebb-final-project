package com.example.voebb.repository;

import com.example.voebb.model.entity.CreatorRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CreatorRoleRepo extends JpaRepository<CreatorRole, Long> {

    Optional<CreatorRole> findByCreatorRoleNameIgnoreCase(String role);

    List<CreatorRole> findTop5ByCreatorRoleNameContainingIgnoreCase(String name);
}
