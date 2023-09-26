package com.example.ch2testhomehork.entity_sjw0926;

import com.example.ch2testhomehork.constant_sjw0926.UserRole;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name="member")
@Getter
@Setter
@ToString
public class Member {

    @Id
    @Column(name="user_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;       //멤버코드


    @Column(nullable = false, length = 50)
    private String userNm; //유저명

    @Column(name="userEmail", nullable = false, unique = true)
    private String userEmail; //이메일

    @Lob
    @Column(nullable = false)
    private String userDescription; //유저 소개

    @Enumerated(EnumType.STRING)
    private UserRole userRole; //유저 권한

    private LocalDateTime regTime; //등록 시간


}
