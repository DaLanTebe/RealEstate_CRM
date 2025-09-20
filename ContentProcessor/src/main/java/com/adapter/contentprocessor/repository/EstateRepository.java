package com.adapter.contentprocessor.repository;

import com.adapter.contentprocessor.entity.Estate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EstateRepository extends JpaRepository<Estate, UUID> {
    Optional<Estate> findByCadastr(String cadastr);

    List<Estate> findAllByCadastrIn(Collection<String> cadastrList);
}
