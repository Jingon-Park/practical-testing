package sample.cafekiosk.spring.domain.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ProductSellingType {

    SELLING("판매중"),
    HOLD("판매보류"),
    STOP_SELLING("판매 중지");

    private final String text;
}