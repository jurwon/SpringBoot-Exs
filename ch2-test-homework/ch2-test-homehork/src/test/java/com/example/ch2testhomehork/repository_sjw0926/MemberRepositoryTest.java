package com.example.ch2testhomehork.repository_sjw0926;

import com.example.ch2testhomehork.constant_sjw0926.UserRole;
import com.example.ch2testhomehork.entity_sjw0926.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
// 테스트를 하기위한 , 설정 파일을 분리했고, 로드.
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberRepositoryTest {

    //DI = 가져오기
    @Autowired
    MemberRepository memberRepository;

    // 영속성 컨텍스트 기능 이용하기위한, 엔티티 매니저 인스턴스
    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("회원 저장 테스트")
    public void createMemberTest() {
        Member member = new Member();
        member.setUserNm("도도새");
        member.setUserDescription("실습 풀이");
        member.setUserEmail("test@naver.com");
        member.setUserRole(UserRole.USER);
        member.setRegTime(LocalDateTime.now());
        //itemRepository 이용해서, 자바 -> 디비, 샘플디비 하나 생성
        // 영속성 컨텍스트 = 중간 저장소, 1차 캐시 테이블
        Member savedMember = memberRepository.save(member);
        // H2, 실제 파일기반으로 내용을 저장이 아니라.
        // 메모리에 임시로 작업을 해서, 저장이 안됨.
        System.out.print(savedMember.toString());
    }


    public void createMemberList(){
        for(int i=1;i<=10;i++){
            Member member = new Member();
            member.setUserNm("테스트 회원" + i);
            member.setUserDescription("실습 풀이" + i);
            member.setUserEmail("test"+i+"@naver.com");
            member.setUserRole(UserRole.USER);
            member.setRegTime(LocalDateTime.now());
            Member savedMember = memberRepository.save(member);
        }
    }

    @Test
    public void findByUserNm() {
        this.createMemberList();
        List<Member> memberList = memberRepository.findByUserNm("테스트 회원");
        for(Member member : memberList){
            System.out.println(member.toString());
        }
    }

    @Test
    public void findByUserDescription() {
        this.createMemberList();
        List<Member> memberList = memberRepository.findByUserDescription("실습 풀이");
        for(Member member : memberList){
            System.out.println(member.toString());
        }
    }

    @Test
    @DisplayName("상품명, 상품상세설명 or 테스트")
    // H2 데이터베이스를 이용해서, 메모리 상에서, 단위 테스트 기능을 확인중.
    // 메모리 상에 작업중이니, 당연히 MySQL 디비와는 관계없음.
    public void findByUserNmOrUserDescription(){
        // 매번 테스트 할 때 마다, 더미 데이터 10 개 생성
        this.createMemberList();
        // 확인.  조회 조건 확인 중. or
        List<Member> memberList = memberRepository.findByUserNmOrUserDescription("테스트 회원1", "실습 풀이5");
        for(Member member : memberList){
            System.out.println(member.toString());
        }
    }

    @Test
    @DisplayName("@Query를 이용한 상품 조회 테스트")
    public void findByUserDescription2(){
        this.createMemberList();
        List<Member> memberList =
                memberRepository.findByUserDescription2("실습 풀이7");
        for(Member member : memberList){
            System.out.println(member.toString());
        }
    }
    /*

    *//*Querydsl 테스트 추가 부분*//*
    @Test
    @DisplayName("Querydsl 조회 테스트1")
    public void queryDslTest(){
        this.createItemList();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QItem qItem = QItem.item;
        JPAQuery<Item> query  = queryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%" + "테스트 상품 상세 설명" + "%"))
                .orderBy(qItem.price.desc());

        List<Item> itemList = query.fetch();

        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    public void createItemList3(){
        for(int i=1;i<=5;i++){
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }

        for(int i=6;i<=10;i++){
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
            item.setStockNumber(0);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }
    }*/
}