package sample.cafekiosk.spring.domain.stock;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

class StockTest {

    @Test
    @DisplayName("재고의 수량이 제공된 수 보다 작은를 확인한다.")
    void isQuantityLessThen() {
        //given
        Stock stock = Stock.builder()
            .productNumber("001")
            .quantity(1)
            .build();
        int quantity = 2;
        //when
        boolean result = stock.isQuantityLessThen(quantity);
        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("재고를 주어진 수만큼 줄일 수 있다.")
    void deductQuantity() {
        //given
        Stock stock = Stock.builder()
            .productNumber("001")
            .quantity(1)
            .build();
        int quantity = 1;
        //when
        stock.deductQuantity(quantity);
        //then
        assertThat(stock.getQuantity()).isEqualTo(0);
    }

    @Test
    @DisplayName("재고보다 더 많은 수를 줄이려고 하면 예외가 발생한다.")
    void deductQuantityOverTest() {
        //given
        Stock stock = Stock.builder()
            .productNumber("001")
            .quantity(1)
            .build();
        int quantity = 2;
        //when//then
        assertThatThrownBy(() -> stock.deductQuantity(quantity))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("차감할 재고 수량이 없습니다.");
    }

    @DisplayName("재고 차감 시나리오")
    @TestFactory
    Collection<DynamicTest> stockDeductionDynamicTest() {
        //given
        Stock stock = Stock.create("001", 1);

        return List.of(
            DynamicTest.dynamicTest("재고를 주어진 개수만큼 차감할 수 있다.", () -> {

                //given
                int quantity = 1;
                //when
                stock.deductQuantity(1);

                //then
                assertThat(stock.getQuantity()).isZero();
            }),
            DynamicTest.dynamicTest("재고보다 많은 수의 수량을 차감 시도하면 예외가 발생한다.", () -> {
                //given
                int quantity = 2;
                //when//then
                assertThatThrownBy(() -> stock.deductQuantity(quantity))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("차감할 재고 수량이 없습니다.");
            })
        );
    }
}