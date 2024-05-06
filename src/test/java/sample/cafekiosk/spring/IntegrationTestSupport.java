package sample.cafekiosk.spring;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.client.mail.MailClient;

/**
 * 테스트를 수행하는 것도 비용이기 때문에 스프링 실행과 같이 시간이 오래걸리는 환경을 세팅하는 시간을 줄려 테스트 비용을 줄이는 것 또한
 * 중요하다. 동일한 환경을 사용하는 경우 아래와 같은 통합 테스트를 위한 상위 class 를 만들어 테스트 수행 시 비용이 많이 발생하는
 * 내용 실행을 최소화하여 테스트 수행 시간을 줄여보자
 */
@ActiveProfiles("test")
@SpringBootTest
public abstract class IntegrationTestSupport {

    @MockBean
    protected MailClient mailClient;

}
