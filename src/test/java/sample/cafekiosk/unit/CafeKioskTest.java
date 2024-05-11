package sample.cafekiosk.unit;

import static org.assertj.core.api.Assertions.*;

import java.beans.BeanDescriptor;
import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.cafekiosk.unit.beverage.Americano;
import sample.cafekiosk.unit.beverage.Latte;
import sample.cafekiosk.unit.order.Order;

public class CafeKioskTest {

    /**
     * 아래의 테스트는 자동화된 테스트라고 할 수없음
     * 수동테스트이며, 실패할 수 없는 테스트임
     */
    @Test
    void add_manual_test() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

        System.out.println(">>> 담긴 음료 갯 수: " + cafeKiosk.getBeverages().size());
        System.out.println(">>> 담긴 음료 이름: " + cafeKiosk.getBeverages().get(0).getName());

    }

    @DisplayName("음료를 1개 추가할 수 있다.")
    @Test
    void add() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

        assertThat(cafeKiosk.getBeverages()).hasSize(1);
        assertThat(cafeKiosk.getBeverages().get(0).getName()).isEqualTo("Americano");
    }

    @Test
    void remove() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        cafeKiosk.add(americano);

        assertThat(cafeKiosk.getBeverages()).hasSize(1);

        cafeKiosk.remove(americano);
        assertThat(cafeKiosk.getBeverages()).hasSize(0);

    }

    @Test
    void clear() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();
        cafeKiosk.add(americano);
        cafeKiosk.add(latte);

        assertThat(cafeKiosk.getBeverages()).hasSize(2);

        cafeKiosk.clear();
        assertThat(cafeKiosk.getBeverages()).isEmpty();

    }

    /**
     * BDD(Behavior Driven Development)
     * TDD에서 파상된 개발 방법으로 함수 단위의 테스트 보다는 시나리오에 기반한 TC에 집중하여 테스트를 진행
     * 개발자가 아닌 사람이 봐도 이해할 수 있는 수준으로 추상화 진행
     * Given = 시나리오 진행에 필요한 모든 준비 과정
     * When = 시나리오 행동 진행, 주로 1줄로 끝나는 경우가 많음
     * Then = 시나리오 진행 결과 검증
     */
    @DisplayName("주문 목록에 담긴 상품의 총 금액을 계산할 수 있다.")
    @Test
    void calculateTotalPrice() {
        //given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();

        cafeKiosk.add(americano);
        cafeKiosk.add(latte);

        //when
        int totalPrice = cafeKiosk.calculateTotalPrice();

        //then
        assertThat(totalPrice).isEqualTo(8500);
    }

    @Test
    void addSeveralBeverages() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano, 2);

        assertThat(cafeKiosk.getBeverages().get(0)).isEqualTo(americano);
        assertThat(cafeKiosk.getBeverages().get(1)).isEqualTo(americano);

        assertThat(cafeKiosk.getBeverages()).hasSize(2);

    }

    @Test
    void addZeroBeverages() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        assertThatThrownBy(() -> {
            cafeKiosk.add(americano, 0);
        }).isInstanceOf(IllegalArgumentException.class)
            .hasMessage("음료는 1잔 이상 주문할 수 있습니다.");
    }

//    @Test
//    void createOrder() {
//        CafeKiosk cafeKiosk = new CafeKiosk();
//        Americano americano = new Americano();
//        cafeKiosk.add(americano);
//
//        Order order = cafeKiosk.createOrder();
//
//        assertThat(order.getBeverages()).hasSize(1);
//        assertThat(order.getBeverages().get(0)).isEqualTo(americano);
//    }

    @Test
    void createOrderWithCurrentTime() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        cafeKiosk.add(americano);

        Order order = cafeKiosk.createOrder(LocalDateTime.of(2024, 02, 05, 12, 00));

        assertThat(order.getBeverages()).hasSize(1);
        assertThat(order.getBeverages().get(0)).isEqualTo(americano);
    }


    /**
     * 서비스의 도메인을 사용하면 좋음 EX. 영업 시간
     * 테스트현상을 중점으로 기술하는 것은 좋지 못함 EX. 특정 시간 이전에 주문을 생성하면 "실패한다". 등등
     * 테스트 행위에 대한 결과를 같이 display name에 기술한다.
     */
    @DisplayName("영업 시간 이외에 주문을 생성할 수 없다.")
    @Test
    void createOrderWithOutsideOpenTime() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        cafeKiosk.add(americano);

        assertThatThrownBy(() -> cafeKiosk.createOrder(LocalDateTime.of(2024, 02, 05, 0, 59)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("주문 시간이 아닙니다.");
    }
}
