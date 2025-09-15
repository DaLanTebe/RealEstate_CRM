package com.adapter.contentprocessor.repository;

import com.adapter.contentprocessor.entity.Estate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EstateRepository extends JpaRepository<Estate, UUID> {
}
