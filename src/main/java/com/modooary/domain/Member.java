package com.modooary.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
@Getter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<DiaryMember> diaryMembers = new ArrayList<>();

    private String name;
    private String email;
    private String password;
    private String picture;


    private void setMemberInfo(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

    /* 생성 메소드 */
    public static Member createMember(String name, String email, String password){
        Member member = new Member();
        member.setMemberInfo(name, email, password);
        return member;
    }

    protected Member(){}

    /* 연관관계 메소드 */
    public void addDiaryInfo(DiaryMember diaryMember) {
        diaryMembers.add(diaryMember);
    }


    public void changePicture(String picture) {
        this.picture = picture;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void setRandomPicture() {
        String[] randomPictures = {
                "bear.png", "blackcat.png", "graycat.png", "parrot.png", "rabbit.png", "yellowcat.png"};
        this.picture = randomPictures[new Random().nextInt(6)];
    }

}
