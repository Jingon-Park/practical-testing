package sample.cafekiosk.spring.api.controller.product;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.ProductService;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;


@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    @DisplayName("신규 상품을 등록한다.")
    void createProduct() throws Exception {

        //given

        ProductCreateRequest request = ProductCreateRequest.builder()
            .type(HANDMADE)
            .sellingStatus(SELLING)
            .name("아메리카노")
            .price(4000).build();


        //when//then
        mockMvc.perform(
                post("/api/v1/product/new")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("신규 상품을 등록할 때에는 상품 타입은 필수값이다.")
    void createProductWithOutType() throws Exception {

        //given

        ProductCreateRequest request = ProductCreateRequest.builder()
            .sellingStatus(SELLING)
            .name("아메리카노")
            .price(4000).build();


        //when//then
        mockMvc.perform(
                post("/api/v1/product/new")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
            .andExpect(jsonPath("$.message").value("상품 타입은 필수입니다."));
    }

    @Test
    @DisplayName("신규 상품을 등록할 때에는 상품 판매 상테는 필수값이다.")
    void createProductWithOutStatus() throws Exception {

        //given

        ProductCreateRequest request = ProductCreateRequest.builder()
            .type(HANDMADE)
            .name("아메리카노")
            .price(4000).build();


        //when//then
        mockMvc.perform(
                post("/api/v1/product/new")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
            .andExpect(jsonPath("$.message").value("상품 판매 상태는 필수입니다."));
    }


    @Test
    @DisplayName("신규 상품을 등록할 때에는 상품 이름은 필수값이다.")
    void createProductWithOutName() throws Exception {

        //given

        ProductCreateRequest request = ProductCreateRequest.builder()
            .type(HANDMADE)
            .sellingStatus(SELLING)
            .price(4000).build();


        //when//then
        mockMvc.perform(
                post("/api/v1/product/new")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
            .andExpect(jsonPath("$.message").value("상품 이름은 필수입니다."));
    }

    @Test
    @DisplayName("신규 상품을 등록할 때에는 상품 가격은 양수이다.")
    void createProductWithOutZeroPrice() throws Exception {

        //given

        ProductCreateRequest request = ProductCreateRequest.builder()
            .type(HANDMADE)
            .sellingStatus(SELLING)
            .name("아메리카노")
            .price(0).build();


        //when//then
        mockMvc.perform(
                post("/api/v1/product/new")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
            .andExpect(jsonPath("$.message").value("상품 가격은 양수입니다."));
    }


    @Test
    @DisplayName("판매 상품을 조회한다.")
    void getSellingProductsTest() throws Exception {

        //given
        List<ProductResponse> result = List.of();

        when(productService.getSellingProducts()).thenReturn(result);
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
            .andExpect(jsonPath("$.data").isArray());

    }
}