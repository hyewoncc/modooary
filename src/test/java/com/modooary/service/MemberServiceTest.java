package com.modooary.service;

import com.modooary.domain.Member;
import com.modooary.repository.MemberRepository;
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

    @Test
    @Rollback(false)
    public void 회원가입과_단일조회() {
        Member member = Member.createMember("cat", "cat@gmail", "catcat");

        Long savedId = memberService.join(member);

        Assert.assertEquals(member, memberRepository.findOne(savedId));
    }
}
