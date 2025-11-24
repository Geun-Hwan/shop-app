package com.example.shop.settlement.domain;

import java.util.List;
import java.util.UUID;

public interface SellerSettlementRepository {

    List<SellerSettlement> findByStatusAndSeller(SettlementStatus settlementStatus, UUID uuid);

    List<SellerSettlement> findByStatus(SettlementStatus settlementStatus);

    void saveAll(List<SellerSettlement> pending);
}
