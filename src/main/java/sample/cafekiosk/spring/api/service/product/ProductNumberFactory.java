package sample.cafekiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sample.cafekiosk.spring.domain.product.ProductRepository;


/**
 * Private 메소드에 대한 테스트 코드를 수행하고자 하는 생각이 강하게 든다면 객체를 아래의 ProductNumberFactory와 같이
 * 새로운 객체를 만들어 역할을 분리해야하는 시기가 아닌지 먼저 검토를 해야한다.
 * private 메소드는 public 메소드를 검증하면서 자연스럽게 검증이 되도록하며, private 메소드만 따로 테스트를 작성하고 싶은
 * 생각이 강하게 든다면 해당 객체의 역할이 너무 커져서 객체를 분리해야하는 시기가 온 것일 수 있다.
 */
@Component
@RequiredArgsConstructor
public class ProductNumberFactory {

    private ProductRepository productRepository;


    public String createNextProductNumber() {
        String latestProductNumber = this.productRepository.findLatestProductNumber();

        if (latestProductNumber == null) {
            return "001";
        }

        int latestProductNumberInt = Integer.parseInt(latestProductNumber);

        int nextProductNumber = latestProductNumberInt + 1;

        return String.format("%03d", nextProductNumber);

    }}
