package hello.concurrencyissuestock.service;

import hello.concurrencyissuestock.domain.Stock;
import hello.concurrencyissuestock.repository.StockRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StockServiceTest {

    @Autowired
    StockRepository repository;

    @Autowired
    StockService service;

    @BeforeEach
    void setUp() {
        repository.saveAndFlush(new Stock(1L, 100L));
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("재고 감소")
    void decreaseTest() {
        // when
        service.decrease(1L, 1L);

        // then
        Stock stock = repository.findById(1L).orElseThrow();
        Assertions.assertThat(stock.getQuantity()).isEqualTo(99);
    }
}
