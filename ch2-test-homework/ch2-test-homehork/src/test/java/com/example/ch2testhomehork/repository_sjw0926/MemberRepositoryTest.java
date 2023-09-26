package com.example.ch2testhomehork.repository_sjw0926;

import com.example.ch2testhomehork.constant_sjw0926.UserRole;
import com.example.ch2testhomehork.entity_sjw0926.Member;
import com.example.ch2testhomehork.entity_sjw0926.QMember;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.domain.Sort.by;

@SpringBootTest
// 테스트를 하기위한 , 설정 파일을 분리했고, 로드.
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberRepositoryTest {

    //DI = 가져오기
    @Autowired
    MemberRepository memberRepository;

    // dsl사용시 필요
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
    @DisplayName("@Query를 이용한 유저 조회 테스트")
    public void findByUserDescription2(){
        this.createMemberList();
        List<Member> memberList =
                memberRepository.findByUserDescription2("실습 풀이7");
        for(Member member : memberList){
            System.out.println(member.toString());
        }
    }

    @Test
    @DisplayName("@Query를 이용한 유저 조회 테스트")
    public void findByUserDescriptionByNative(){
        this.createMemberList();
        List<Member> memberList =
                memberRepository.findByUserDescriptionByNative("실습 풀이7");
        for(Member member : memberList){
            System.out.println(member.toString());
        }
    }


    //Querydsl 테스트 추가 부분
    //사용할때 pom.xml에 추가해야 할 의존성 2개, 플러그인 1개
    @Test
    @DisplayName("Querydsl 조회 테스트1")
    public void queryDslTest(){
        this.createMemberList2();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QMember qMember = QMember.member;
        JPAQuery<Member> query  = queryFactory.selectFrom(qMember)
                .where(qMember.userRole.eq(UserRole.ADMIN))
                .where(qMember.userDescription.like("%" + "실습 풀이3"+ "%"))
                .orderBy(qMember.regTime.desc());

        List<Member> memberList = query.fetch();

        for(Member member : memberList){
            System.out.println(member.toString());
        }
    }

    public void createMemberList2(){
        for(int i=1;i<=5;i++){
            Member member = new Member();
            member.setUserNm("테스트 회원" + i);
            member.setUserDescription("실습 풀이" + i);
            member.setUserEmail("test"+i+"@naver.com");
            member.setUserRole(UserRole.ADMIN);
            member.setRegTime(LocalDateTime.now());
            Member savedMember = memberRepository.save(member);
        }

        for(int i=6;i<=10;i++){
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
    @DisplayName("유저 Querydsl 조회 테스트 2")
    public void queryDslTest2(){

        this.createMemberList2();

        //실습 위한 더미 data
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QMember qMember = QMember.member;
        String userDescription = "실습 풀이";
        String memberUserRole = "ADMIN";

        //querydsl 사용할 때 조건 담는 클래스
        booleanBuilder.and(qMember.userDescription.like("%" + userDescription + "%"));
        //booleanBuilder.and(qMember.id.gt(5)); //이거 조회안됨 수정 요망
        System.out.println(UserRole.ADMIN);

        //StringUtils : 첫번째, 두번째 문자열 비교(thymleaf밑에 있음)
        if(StringUtils.equals(memberUserRole, UserRole.ADMIN)){
            booleanBuilder.and(qMember.userRole.eq(UserRole.ADMIN));
        }

        //정렬 순서 옵션 추가 부분1
        //예제 : MemberRepository.findAll(query, 0, Integer.MAX_VALUE.field1.asc(),_personInventory.field2.asc())

        //정렬 순서 옵션 추가 부분2
        //예제 : Pageable pageable = PageRequest.of(0,10, Sort.by("gno").descending());
        //      Page<Guestbook> result = guestbookRepository.findAll(builder,pageable); //
        //코드 : Pageable pageable = PageRequest.of(0, 5, Sort.by("id").descending());

        //원본
        //Pageable pageable = PageRequest.of(0, 5);
        Pageable pageable = PageRequest.of(0, 5, Sort.by("id").descending());
        Page<Member> itemPagingResult = memberRepository.findAll(booleanBuilder, pageable);
        System.out.println("total elements : " + itemPagingResult. getTotalElements ());

        List<Member> resultMemberList = itemPagingResult.getContent();
        for(Member resultMember: resultMemberList){
            System.out.println(resultMember.toString());
        }
    }
}