package com.modooary.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    private String name;
    private String email;
    private String password;
    private String picture;


    private void setMemberInfo(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static Member createMember(String name, String email, String password){
        Member member = new Member();
        member.setMemberInfo(name, email, password);
        return member;
    }

}
