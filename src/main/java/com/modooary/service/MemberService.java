package com.modooary.service;

import com.modooary.domain.Member;
import com.modooary.domain.PreMember;
import com.modooary.repository.MemberRepository;
import com.modooary.repository.PreMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PreMemberRepository preMemberRepository;


    //임시 회원 가입
    @Transactional
    public Long joinPreMember(PreMember preMember) {
        preMemberRepository.save(preMember);
        return preMember.getId();
    }

    //회원 가입
    @Transactional
    public Long join(Member member){
        memberRepository.save(member);
        return member.getId();
    }

    //단일 회원 조회
    public Member findOneMember(Long memberId){
        return memberRepository.findOne(memberId);
    }

    //전체 회원 조회
    public List<Member> findAllMembers(){
        return memberRepository.findAll();
    }

    //임시 회원 조회
    public PreMember findPreMember(Long preMemberId) {
        return preMemberRepository.findOne(preMemberId);
    }

    //임시 회원을 정식 회원으로 전환
    @Transactional
    public Long approveMember(Long preMemberId) {
        //id 값으로 임시 회원을 찾아 정식 회원으로 값 세팅
        PreMember preMember = preMemberRepository.findOne(preMemberId);
        Member member = Member.createMember(
                preMember.getName(), preMember.getEmail(), preMember.getPassword());

        //회원을 등록하고 임시 회원을 삭제
        memberRepository.save(member);
        preMemberRepository.delete(preMember);

        return member.getId();
    }

    //임시 회원의 인증키를 값을 비교해서 True / False 반환
    public boolean checkPreMemberKey(Long preMemberId, String key) {
        if(preMemberRepository.findOne(preMemberId).getKey().equals(key)){
            return true;
        }
        else {
            return false;
        }
    }

    //로그인 처리를 위해 이메일로 검색 후 비밀번호 값 비교
    public boolean memberLogin(String email, String password) {
        Member member = memberRepository.findOneByEmail(email);
        if(member.getPassword().equals(password)){
            return true;
        }else {
            return false;
        }
    }

    //이메일 검색 후 멤버를 반환
    public Member findOneByEmail(String email){
        return memberRepository.findOneByEmail(email);
    }
}
