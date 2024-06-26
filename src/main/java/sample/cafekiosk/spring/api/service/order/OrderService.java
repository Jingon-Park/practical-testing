package sample.cafekiosk.spring.api.service.order;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.service.order.request.OrderCreateServiceRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;

/**
 * Transactional 의 readOnly 옵션을 true로 설정하면 CRUD 작업 중 read 동작 수행한다.
 * 즉 JPA 관점에서 스냅샷과 변경 감지로직이 없어, 성능상의 이점이 있다.
 *
 * CQRS -> command /read 를 분리하여 관리
 * command(CUD)작업을 수행할때에는 Transactional readOnly false를 주고
 * read 작업을 수행하때에는 readOnly 를 true로 주어 분리할 수있다.
 * 이때, master와 slave db를 분리할 수 있어 장애를 따로 관리할 수 있음
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;

    /**
     * 재고 감소 라는 문제는 동시성 문제에 대해서 고민해야한다.
     * optimistic lock / pessimistic lock 과 같은 lock 개념 사용을 고려
     */
    @Transactional
    public OrderResponse createOrder(OrderCreateServiceRequest request, LocalDateTime registeredDateTime) {

        List<String> productNumbers = request.getProductNumbers();
        //product
        List<Product> products = findProductsBy(productNumbers);

        deductStockQuantities(products);

        Order order = Order.create(products, registeredDateTime);
        Order savedOrder = orderRepository.save(order);
        //order
        return OrderResponse.of(savedOrder);
    }

    private void deductStockQuantities(List<Product> products) {
        //재고 차감이 필요한 상품 필터
        List<String> stockProductNumbers = extractStockProductNumbers(products);

        //재고 엔티티 조회
        Map<String, Stock> stockMap = createStokcMapBy(stockProductNumbers);

        //상품별 갯수 확인
        Map<String, Long> productCountingMap = createCountingMapBy(stockProductNumbers);

        //재고 차감
        for (String stockProductNumber : new HashSet<>(stockProductNumbers)) {
            Stock stock = stockMap.get(stockProductNumber);
            int quantity = productCountingMap.get(stockProductNumber).intValue();

            if (stock.isQuantityLessThen(quantity)) {
                throw new IllegalArgumentException("재고가 부족한 상품이 있습니다.");
            }
            stock.deductQuantity(quantity);

        }
    }

    private static Map<String, Long> createCountingMapBy(List<String> stockProductNumbers) {
        return stockProductNumbers.stream()
            .collect(Collectors.groupingBy(p -> p, Collectors.counting()));
    }

    private Map<String, Stock> createStokcMapBy(List<String> stockProductNumbers) {
        List<Stock> stocks = stockRepository.findAllByProductNumberIn(stockProductNumbers);
        Map<String, Stock> stockMap = stocks.stream()
            .collect(Collectors.toMap(Stock::getProductNumber, p -> p));
        return stockMap;
    }

    private static List<String> extractStockProductNumbers(List<Product> products) {
        return products.stream()
            .filter(product -> ProductType.containsStockType(product.getType()))
            .map(Product::getProductNumber)
            .collect(Collectors.toList());
    }

    private List<Product> findProductsBy(List<String> productNumbers) {
        List<Product> products = productRepository.findAllByProductNumberIn(
            productNumbers);
        Map<String, Product> productMap = products.stream()
            .collect(Collectors.toMap(Product::getProductNumber, p -> p));

        return productNumbers.stream().map(productMap::get).collect(
            Collectors.toList());

    }

}
