package com.meta.jpacore;

import com.meta.jpacore.entity.Memo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLOutput;

public class EntityTest {

    EntityManagerFactory emf;
    EntityManager em;

    @BeforeEach
    void setUp() {
        emf = Persistence.createEntityManagerFactory("memo");
        em = emf.createEntityManager();
    }

    @Test
    @DisplayName("EntityTransaction 성공테스트")
    void test1() {
        EntityTransaction et = em.getTransaction(); // EntityManager 에서 트랜잭션을 가져옴

        et.begin(); //트랜잭션 시작

        try {
            Memo memo = new Memo(); //저장할 메모 엔터티 객체 생성
            memo.setId(1L);
            memo.setUsername("Meta1");
            memo.setContents("영속성 컨텍스트 와 트랜잭션 이해하기 (디버거 보는중)");

            em.persist(memo); //EM이 memo 객체 를 영속성 컨텍스트에 저장/

            et.commit();//오류가 발생하지 않고 정상이라면, Commit(쿼리 수행)을 수행
        } catch (Exception e) {
            e.printStackTrace();
            et.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    @Test
    @DisplayName("EntityTransaction 실패 테스트")
    void test2() {
        EntityTransaction et = em.getTransaction(); // EntityManager 에서 트랜잭션을 가져옴

        et.begin(); //트랜잭션 시작

        try {
            Memo memo = new Memo(); //저장할 메모 엔터티 객체 생성
            //아이디 값을 안넣고
            memo.setUsername("Meta2");
            memo.setContents("영속성 컨텍스트 와 트랜잭션 이해하기");

            em.persist(memo); //EM이 memo 객체 를 영속성 컨텍스트에 저장/

            et.commit();//오류가 발생하지 않고 정상이라면, Commit(쿼리 수행)을 수행
        } catch (Exception e) {
            System.out.println("식별자 값을 넣어주지 않아 오류가 발생하였습니다.");
            e.printStackTrace();
            et.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    @Test
    @DisplayName("1차 캐시 : Entity 저장")
    void test3() {
        EntityTransaction et = em.getTransaction(); // EntityManager 에서 트랜잭션을 가져옴

        et.begin(); //트랜잭션 시작
    //Ctrl +alt+T-> try catch finally
        try {
            //저장할 메모 엔터티 객체 생성
            Memo memo = new Memo();
            memo.setId(10L);
            memo.setUsername("김메타10");
            memo.setContents("1차 캐시 Entity 저장");

            //EM이 memo 객체 를 영속성 컨텍스트에 관리
            em.persist(memo);

            //트랜잭션 커밋
            et.commit();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
            em.close();
        }
        emf.close();
    }

    @Test
    @DisplayName("1차 캐시 : 캐시 저장소에 해당하는 Id 가 존재하지 않는 경우")
    void test4() {
        try {
            //저장할 메모 엔터티 객체 생성
            Memo memo = em.find(Memo.class, 11L);
            System.out.println("memo.getId()=" + memo.getId());
            System.out.println("memo.getUsername()=" + memo.getUsername());
            System.out.println("memo.getContents()=" + memo.getContents());

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
        emf.close();
    }

    @Test
    @DisplayName("1차 캐시 : 캐시 저장소에 해당하는 Id 가 존재하는 경우")
    void test5() {
        try {
            //저장할 메모 엔터티 객체 생성
            Memo memo1 = em.find(Memo.class, 10L);
            Memo memo2 = em.find(Memo.class, 10L);
    //다른 메모리를 사용하는 변수를 지정하더라도 위 2개 엔티티는 같은 객체를 바라봄.
            System.out.println("memo1 == memo2 : " + (memo1 == memo2)); //true

            System.out.println("memo.getId()=" + memo1.getId());
            System.out.println("memo.getUsername()=" + memo1.getUsername());
            System.out.println("memo.getContents()=" + memo1.getContents());


        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
        emf.close();
    }

    @Test
    @DisplayName("객체 동일성 보장 확인")
    void test6() {
        EntityTransaction et = em.getTransaction(); // EntityManager 에서 트랜잭션을 가져옴

        et.begin(); //트랜잭션 시작
        //Ctrl +alt+T-> try catch finally
        try {
            //저장할 메모 엔터티 객체 생성
            Memo memo3 = new Memo();
            memo3.setId(23L);
            memo3.setUsername("김메타23");
            memo3.setContents("객체동일성 보장");

            //EM이 memo 객체 를 영속성 컨텍스트에 관리
            em.persist(memo3);
            Memo memo0 = em.find(Memo.class, 23L);

            Memo memo1 = em.find(Memo.class, 10L);

            System.out.println(memo3 == memo0); //true
            System.out.println(memo1 == memo0); //false

            //트랜잭션 커밋
            et.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
        emf.close();
    }

    @Test
    @DisplayName("1차 캐에서의 엔터티 삭제")
    void test7() {
        EntityTransaction et = em.getTransaction(); // EntityManager 에서 트랜잭션을 가져옴

        et.begin(); //트랜잭션 시작
        //Ctrl +alt+T-> try catch finally
        try {
            Memo memo = em.find(Memo.class, 23L);

            em.remove(memo);
//Debuger->em>persist context->EntityEntryContext->persist context->EntityEntryContext->nonEnhancedEntityXref에서 Managed 상태확인
            et.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
        emf.close();
    }

    @Test
    @DisplayName("쓰기지연저장소 Action Queue")
    void test8() {
        EntityTransaction et = em.getTransaction(); // EntityManager 에서 트랜잭션을 가져옴

        et.begin(); //트랜잭션 시작

        try {
            //저장할 메모 엔터티 객체 생성
            Memo memo1 = new Memo();
            memo1.setId(24L);
            memo1.setUsername("김메타24");
            memo1.setContents("쓰기 지연 저장소 확인");
            em.persist(memo1);

            Memo memo2 = new Memo();
            memo2.setId(25L);
            memo2.setUsername("김메타25");
            memo2.setContents("저장은 잘될까?");
            em.persist(memo2);

            System.out.println("---트랜잭션 commit 전---");
            //트랜잭션 커밋
            et.commit();
            System.out.println("---트랜잭션 commit 후---");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
        emf.close();
    }

    @Test
    @DisplayName("fluch()매서드 확인")
    void test9() {
        EntityTransaction et = em.getTransaction(); // EntityManager 에서 트랜잭션을 가져옴

        et.begin(); //트랜잭션 시작

        try {
            //저장할 메모 엔터티 객체 생성
            Memo memo1 = new Memo();
            memo1.setId(26L);
            memo1.setUsername("김메타26");
            memo1.setContents("flush 매서드 호출 확인");
            em.persist(memo1);

            System.out.println("---flush() 전---");
            em.flush();

            System.out.println("---flush() 후---");

            System.out.println("---트랜잭션 commit 전---");
            //트랜잭션 커밋
            et.commit();
            System.out.println("---트랜잭션 commit 후---");
        } catch (Exception e) {
            et.rollback();
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
        emf.close();
    }


    @Test
    @DisplayName("변경 감지 DIrty Checking")
    void test10() {
        EntityTransaction et = em.getTransaction(); // EntityManager 에서 트랜잭션을 가져옴

        et.begin(); //트랜잭션 시작

        try {
            //저장할 메모 엔터티 객체 생성
            Memo memo1 = em.find(Memo.class,26L);
            System.out.println("memo1.getId() = " + memo1.getId());
            System.out.println("memo1.getUsername() = " + memo1.getUsername());
            System.out.println("memo1.getContents() = " + memo1.getContents());

            System.out.println("\n수정을 진행합니다.");
            memo1.setUsername("김수정");
            memo1.setContents("변경 감지 확인");

            System.out.println("---트랜잭션 commit 전---");
            //트랜잭션 커밋
            et.commit();
            System.out.println("---트랜잭션 commit 후---");
        } catch (Exception e) {
            et.rollback();
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
        emf.close();
    }
/**
 * 안쓰긴 하는데 알아만 두셈
 *
 * 엔터티의 영속화 상태
 * 비영속 -key 가 null
 * 영속 = em.persist()를 통해 Xref->entry 에 "MANAGED"
 * 준영속 = em.detach()를 통해 "DETACHED"상태로 변함
 * ->준영속 em.merg()를 통해 영속상태로 복원
 * 삭제 = em.removed()를 통해 "REMOVED"
 *
 * 영속성 컨텍스트 내부를 전체 비움 =em.clear(); -> 내부에 있던 엔터티들이 모두 DETACGED가 됌
 * 영속성 컨텍스트 종료 - em.close()
* */

}

