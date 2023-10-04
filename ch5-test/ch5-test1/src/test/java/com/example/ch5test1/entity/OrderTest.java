package com.example.ch5test1.entity;

import com.example.ch5test1.constant.ItemSellStatus;
import com.example.ch5test1.repository.ItemRepository;
import com.example.ch5test1.repository.MemberRepository;
import com.example.ch5test1.repository.OrderItemRepository;
import com.example.ch5test1.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(locations="classpath:application-test.properties")
@Transactional
class OrderTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    //중간 저장소 관리 : 중간저장소 -> db / 중간저장소 clear
    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    public Item createItem(){
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("상세설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());

        item.setUpdateTime(LocalDateTime.now());
        return item;
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest() {

        Order order = new Order();

        for(int i=0;i<3;i++){
            Item item = this.createItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();

            //중요
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            
            //중요
            order.getOrderItems().add(orderItem);
        }

        //주문 엔티티 클래스의 중간테이블에 저장 및 실제 테이블에 저장
        orderRepository.saveAndFlush(order);
        //중간 테이블 비우기
        // 조회시, 연관 관계 테이블이 조인되면서, 같이 참조 되는 부분 확인
        em.clear();

        // 조회시 중간 테이블의 내용이 비워져서, 실제 테이블에서 내용 가져올 때
        //참조하는 테이블이 뭐가 되는지 봐야됨
        //현재 lazy, 지연로딩이지만
        // 즉시 로딩 eager, 이걸로 설정되는 경우, 연관 없는 테이블도 
        //전부 다 조회 해서 성능상 이슈 발생
        Order savedOrder = orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(3, savedOrder.getOrderItems().size());
    }

    public Order createOrder(){
        // 주문 ( 주문_상품 을 요소로 하는 리스트, 멤버) ->
        // 주문_상품 추가 ->
        // 상품 아이템 추가 ->
        // 주문 상품 아이템들을 요소로 가지는 리스트에 추가 ->
        // 회원 추가 ->
        // 주문, 회원 추가(주문자)

        Order order = new Order();
        for(int i=0;i<3;i++){
            Item item = createItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }
        Member member = new Member();
        memberRepository.save(member);
        order.setMember(member);
        orderRepository.save(order);
        return order;
    }

    @Test
    @DisplayName("고아객체 제거 테스트")
    public void orphanRemovalTest(){
        Order order = this.createOrder();
        order.getOrderItems().remove(0);
        em.flush();
    }

    @Test
    @DisplayName("지연 로딩 테스트")
    public void lazyLoadingTest(){
        Order order = this.createOrder();
        Long orderItemId = order.getOrderItems().get(0).getId();
        // 실제 디비에 반영
        em.flush();
        // 중간 저장소를 비우기 전에 orderItemId, 주문_상품의 번호 기록
        em.clear();
        //주문_상품을 조회시, 실제 디비에서 찾기 할때
        // 연관 있는 것만 조회(lazy) or 연관 없는 것도 조회(eager)
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(EntityNotFoundException::new);
        System.out.println("Order class : " + orderItem.getOrder().getClass());
        System.out.println("===========================");
        orderItem.getOrder().getOrderDate();
        System.out.println("===========================");
    }

}