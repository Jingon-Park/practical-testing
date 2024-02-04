package sample.cafekiosk.unit.beverage;


import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class AmericanoTest {

    @Test
    void getName() {
        Americano americano = new Americano();

        assertThat(americano.getName()).isEqualTo("Americano");
    }

    @Test
    void getPrivate() {
        Americano americano = new Americano();
        assertThat(americano.getPrice()).isEqualTo(4000);
    }


}