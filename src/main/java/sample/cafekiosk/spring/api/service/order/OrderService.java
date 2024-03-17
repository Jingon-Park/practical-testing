package sample.cafekiosk.spring.api.service.order;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.api.service.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;

    /**
     * 재고 감소 라는 문제는 동시성 문제에 대해서 고민해야한다.
     * optimistic lock / pessimistic lock 과 같은 lock 개념 사용을 고려
     */
    public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime registeredDataTime) {

        List<String> productNumbers = request.getProductNumbers();
        //product
        List<Product> products = findProductsBy(productNumbers);

        deductStokcQuantities(products);

        Order order = Order.create(products, registeredDataTime);
        Order savedOrder = orderRepository.save(order);
        //order
        return OrderResponse.of(savedOrder);
    }

    private void deductStokcQuantities(List<Product> products) {
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
