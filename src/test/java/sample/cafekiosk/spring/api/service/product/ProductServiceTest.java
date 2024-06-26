package sample.cafekiosk.spring.api.service.product;

import static org.assertj.core.api.Assertions.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.*;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.IntegrationTestSupport;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;


class ProductServiceTest extends IntegrationTestSupport{

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        /**
         * 모든 테스트를 수행하기 전에 내용을 수행
         * 자주 사용할 일이 없음
         */

        //사용하기전 체크 사항
        // 각 테스트 코드의 입장에서 setUp내용을 알 필요가 없는지?
        // 수정되더라도 모든 테스트 코드에 영향이 없는지??

        /**
         * 테스트 코드의 문서적인 관점에 봤을때, given절에 들어가는 Fixture 들이 많은 테스트 코드에서 곂치더라도
         * 각 테스트 코드의 given절에 작성하는 것이 테스트 코드의 흐름을 이해하기에도 좋고 문서로의 역할도 수행하기에 좋음
         */
    }
    @AfterEach
    void tearDown() {
        this.productRepository.deleteAllInBatch();

    }

    @DisplayName("신규 상품을 등록한다. 신규 상품 번호는 가장 마지막 등록된 상품의 상품 번호 + 1 한 값이다.")
    @Test
    void createProduct() {

        //given
        Product product1 = createProduct("001", SELLING, HANDMADE, "아메리카노", 4000);
        productRepository.save(product1);

        ProductCreateRequest productCreateRequest = ProductCreateRequest.builder()
            .sellingStatus(SELLING)
            .type(HANDMADE)
            .name("카푸치노")
            .price(5000)
            .build();

        //when
        ProductResponse productResponse = this.productService.createProduct(productCreateRequest.toServiceRequest());

        //then
        assertThat(productResponse).extracting(
                "productNumber",
                "type",
                "sellingStatus",
                "name",
                "price"
            )
            .contains("002", SELLING, HANDMADE, "카푸치노", 5000);

        List<Product> products = productRepository.findAll();

        assertThat(products).hasSize(2)
            .extracting("productNumber",
                "type",
                "sellingStatus",
                "name",
                "price"
            ).containsExactlyInAnyOrder(
                tuple("001", HANDMADE, SELLING, "아메리카노", 4000),
                tuple("002", HANDMADE, SELLING, "카푸치노", 5000)
            );
    }

    @DisplayName("신규 상품을 등록할때, 등록된 상품이 없으면 신규 상품의 상품 번호는 001 이다.")
    @Test
    void createProductWhenEmpty() {

        //given
        ProductCreateRequest productCreateRequest = ProductCreateRequest.builder()
            .sellingStatus(SELLING)
            .type(HANDMADE)
            .name("카푸치노")
            .price(5000)
            .build();

        //when
        ProductResponse productResponse = this.productService.createProduct(productCreateRequest.toServiceRequest());

        //then
        assertThat(productResponse).extracting(
                "productNumber",
                "type",
                "sellingStatus",
                "name",
                "price"
            )
            .contains("001", SELLING, HANDMADE, "카푸치노", 5000);

        List<Product> products = productRepository.findAll();

        assertThat(products).hasSize(1)
            .extracting("productNumber",
                "type",
                "sellingStatus",
                "name",
                "price"
            ).contains(tuple("001", HANDMADE, SELLING, "카푸치노", 5000));
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