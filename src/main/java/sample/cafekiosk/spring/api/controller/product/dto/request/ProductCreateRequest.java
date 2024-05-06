package sample.cafekiosk.spring.api.controller.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

@Getter
@NoArgsConstructor
public class ProductCreateRequest {


    @NotNull(message = "상품 타입은 필수입니다.")
    private ProductType type;
    @NotNull(message = "상품 판매 상태는 필수입니다.")
    private ProductSellingStatus sellingStatus;
    @NotBlank(message = "상품 이름은 필수입니다.")
    private String name;
    @Positive(message = "상품 가격은 양수입니다.")
    private int price;


    /**
     * 아래의 빌더는 테스트 코드에서만 사용한다.
     * 상용 코드에서 사용하지 않는 테스트만을 위한 메서드를 만들어도 되는지에 대해서 생각해보면
     * 만들어도 괜찮지만, 객체에서 해당 메서드가 존재하는 것이 어색하지 않는 선에서 만들어야한다.
     * 예로 Getter, 기본생성자, 빌더 등이 있다.
     */
    @Builder
    private ProductCreateRequest(ProductType type, ProductSellingStatus sellingStatus, String name,
        int price) {
        this.type = type;
        this.sellingStatus = sellingStatus;
        this.name = name;
        this.price = price;
    }
    public ProductCreateServiceRequest toServiceRequest() {

        return ProductCreateServiceRequest.builder()
            .type(type)
            .sellingStatus(sellingStatus)
            .name(name)
            .price(price)
            .build();
    }
}
