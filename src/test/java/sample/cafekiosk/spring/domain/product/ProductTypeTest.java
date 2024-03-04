package sample.cafekiosk.spring.domain.product;


import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class ProductTypeTest {

    @DisplayName("재고 관련 상품 타입인지 확인한다.")
    @Test
    void containsStockTypeForTrue() {
        //given
        ProductType givenType = ProductType.BAKERY;


        //when
        boolean result = ProductType.containsStockType(givenType);

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("재고 관련 상품 타입인지 확인한다.")
    @Test
    void containsStockTypeForFalse() {
        //given
        ProductType givenType = ProductType.HANDMADE;


        //when
        boolean result = ProductType.containsStockType(givenType);

        //then
        assertThat(result).isFalse();
    }
}