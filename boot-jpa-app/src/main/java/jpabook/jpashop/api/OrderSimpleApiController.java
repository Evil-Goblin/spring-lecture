package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * xToOne ( Order <-> Member ManyToOne , Order <-> Delivery OneToOne )
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> orderV1() {
        // Order 는 Member를 갖고 있기 때문에 Member를 가져오고 Member는 다시 Order를 가지고 있기 때문에 무한 루프에 빠진다.
        // 양방향 연관관계의 경우 한쪽은 @JsonIgnore 를 해줘야한다.
        // 그냥 @JsonIgnore를 한다고 해결이 되지 않는다.
        // Fetch 가 Lazy로 설정이 되어있기 때문에 Member 와 같은 연관관계의 객체들이 프록시 객체인 채로 존재하게 되는데 이 프록시 객체는 실제 객체가 아니라서 에러가 발생한다.(bytebuddy)
        // Hibernate5module 모듈로 해결할 수는 있지만 좋은 방법은 아니다.
        // 아잇 또 jakarta javax 문제로 안된다...굳이 해결하진 않겠다.
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName(); // Lazy 강제 초기화
            order.getDelivery().getAddress(); // Lazy 강제 초기화
        }
        return all;
    }

    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() { // 리스트로 반환하는 것은 확장성 등의 측면에서 안좋지만 예제임으로 넘어간다.(MemberApiController 참조)
        List<Order> allByString = orderRepository.findAllByString(new OrderSearch());
        List<SimpleOrderDto> collect = allByString.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        // Order 의 조회 결과는 2개가 나온다 (init db data 참고) -> 루프는 두바퀴 돌게 된다.
        // 각 파퀴당 SimpleOrderDto 를 생성한다. -> 생성자 내부에서 Lazy 로딩이 초기화 된다.
        // Order 조회 1번 Order 2개에 대해서 Member, Delivery 가 각각 수행되어 총 5개의 쿼리가 수행된다. (N+1문제)


        return collect;
    }

    @GetMapping("/api/v3/simple-orders") // fetch join 으로 쿼리를 한번만 나가게 함으로서 최적화한다.
    public List<SimpleOrderDto> ordersV3() {
        List<Order> allWithMemberDelivery = orderRepository.findAllWithMemberDelivery(0, 0);
        return allWithMemberDelivery.stream()
                .map(SimpleOrderDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() { // 의존성은 단방향으로 흐르는게 좋다 (Controller -> Service -> Repository) OrderSimpleQueryDto가 Repository 패키지 에 있는 이유
        return orderSimpleQueryRepository.findOrderDtos(); // Dto 로 쿼리하기 때문에 보다 적은 데이터를 가져옴으로서 보다 최적화된다.
        // 하지만 특정 dto를 리턴하기 때문에 재사용성이 떨어진다.
        // Order를 가져와서 Dto로 변환하는 것이 재사용성에서 보다 낫지만 쿼리자체의 성능은 Dto로 바로 쿼리하는 것이 빠르다.
        // 하지만 또 Dto로 쿼리하면 쿼리가 복잡해진다.

        // 이 경우 repository 가 api에 종속되어버리는 문제가 있다.
        // 그렇기 때문에 repository 를 분리해버리는 것이 낫다.
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }
}
