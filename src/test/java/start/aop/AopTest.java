package start.aop;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import start.aop.order.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@Slf4j
@SpringBootTest
//@Import(Aspect1.class)
//@Import(Aspect2.class)
//@Import(Aspect3.class)
//@Import(Aspect4.class)
//@Import({Aspect5.LogAspect.class, Aspect5.TxAspect.class})
@Import(Aspect6.class)
public class AopTest {
    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    void aopInfo() {
        log.info("isAopProxy, orderService={}", AopUtils.isAopProxy(orderService));
        log.info("isAopProxy, orderRepository={}", AopUtils.isAopProxy(orderRepository));
    }

    @Test
    void success() {
        orderService.orderItem("itemA");
        // Aspect1 실행 순서 : logging(OrderService.orderItem(String)) ->
        // OrderService.orderItem("itemA") ->
        // logging(OrderRepository.save(String)) ->
        // OrderRepository.save("itemA")

        // Aspect2 실행 순서 : Aspect1과 같음

        // Aspect3 실행 순서 : doTransaction(OrderService.orderItem(String) (트랜잭션 시작) ->
        // logging(OrderService.orderItem(String) ->
        // OrderService.orderItem("itemA") ->
        // logging(OrderRepository.save(String) ->
        // OrderRepository.save("itemA") ->
        // doTransaction(OrderService.orderItem(String) (트랜잭션 커밋) ->
        // doTransaction(OrderService.orderItem(String) (리소스 릴리즈)

        // Aspect4 실행 순서 : Aspect3과 같음

        // Aspect5 실행 순서 : Aspect3과 같음
    }

    @Test
    void exception() {
        assertThatThrownBy(() -> orderService.orderItem("ex"))
                .isInstanceOf(IllegalStateException.class);
    }
}
