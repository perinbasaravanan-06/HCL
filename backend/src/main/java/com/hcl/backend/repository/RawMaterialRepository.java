package com.hcl.backend.repository;

import com.hcl.backend.Entity.RawMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RawMaterialRepository extends JpaRepository<RawMaterial, Long> {
    List<RawMaterial> findByQuantityAvailableLessThanEqual(double reorderLevel);
}
