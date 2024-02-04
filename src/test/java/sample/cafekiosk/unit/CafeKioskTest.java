package sample.cafekiosk.unit;

import org.junit.jupiter.api.Test;
import sample.cafekiosk.unit.beverage.Americano;

public class CafeKioskTest {

    /**
     * 아래의 테스트는 자동화된 테스트라고 할 수없음
     * 수동테스트이며, 실패할 수 없는 테스트임
     */
    @Test
    void add() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

        System.out.println(">>> 담긴 음료 갯 수: " + cafeKiosk.getBeverages().size());
        System.out.println(">>> 담긴 음료 이름: " + cafeKiosk.getBeverages().get(0).getName());

    }

}
