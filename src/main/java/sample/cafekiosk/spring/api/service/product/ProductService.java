package sample.cafekiosk.spring.api.service.product;

import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductNumberFactory productNumberFactory;

    public List<ProductResponse> getSellingProducts() {

        List<Product> products = productRepository.findAllBySellingStatusIn(
            forDisplay());

        return products.stream()
            .map(ProductResponse::of)
            .collect(Collectors.toList());
    }

    /**
     * Controller의 DTO를 서비스 단에서 사용하더라도 구현은 가능
     * 단, 기능 분리, 책임, 역할 분리 측면에서 각 레이어에서 상위 레이어에 의존할 필요는 없음
     * 상위 레이어인 Controller는 Service 레이어를 알고있는 것은 당연하지만, 하위 레이어인 Service는 알 필요 없음
     * @param request
     * @return
     */
    @Transactional
    public ProductResponse createProduct(ProductCreateServiceRequest request) {

        String nextProductNumber = productNumberFactory.createNextProductNumber();

        Product savedProduct = productRepository.save(request.toEntity(nextProductNumber));

        return ProductResponse.of(savedProduct);
    }

//    private String createNextProductNumber() {
//        String latestProductNumber = this.productRepository.findLatestProductNumber();
//
//        if (latestProductNumber == null) {
//            return "001";
//        }
//
//        int latestProductNumberInt = Integer.parseInt(latestProductNumber);
//
//        int nextProductNumber = latestProductNumberInt + 1;
//
//        return String.format("%03d", nextProductNumber);
//
//    }
}
