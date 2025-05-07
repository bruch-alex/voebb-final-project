package com.example.voebb.repository;

import com.example.voebb.model.entity.CreatorProductRelation;
import com.example.voebb.model.entity.CreatorRelationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreatorProductRelationRepo extends JpaRepository<CreatorProductRelation, CreatorRelationId> {

    List<CreatorProductRelation> findByProductId(Long productId);

}
