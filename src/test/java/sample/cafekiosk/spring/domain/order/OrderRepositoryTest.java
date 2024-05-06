package sample.cafekiosk.spring.domain.order;


import static org.assertj.core.api.Assertions.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.HOLD;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.STOP_SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.IntegrationTestSupport;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

@Transactional
class OrderRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    void tearDown() {
        this.productRepository.deleteAllInBatch();
        this.orderRepository.deleteAllInBatch();

    }
    @Test
    @DisplayName("시작 날짜와 종료 날짜, 주문 상태로 주문을 검색한다.")
    void findOrdersBy() {
        //given

        List<Product> products = productRepository.findAll();

        LocalDateTime localDateTime = LocalDateTime.of(2024, Month.APRIL, 1, 12, 30);
        Order order = Order.builder().products(products).orderStatus(OrderStatus.PAYMENT_COMPLETED)
            .registeredDateTime(localDateTime).build();

        LocalDate localDate = LocalDate.of(2024, Month.APRIL, 1);
        order.setOrderStatus(OrderStatus.PAYMENT_COMPLETED);
        orderRepository.save(order);

        //when
        List<Order> orders = orderRepository.findOrdersBy(localDate.atStartOfDay(),
            localDate.plusDays(1).atStartOfDay(), OrderStatus.PAYMENT_COMPLETED);

        //then
        assertThat(orders).hasSize(1)
            .extracting(
                "orderStatus"
            ).containsExactlyInAnyOrder(OrderStatus.PAYMENT_COMPLETED);
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