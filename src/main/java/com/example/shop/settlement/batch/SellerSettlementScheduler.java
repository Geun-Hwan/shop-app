package com.example.shop.settlement.batch;

import com.example.shop.seller.domain.Seller;
import com.example.shop.seller.domain.SellerRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SellerSettlementScheduler {

    private final SellerRepository sellerRepository;
    private final JobLauncher jobLauncher;
    private final Job sellerSettlementJob;

    private final ThreadPoolTaskExecutor settlementTaskExecutor;

    @Value("${settlement.async.enabled}")
    private boolean settlementAsyncEnabled;

    @Scheduled(cron = "${spring.task.scheduling.cron.settlement}")
    public void runMidnightSettlements() {
        //  100개씩 조회
        Pageable pageable = Pageable.ofSize(100);

        Page<UUID> page;

        do {
            page = sellerRepository.findAll(pageable).map(Seller::getId);
            List<UUID> sellerIds = page.getContent();

            if (sellerIds.isEmpty()) {
                break;
            }

            sellerIds.forEach(this::runJonForSeller);
            pageable = page.hasNext() ? page.nextPageable() : Pageable.unpaged();

        } while (page.hasNext());
    }

    private void runJonForSeller(UUID sellerId) {
        try {
            Runnable executeJob = () -> {
                try {

                    JobParameters params = new JobParametersBuilder().addLong("timestamp",
                            System.currentTimeMillis()).addString("sellerId", sellerId.toString())
                        .toJobParameters();

                    jobLauncher.run(sellerSettlementJob, params);
                } catch (Exception ex) {
                    log.error("{}", sellerId);
                }
            };

            if (settlementAsyncEnabled) {
                settlementTaskExecutor.execute(executeJob);
            } else {
                executeJob.run();
            }
        } catch (Exception ex) {
            log.error("{}", sellerId);
        }
    }
}
