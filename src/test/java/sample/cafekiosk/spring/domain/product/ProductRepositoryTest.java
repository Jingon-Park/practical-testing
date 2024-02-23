package sample.cafekiosk.spring.domain.product;

import static org.assertj.core.api.Assertions.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.*;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
//SpringBootTest
@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("원하는 판매 상태인 Product만 검색")
    void findAllBySellingStatusInTest() {
        //given
        Product product1 = Product.builder()
            .productNumber("001")
            .sellingStatus(SELLING)
            .type(HANDMADE)
            .name("아메리카노")
            .price(4000)
            .build();
        Product product2 = Product.builder()
            .productNumber("002")
            .sellingStatus(HOLD)
            .type(HANDMADE)
            .name("라때")
            .price(4500)
            .build();
        Product product3 = Product.builder()
            .productNumber("003")
            .sellingStatus(STOP_SELLING)
            .type(HANDMADE)
            .name("팥빙수")
            .price(7000)
            .build();

        productRepository.saveAll(List.of(product1, product2, product3));
        //when

        List<Product> products = productRepository.findAllBySellingStatusIn(List.of(SELLING, HOLD));

        //then
        assertThat(products).hasSize(2)
            .extracting("productNumber", "name", "sellingStatus")
            .containsExactlyInAnyOrder(
                tuple(product1.getProductNumber(), product1.getName(), product1.getSellingStatus()),
                tuple(product2.getProductNumber(), product2.getName(), product2.getSellingStatus())

            );

    }

}