package hello.concurrencyissuestock.repository;

import hello.concurrencyissuestock.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
}
