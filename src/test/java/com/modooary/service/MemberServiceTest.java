package com.modooary.service;

import com.modooary.domain.Member;
import com.modooary.domain.PreMember;
import com.modooary.repository.MemberRepository;
import com.modooary.repository.PreMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired PreMemberRepository preMemberRepository;

    @Test
    public void 회원가입과_단일조회() {
        Member member = Member.createMember("cat", "cat@gmail", "catcat");

        Long savedId = memberService.join(member);

        Assert.assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test
    //@Rollback(value = false)
    public void 임시회원_등록() {
        PreMember preMember = PreMember.createPreMember("dog@gmail.com", "0000", "dog");
        Long savedId = memberService.joinPreMember(preMember);
        Assert.assertEquals(preMember, preMemberRepository.findOne(savedId));
    }

    @Test
    //@Rollback(value = false)
    public void 임시회원의_회원_전환() {
        //임시회원을 생성해서 등록하고 생성된 키를 꺼냄
        PreMember preMember = PreMember.createPreMember("bird@gmail.com", "1111", "bird");
        Long savedId = memberService.joinPreMember(preMember);
        String getKey = preMember.getKey();
        String preEmail = preMember.getEmail();

        //임시회원을 조회해서 키값을 비교 후 맞다면 전환
        Long memberId = 0L;
        if(memberService.checkPreMemberKey(savedId, getKey)){
            memberId = memberService.approveMember(savedId);
        }

        //전환된 멤버의 이메일과 기존 이메일을 비교
        Assert.assertEquals(memberRepository.findOne(memberId).getEmail(), preEmail);
    }
}
