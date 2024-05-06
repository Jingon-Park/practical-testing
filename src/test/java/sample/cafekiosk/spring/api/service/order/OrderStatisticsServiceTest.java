package sample.cafekiosk.spring.api.service.order;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import sample.cafekiosk.spring.IntegrationTestSupport;
import sample.cafekiosk.spring.client.mail.MailClient;
import sample.cafekiosk.spring.domain.history.MailSendHistory;
import sample.cafekiosk.spring.domain.history.MailSendHistoryRepository;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.order.OrderStatus;
import sample.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductType;

class OrderStatisticsServiceTest extends IntegrationTestSupport {

    @Autowired
    private OrderStatisticsService orderStatisticsService;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MailSendHistoryRepository mailSendHistoryRepository;

//    @MockBean
//    private MailClient mailClient;

    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        mailSendHistoryRepository.deleteAllInBatch();

    }

    @Test
    @DisplayName("결제 완료 주문을 날짜로 검색하여 매출 통계 메일을 전송한다.")
    void sendOrderStatisticsMail() {

        //given
        LocalDateTime orderTime = LocalDateTime.of(2024, 04, 01, 10, 00);
        Product product1 = createProduct(ProductType.HANDMADE, "001", 1000);
        Product product2 = createProduct(ProductType.HANDMADE, "002", 2000);
        Product product3 = createProduct(ProductType.HANDMADE, "003", 3000);
        List<Product> products = List.of(product1, product2, product3);

        productRepository.saveAll(products);
        Order order1 = createPaymentedOrder(LocalDateTime.of(2024, 4, 2, 0, 0), products);
        Order order2 = createPaymentedOrder(orderTime, products);
        Order order3 = createPaymentedOrder(orderTime, products);
        Order order4 = createPaymentedOrder(LocalDateTime.of(2024, 3, 31, 0, 0), products);

        orderRepository.saveAll(List.of(order1, order2, order3, order4));

        //stubbing
        when(mailClient.sendEmail(any(String.class), any(String.class), any(String.class), any(
            String.class))).thenReturn(true);

        //when
        boolean result = orderStatisticsService.sendOrderStatisticsMail(LocalDate.of(2024, 04, 01),
            "test@test.com");

        //then
        assertThat(result).isTrue();

        List<MailSendHistory> histories = mailSendHistoryRepository.findAll();
        assertThat(histories).hasSize(1)
            .extracting("content")
            .contains("총 매출액은 12000원 입니다.");

    }

    private Order createPaymentedOrder(LocalDateTime orderTime, List<Product> products) {
        Order order = Order.builder()
            .products(products)
            .orderStatus(OrderStatus.PAYMENT_COMPLETED)
            .registeredDateTime(orderTime)
            .build();

        return orderRepository.save(order);
    }


    private Product createProduct(ProductType type, String productNumber, int price) {
        return Product.builder()
            .type(type)
            .productNumber(productNumber)
            .sellingStatus(SELLING)
            .price(price)
            .name("메뉴 이름")
            .build();
    }
}