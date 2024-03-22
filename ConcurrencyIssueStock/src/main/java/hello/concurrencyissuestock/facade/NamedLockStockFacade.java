package hello.concurrencyissuestock.facade;

import hello.concurrencyissuestock.repository.LockRepository;
import hello.concurrencyissuestock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NamedLockStockFacade {

    private final LockRepository lockRepository;
    private final StockService stockService;

    public void decrease(Long id, Long quantity) {
        try {
            lockRepository.getLock(id.toString());
            stockService.decrease(id, quantity);
        } finally {
            lockRepository.releaseLock(id.toString());
        }
    }
}
