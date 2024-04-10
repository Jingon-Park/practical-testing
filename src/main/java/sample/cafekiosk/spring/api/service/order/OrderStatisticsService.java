package sample.cafekiosk.spring.api.service.order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.api.service.mail.MailService;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.order.OrderStatus;

@Service
@RequiredArgsConstructor
public class OrderStatisticsService {

    private final OrderRepository orderRepository;

    private final MailService mailService;
    public void sendOrderStatisticsMail(LocalDate localDate, String email) {

        //날짜의 결제 완료된 주문 목록을 가져온다
        List<Order> orders = orderRepository.findOrdersBy(localDate.atStartOfDay(),
            localDate.plusDays(1).atStartOfDay(),
            OrderStatus.PAYMENT_COMPLETED
        );

        //총 매출액을 계산한다.
        int totalAmount = orders.stream()
            .mapToInt(Order::getTotalPrice)
            .sum();

        //총 매출액 데이터를 메일로 전송한다.
        boolean result = this.mailService.sendEmail(
            "no-replay@cafekiosk.com"
            , email
            , String.format("[매출 통계] %s, 매출액", localDate)
            , String.format("총 매출액은 %d원 입니다.", totalAmount));

        if (!result) {
            throw new IllegalArgumentException("매출 통계 메일 발송에 실패했습니다.");
        }

    }

}
