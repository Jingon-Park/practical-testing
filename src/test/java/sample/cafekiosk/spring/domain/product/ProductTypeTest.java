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

    @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
    @Test
    void containsStockTypeEx() {

        //given
        //ProductType을 순회하기 위해 배열로 추출
        ProductType[] productTypes = ProductType.values();

        /**
         * 아래와 같이 테스트 코드의 의도를 파악하기 위한 논리 구조(for, if)가 들어가는 경우
         * 테스트 코드를 읽는 사람이 테스트 코드의 의미를 파악하기 위해 이해하는 과정이 필요하다.
         * 하나의 테스트 코드 문단에는 하나의 경우를 확인하기 위한 테스트 코드만을 포함하는 것이 바람직하며
         * 여러 케이스를 테스트 하기 위해서는 매개변수를 전달하는 방법을 고려하는 것이 낫다.
         */
        for (ProductType productType : productTypes) {
            if (productType == ProductType.HANDMADE) {
                //when
                boolean result = ProductType.containsStockType(productType);
                //then
                assertThat(result).isFalse();
            }

            if (productType == ProductType.BAKERY || productType == ProductType.BOTTLE) {
                //when
                boolean result = ProductType.containsStockType(productType);

                //then
                assertThat(result).isTrue();
            }
        }
    }
}