package sample.cafekiosk.spring.docs.product;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import sample.cafekiosk.spring.api.controller.product.ProductController;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.ProductService;
import sample.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.docs.RestDocsSupport;
import sample.cafekiosk.spring.domain.product.ProductType;

/**
 * TODO Get 요청으로 쿼리 파라미터가 있는 API에 대한 명세서도 만들어보자!!
 */
public class ProductControllerDocsTest extends RestDocsSupport {

    private final ProductService productService = Mockito.mock(ProductService.class);
    @Override
    protected Object initController() {
        return new ProductController(productService);
    }

    @DisplayName("신규 상품을 등록하는 API")
    @Test
    void createProduct() throws Exception {
        //given

        ProductCreateRequest request = ProductCreateRequest.builder()
            .type(HANDMADE)
            .sellingStatus(SELLING)
            .name("아메리카노")
            .price(4000).build();

        BDDMockito.given(
                productService.createProduct(BDDMockito.any(ProductCreateServiceRequest.class)))
            .willReturn(ProductResponse.builder()
                .id(1L)
                .productNumber("001")
                .type(HANDMADE)
                .name("아메리카노")
                .price(4000)
                .sellingStatus(SELLING)
                .build()
            );


        //when//then
        mockMvc.perform(
                post("/api/v1/product/new")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
        .andDo(document("product-create",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("type").type(JsonFieldType.STRING)
                    .description("상품 타입"),
                fieldWithPath("sellingStatus").type(JsonFieldType.STRING)
                    .optional()
                    .description("상품 판매 상태"),
                fieldWithPath("name").type(JsonFieldType.STRING)
                    .description("상품 이름"),
                fieldWithPath("price").type(JsonFieldType.NUMBER)
                    .description("상품 가격")
            ),
            responseFields(
                fieldWithPath("code").type(JsonFieldType.NUMBER)
                    .description("코드"),
                fieldWithPath("status").type(JsonFieldType.STRING)
                    .description("상태코드"),
                fieldWithPath("message").type(JsonFieldType.STRING)
                    .description("메시지"),
                fieldWithPath("data").type(JsonFieldType.OBJECT)
                    .description("데이터"),
                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                    .description("상품 id"),
                fieldWithPath("data.type").type(JsonFieldType.STRING)
                    .description("상품 타입"),
                fieldWithPath("data.productNumber").type(JsonFieldType.STRING)
                    .description("상품 번호"),
                fieldWithPath("data.sellingStatus").type(JsonFieldType.STRING)
                    .description("상품 판매 상태"),
                fieldWithPath("data.name").type(JsonFieldType.STRING)
                    .description("상품 이름"),
                fieldWithPath("data.price").type(JsonFieldType.NUMBER)
                    .description("상품 가격")
            )
            ));
    }


    @Test
    @DisplayName("판매 상품을 조회한다.")
    void getSellingProductsTest() throws Exception {

        //given
        ProductResponse product1 = ProductResponse.builder()
            .id(1L)
            .productNumber("001")
            .type(HANDMADE)
            .sellingStatus(SELLING)
            .name("아메리카노")
            .price(4500)
            .build();
        ProductResponse product2 = ProductResponse.builder()
            .id(1L)
            .productNumber("002")
            .type(HANDMADE)
            .sellingStatus(SELLING)
            .name("라떼")
            .price(5000)
            .build();
        List<ProductResponse> result = List.of(product1, product2);

        BDDMockito.given(productService.getSellingProducts()).willReturn(result);
        //when//then

        /**
         * selling 데이터를 검증하지 않고, data가 Array인지만 확인하는 이유는 데이터를 조회하는 로직은 이미
         * 서비스 layer 테스트에서 검증되었기 때문에 Presentation layer 에서는 Array가 잘 응답되는지만 검증한다.
         */
        mockMvc.perform(
                get("/api/v1/products/selling")
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"))
            .andExpect(jsonPath("$.data").isArray())
            .andDo(document("product-select",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("code").type(JsonFieldType.NUMBER)
                            .description("코드"),
                        fieldWithPath("status").type(JsonFieldType.STRING)
                            .description("상태코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING)
                            .description("메시지"),
                        fieldWithPath("data").type(JsonFieldType.ARRAY)
                            .description("데이터"),
                        fieldWithPath("data[].id").type(JsonFieldType.NUMBER)
                            .description("상품ID"),
                        fieldWithPath("data[].productNumber").type(JsonFieldType.STRING)
                            .description("상품번호"),
                        fieldWithPath("data[].type").type(JsonFieldType.STRING)
                            .description("상품타입"),
                        fieldWithPath("data[].sellingStatus").type(JsonFieldType.STRING)
                            .description("판매 상태"),
                        fieldWithPath("data[].name").type(JsonFieldType.STRING)
                            .description("상품이름"),
                        fieldWithPath("data[].price").type(JsonFieldType.NUMBER)
                            .description("상품가격")
                    )
                )
            );

    }
}
