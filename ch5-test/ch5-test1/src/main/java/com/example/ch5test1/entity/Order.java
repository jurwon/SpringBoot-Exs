package com.example.ch5test1.entity;

import com.example.ch5test1.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    /*@ManyToOne()*/
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate; //주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //주문상태

    /* eager :  연관 없는 테이블도 전부 다 조회 해서 성능상 이슈 발생
       lazy : 해당 테이블만 조회 */
     @OneToMany(mappedBy = "order", cascade = CascadeType.ALL
            , orphanRemoval = true, fetch = FetchType.LAZY)

    /*orphanRemoval = true : 연쇄 cascade설정 여부*/
    /*@OneToMany(mappedBy = "order", cascade = CascadeType.ALL
            , orphanRemoval = true, fetch = FetchType.EAGER)*/
   /* @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)*/
    private List<OrderItem> orderItems = new ArrayList<>();

}