package com.modooary.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Invitation {

    @Id
    @GeneratedValue
    @Column(name = "INVITATION_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SENDER_ID")
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECEIVER_ID")
    private Member receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DIARY_ID")
    private Diary diary;

    protected Invitation() {}

    private void setSender(Member member) {
        this.sender = member;
    }

    private void setReceiver(Member member) {
        this.receiver = member;
    }

    private void setDiary(Diary diary) {
        this.diary = diary;
    }

    /* 생성 메서드 */
    //새로운 초대장 생성
    public static Invitation createInvitation(Member sender, Member receiver, Diary diary) {
        Invitation invitation = new Invitation();
        invitation.setSender(sender);
        invitation.setReceiver(receiver);
        invitation.setDiary(diary);
        return invitation;
    }
}
