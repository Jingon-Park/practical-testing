package sample.cafekiosk.spring.domain.product;

import static org.assertj.core.api.Assertions.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.*;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.IntegrationTestSupport;

@Transactional
class ProductRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("원하는 판매 상태인 Product만 검색")
    void findAllBySellingStatusInTest() {
        //given
        Product product1 = createProduct("001", SELLING, HANDMADE, "아메리카노", 4000);
        Product product2 = createProduct("002", HOLD, HANDMADE, "라때", 4500);
        Product product3 = createProduct("003", STOP_SELLING, HANDMADE, "팥빙수", 7000);

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

    @Test
    @DisplayName("상품 번호로 싱품을 조회한다.")
    void findAllByProductNumberInTest() {
        //given
        Product product1 = createProduct("001", SELLING, HANDMADE, "아메리카노", 4000);
        Product product2 = createProduct("002", HOLD, HANDMADE, "라때", 4500);
        Product product3 = createProduct("003", STOP_SELLING, HANDMADE, "팥빙수", 7000);

        productRepository.saveAll(List.of(product1, product2, product3));
        //when

        List<Product> products = productRepository.findAllByProductNumberIn(List.of("001", "002"));

        //then
        assertThat(products).hasSize(2)
            .extracting("productNumber", "name", "sellingStatus")
            .containsExactlyInAnyOrder(
                tuple(product1.getProductNumber(), product1.getName(), product1.getSellingStatus()),
                tuple(product2.getProductNumber(), product2.getName(), product2.getSellingStatus())

            );
    }


    @DisplayName("가장 마지막에 저장된 Product의 product_number 값을 구한다.")
    @Test
    void findLatestProductNumber() {
        //given
        String targetProductNumber = "003";
        Product product1 = createProduct("001", SELLING, HANDMADE, "아메리카노", 4000);
        Product product2 = createProduct("002", HOLD, HANDMADE, "라때", 4500);
        Product product3 = createProduct(targetProductNumber, STOP_SELLING, HANDMADE, "팥빙수", 7000);

        this.productRepository.saveAll(List.of(product1, product2, product3));

        //when
        String latestProductNumber = this.productRepository.findLatestProductNumber();

        //then

        assertThat(latestProductNumber).isEqualTo(targetProductNumber);
    }

    @DisplayName("가장 마지막에 저장된 Product의 product_number 값을 구할때, Product가 없으면 null을 반환한다.")
    @Test
    void findLatestProductNumberWhenProductEmpty() {
        //when
        String latestProductNumber = this.productRepository.findLatestProductNumber();

        //then

        assertThat(latestProductNumber).isNull();
    }

    private Product createProduct(String productNumber, ProductSellingStatus status,
        ProductType productType, String name, int price) {
        return Product.builder()
            .productNumber(productNumber)
            .sellingStatus(status)
            .type(productType)
            .name(name)
            .price(price)
            .build();
    }
}