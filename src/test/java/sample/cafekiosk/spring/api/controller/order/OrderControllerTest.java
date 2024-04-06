package sample.cafekiosk.spring.api.controller.order;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import sample.cafekiosk.spring.api.service.order.OrderService;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;

@WebMvcTest(controllers = OrderController.class)
class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @Test
    @DisplayName("신규 주문을 생성한다.")
    void createOrder() throws Exception {

        //given

        OrderCreateRequest request = OrderCreateRequest.builder()
            .productNumbers(List.of("001"))
            .build();


        //when//then
        mockMvc.perform(
                post("/api/v1/orders/new")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
            .andExpect(jsonPath("$.message").value(HttpStatus.OK.name()));
    }
    @Test
    @DisplayName("신규 주문 생성 시 상품 번호 리스트트 1개 이상이다.")
    void createOrderWithOutProductList() throws Exception {

        //given

        OrderCreateRequest request = OrderCreateRequest.builder()
            .productNumbers(List.of())
            .build();


        //when//then
        mockMvc.perform(
                post("/api/v1/orders/new")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
            .andExpect(jsonPath("$.message").value("상품 번호 리스트는 필수 입니다."));
    }

}