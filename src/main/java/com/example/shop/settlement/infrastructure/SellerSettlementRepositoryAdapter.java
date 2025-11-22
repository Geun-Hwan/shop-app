package com.example.shop.settlement.infrastructure;

import com.example.shop.settlement.domain.SellerSettlement;
import com.example.shop.settlement.domain.SellerSettlementRepository;
import com.example.shop.settlement.domain.SettlementStatus;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SellerSettlementRepositoryAdapter implements SellerSettlementRepository {

    private final SellerSettlementJpaRepository sellerSettlementJpaRepository;

    @Override
    public List<SellerSettlement> findByStatusAndSeller(SettlementStatus settlementStatus,
        UUID uuid) {
        return sellerSettlementJpaRepository.findByStatusAndSellerId(settlementStatus, uuid);
    }

    @Override
    public List<SellerSettlement> findByStatus(SettlementStatus settlementStatus) {
        return sellerSettlementJpaRepository.findByStatus(settlementStatus);
    }

    @Override
    public void saveAll(List<SellerSettlement> pending) {

        sellerSettlementJpaRepository.saveAll(pending);
    }
}
