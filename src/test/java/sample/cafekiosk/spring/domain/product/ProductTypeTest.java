package sample.cafekiosk.spring.domain.product;


import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;


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


    /**
     * 아래의 테스트와 같이 여러 케이스에 대한 테스트를 한번에 수행하고자할 때에는 parameterized test를 활용하면 좋다
     */
    @Test
    void containsStockType3() {

        //given
        ProductType type1 = ProductType.BAKERY;
        ProductType type2 = ProductType.BOTTLE;
        ProductType type3 = ProductType.HANDMADE;
        //when
        boolean result1 = ProductType.containsStockType(type1);
        boolean result2 = ProductType.containsStockType(type2);
        boolean result3 = ProductType.containsStockType(type3);
        //then
        assertThat(result1).isTrue();
        assertThat(result2).isTrue();
        assertThat(result3).isFalse();

    }


    @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
    @ParameterizedTest
    @CsvSource({"HANDMADE, false", "BOTTLE, true", "BAKERY, true"})
    void containsStockType4(ProductType productType, boolean expected) {
        //when
        boolean result = ProductType.containsStockType(productType);
        //then

        assertThat(result).isEqualTo(expected);
    }

    private static Stream<Arguments> provideProductTypesForCheckingStockType() {
        return Stream.of(
            Arguments.of(ProductType.HANDMADE, false),
            Arguments.of(ProductType.BAKERY, true),
            Arguments.of(ProductType.BOTTLE, true)
        );
    }

    @DisplayName("상품 타입이 재고 관련 타입인지 체크한다.")
    @ParameterizedTest
    @MethodSource("provideProductTypesForCheckingStockType")
    void containsStockType5(ProductType productType, boolean expected) {
        //when
        boolean result = ProductType.containsStockType(productType);
        //then
        assertThat(result).isEqualTo(expected);
    }
}