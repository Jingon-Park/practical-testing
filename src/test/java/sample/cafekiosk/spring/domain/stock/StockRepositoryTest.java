package sample.cafekiosk.spring.domain.stock;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
class StockRepositoryTest {

    @Autowired
    private StockRepository stockRepository;


    @Test
    @DisplayName("삼품 번호 목록으로 재고를 조회한다.")
    void findStockByProductNumbers() {

        //given

        Stock stock1 = Stock.builder()
            .productNumber("001")
            .quantity(1)
            .build();
        Stock stock2 = Stock.builder()
            .productNumber("002")
            .quantity(2)
            .build();
        Stock stock3 = Stock.builder()
            .productNumber("003")
            .quantity(3)
            .build();
        stockRepository.saveAll(List.of(stock1, stock2, stock3));
        //when

        List<Stock> stocks = stockRepository.findAllByProductNumberIn(
            List.of("001", "002"));

        //then

        assertThat(stocks).hasSize(2)
            .extracting("productNumber", "quantity")
            .containsExactlyInAnyOrder(
                tuple("001", 1),
                tuple("002", 2)

            );
    }
}