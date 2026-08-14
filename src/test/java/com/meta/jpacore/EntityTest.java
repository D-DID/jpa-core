package com.meta.jpacore;

import com.meta.jpacore.entity.Memo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class EntityTest {

    EntityManagerFactory emf;
    EntityManager em;

    @BeforeEach
    void setUp(){
        emf=Persistence.createEntityManagerFactory("memo");
        em=emf.createEntityManager();
    }

    @Test
    @DisplayName("EntityTransaction 성공테스트")
    void test1(){
        EntityTransaction et = em.getTransaction(); // EntityManager 에서 트랜잭션을 가져옴

        et.begin(); //트랜잭션 시작

        try {
            Memo memo = new Memo(); //저장할 메모 엔터티 객체 생성
            memo.setId(1L);
            memo.setUsername("Meta1");
            memo.setContents("영속성 컨텍스트 와 트랜잭션 이해하기 (디버거 보는중)");

            em.persist(memo); //EM이 memo 객체 를 영속성 컨텍스트에 저장/

            et.commit();//오류가 바생하지 않고 정상이라면ㄷ, Commit(쿼리 수랭)을 수행
        } catch(Exception e) {
            e.printStackTrace();
            et.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    @Test
    @DisplayName("EntityTransaction 실패 테스트")
    void test2(){
        EntityTransaction et = em.getTransaction(); // EntityManager 에서 트랜잭션을 가져옴

        et.begin(); //트랜잭션 시작

        try {
            Memo memo = new Memo(); //저장할 메모 엔터티 객체 생성
            //아이디 값을 안넣고
            memo.setUsername("Meta2");
            memo.setContents("영속성 컨텍스트 와 트랜잭션 이해하기");

            em.persist(memo); //EM이 memo 객체 를 영속성 컨텍스트에 저장/

            et.commit();//오류가 바생하지 않고 정상이라면, Commit(쿼리 수랭)을 수행
        } catch(Exception e) {
            System.out.println("식별자 값을 넣어주지 않아 오류가 발생하였습니다.");
            e.printStackTrace();
            et.rollback();
        } finally {
            em.close();
        }
        emf.close();

    }
}
