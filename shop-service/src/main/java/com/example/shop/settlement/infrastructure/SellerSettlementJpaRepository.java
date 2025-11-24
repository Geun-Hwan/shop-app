package com.example.shop.settlement.infrastructure;

import com.example.shop.settlement.domain.SellerSettlement;
import com.example.shop.settlement.domain.SettlementStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerSettlementJpaRepository extends JpaRepository<SellerSettlement, UUID> {

    List<SellerSettlement> findByStatus(SettlementStatus status);

    List<SellerSettlement> findByStatusAndSellerId(SettlementStatus status, UUID sellerId);
}
