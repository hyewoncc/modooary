package com.modooary.domain;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Random;

@Entity
@Getter
public class PreMember {

    @Id
    @GeneratedValue
    @Column(name = "PREMEMBER_ID")
    private Long id;

    private String email;
    private String password;
    private String name;
    private String key;

    /* set 메서드 */
    private void setEmail(String email) {
        this.email = email;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    private void setName(String name) {
        this.name = name;
    }

    //6자리의 난수를 랜덤키로 지정
    private void setRandomKey() {
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();

        while (stringBuffer.length() < 6) {
            stringBuffer.append(random.nextInt(10));
        }

        this.key = stringBuffer.toString();
    }

    /* 생성 메서드 */
    protected PreMember(){}

    public static PreMember createPreMember(String email, String password, String name){
        PreMember preMember = new PreMember();
        preMember.setEmail(email);
        preMember.setPassword(password);
        preMember.setName(name);
        preMember.setRandomKey();
        return preMember;
    }

}
