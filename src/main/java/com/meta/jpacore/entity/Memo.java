package com.meta.jpacore.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="memo")
@Getter
@Setter
public class Memo {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY) //잠시해제
    private  Long id;

    //nullable =null 허용 여부
    //unique = 중복 허용 여부(고유)
    @Column(name="username", nullable = false, unique=true)
    private  String username;

    @Column(name = "contents", nullable = false,length = 500)
    private  String contents;

}
