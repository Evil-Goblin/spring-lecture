package hello.concurrencyissuestock.service;

import hello.concurrencyissuestock.domain.Stock;
import hello.concurrencyissuestock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OptimisticLockStockService {

    private final StockRepository stockRepository;

    @Transactional
    public void decrease(Long id, Long quantity) {
        Stock stock = stockRepository.findByIdWithOptimisticLock(id).orElseThrow();

        stock.decrease(quantity);
    }
}
