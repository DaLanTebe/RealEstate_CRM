package com.history.pricehistory.repository;

import com.history.pricehistory.entity.HistoryEstates;
import com.history.pricehistory.entity.HistoryEstates.PriceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface HistoryEstatesRepository extends JpaRepository<HistoryEstates, UUID> {

    HistoryEstates findHistoryEstatesByCadastrAndPriceType(@Param("cadastr") String cadastr, @Param("priceType") PriceType priceType);
}
