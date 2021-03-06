package com.modooary.service;

import com.modooary.domain.Diary;
import com.modooary.domain.DiaryMember;
import com.modooary.domain.Member;
import com.modooary.repository.DiaryMemberRepository;
import com.modooary.repository.DiaryRepository;
import com.modooary.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DiarySetService {

    private final MemberRepository memberRepository;
    private final DiaryRepository diaryRepository;
    private final DiaryMemberRepository diaryMemberRepository;

    //다이어리 생성
    @Transactional
    public Long registerDiary(Diary diary, Member member){
        //다이어리 정보를 저장
        diaryRepository.save(diary);

        //회원을 호스트로 소속 정보 등록
        DiaryMember diaryMember = DiaryMember.createHost(member, diary);
        diaryMemberRepository.save(diaryMember);

        return diary.getId();
    }

    //게스트 회원 소속 정보 등록
    @Transactional
    public Long registerDiaryMember(Diary diary, Member member){
        //회원을 게스트로 소속 정보 등록
        DiaryMember diaryMember = DiaryMember.createGuest(member, diary);
        diaryMemberRepository.save(diaryMember);

        return diaryMember.getId();
    }

    //다이어리 제목과 색상 정보 변경
    @Transactional
    public void changeDiaryInfo(Diary diary, String title, String color) {
        diary.changeTitle(title);
        diary.changeColor(color);
    }

    //단일 다이어리 조회
    public Diary findDairy(Long dairyId){
        return diaryRepository.findOne(dairyId);
    }

    //단일 다이어리의 모든 다이어리-회원 소속 정보 조회
    public List<DiaryMember> findDairyMembers(Diary diary){

        List<DiaryMember> diaryMembers = new ArrayList<>();

        //해당 다이어리-회원 소속 정보 조회
        diaryMembers = diaryMemberRepository.findDairyMembers(diary.getId());

        return diaryMembers;
    }

    //단일 다이어리의 모든 회원 조회
    public List<Member> listDairyMembers(Diary diary){

        List<Member> members = new ArrayList<>();

        //해당 다이어리의 회원 소속 정보를 조회
        List<DiaryMember> diaryMembers = diaryMemberRepository.findDairyMembers(diary.getId());

        //소속 정보를 이용해 모든 멤버 정보를 조회
        for(DiaryMember dm : diaryMembers){
            members.add(memberRepository.findOne(dm.getMember().getId()));
        }

        return members;
    }

    //단일 회원의 모든 다이어리 조회
    public List<Diary> listMemberDiaries(Member member){

        List<Diary> diaries = new ArrayList<>();

        //해당 멤버의 다이어리 소속 정보를 조회
        List<DiaryMember> diaryMembers = diaryMemberRepository.findMemberDairies(member.getId());

        //소속 정보를 이용해 모든 다이어리 정보를 조회
        for (DiaryMember dm : diaryMembers) {
            diaries.add(diaryRepository.findOne(dm.getDiary().getId()));
        }

        return diaries;
    }

    //단일 회원이 특정 다이어리에 속해있는지 확인
    public boolean checkMemberInDiary(Member member, Diary diary){
        List<DiaryMember> diaryMembers = diaryMemberRepository.findDairyMembers(diary.getId());
        for (DiaryMember diaryMember : diaryMembers) {
            if (diaryMember.getMember().equals(member)) {
                return true;
            }
        }
        return false;
    }
}
