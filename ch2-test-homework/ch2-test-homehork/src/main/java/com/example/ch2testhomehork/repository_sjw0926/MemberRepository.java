package com.example.ch2testhomehork.repository_sjw0926;


import com.example.ch2testhomehork.entity_sjw0926.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

// 레거시 ,
// 동네 1 ~ 4, 비유,
// DAO (Repository) -> JPA -> ORM 기술 사용중, -> Hibernate 구현체 이용해서.
// 인터페이스에 있는 기본 쿼리 메소드를 이용해서, save, count, delete 기본 기능.
// 단위 테스트에 사용할 테스트 디비 H2 디비 사용함.
// 임시로 사용하기 위해서, 메모리 위에 작업을 한다.
// 기본설정 파일과, 테스트를 위한 설정 파일을 2개 분리해서 작업 중.

public interface MemberRepository extends JpaRepository<Member, Long> {
    //public interface MemberRepository extends JpaRepository<Member, Long>, QuerydslPredicateExecutor<Member> {


    List<Member> findByUserNm(String userNm);

    List<Member> findByUserDescription(String userDescription);

   List<Member> findByUserNmOrUserDescription(String userNm, String userDescription);

    //Optional<Member> findById(Long id);



    @Query("select m from Member m where m.userDescription like " +
            "%:userDescription% order by m.regTime desc")
    List<Member> findByUserDescription2(@Param("userDescription") String userDescription);

    //Native Query 사용하면 DB컬럼명까지 맞춰줘야함
    @Query(value="select * from Member m where m.user_Description like " +
            "%:userDescription% order by  m.reg_Time desc", nativeQuery = true)
    List<Member> findByUserDescriptionByNative(@Param("userDescription") String userDescription);

}