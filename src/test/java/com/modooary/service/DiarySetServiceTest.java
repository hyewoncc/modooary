package com.modooary.service;

import com.modooary.domain.Diary;
import com.modooary.domain.DiaryMember;
import com.modooary.domain.Grade;
import com.modooary.domain.Member;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class DiarySetServiceTest {

    @Autowired
    DiarySetService diarySetService;
    @Autowired
    MemberService memberService;

    @Test
    //@Rollback(value = false)
    public void 다이어리_생성과_호스트_지정(){

        //계정 생성
        Member member = Member.createMember("cat", "cat@gmail.com", "0000");

        //다이어리 생성
        Diary diary = Diary.createDiary("모두의일기장");

        //다이어리 저장 + 호스트 지정
        Long savedId = diarySetService.registerDiary(diary, member);

        //다이어리 소속 정보를 순회하면서 생성 계정의 grade 정보를 찾아 확인
        List<DiaryMember> diaryMembers = diarySetService.findDairyMembers(diary);
        //grade를 GUEST로 생성, 생성 계정은 HOST로 등록되어 있어야 함
        Grade grade = Grade.GUEST;
        for(DiaryMember dm : diaryMembers){
            if (dm.getMember().equals(member)) {
                grade = dm.getGrade();
            }
        }

        Assert.assertEquals("저장 후 반환된 아이디값으로 엔티티를 조회 ",diary, diarySetService.findDairy(savedId));
        Assert.assertEquals("해당 다이어리-회원 소속 정보에서 생성자가 호스트 타입인지 확인 ",
                Grade.HOST, grade);
    }

    @Test
    //@Rollback(value = false)
    public void 다이어리에_게스트회원_등록(){

        List<Member> members = new ArrayList<>();

        //5개의 복수 계정 생성, 저장, 배열에 추가
        for(int i = 0; i < 5; i++){
            Member member = Member.createMember(i + "번 회원", i + "@gmail.com", "0000");
            memberService.join(member);
            members.add(member);
        }

        //다이어리 생성
        Diary diary = Diary.createDiary("모두의일기장");

        //복수 계정을 다이어리에 게스트로 등록
        for (Member m : members) {
            diarySetService.registerDiaryMember(diary, m);
        }

        //다이어리-회원 등록 정보를 조회
        List<DiaryMember> diaryMembers = diarySetService.findDairyMembers(diary);

        //현재 다이어리-회원 등록 정보가 모두 GUEST인지 확인
        for (DiaryMember dm : diaryMembers){
            Assert.assertEquals("다이어리에 등록된 회원은 모두 GUEST다", Grade.GUEST, dm.getGrade());
        }
    }
}
