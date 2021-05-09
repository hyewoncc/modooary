package com.modooary;

import com.modooary.domain.Member;
import com.modooary.repository.MemberSearchRepository;
import com.modooary.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberSearchTest {

    @Autowired
    MemberSearchRepository memberSearchRepository;
    @Autowired
    MemberService memberService;

    @Test
    public void 이름_또는_이메일로_회원_찾기() {
        //네 회원 등록
        Member member1 = Member.createMember("cat1", "goyang@gmail.com", "1111");
        memberService.join(member1);
        Member member2 = Member.createMember("bird", "bird@gmail.com", "2222");
        memberService.join(member2);
        Member member3 = Member.createMember("dog", "dog@gmail.com", "3333");
        memberService.join(member3);
        Member member4 = Member.createMember("cat2", "cat@gmail.com", "4444");
        memberService.join(member4);

        //키워드로 검색 후 결과값의 건수를 비교
        List<Member> searchByCat = memberSearchRepository.findByNameLike("%cat%");
        //검색 결과는 두 건이 되어야 한다
        Assertions.assertThat(searchByCat.size()).isEqualTo(2);

        //키워드로 검색 후 결과값을 건수를 비교
        List<Member> searchByGmail = memberSearchRepository.findByEmailLike("%gmail%");
        //검색 결과는 네 건이 되어야 한다
        Assertions.assertThat(searchByGmail.size()).isEqualTo(4);

    }

}
