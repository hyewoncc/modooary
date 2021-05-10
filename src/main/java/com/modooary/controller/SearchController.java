package com.modooary.controller;

import com.modooary.domain.Member;
import com.modooary.repository.MemberSearchRepository;
import com.modooary.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class SearchController {

    private final MemberService memberService;
    private final MemberSearchRepository memberSearchRepository;

    @GetMapping("/search")
    @ResponseBody
    public List<searchMemberDto> searchMember(HttpServletRequest request) {

        //비동기 통신으로 받은 데이터를 이용해 회원 검색
        Set<Member> members = new HashSet<>();
        String keyword = "%" + request.getParameter("keyword") + "%";
        members.addAll(memberSearchRepository.findByNameLike(keyword));
        members.addAll(memberSearchRepository.findByEmailLike(keyword));

        List<searchMemberDto> result = members.stream()
                .map(m -> new searchMemberDto(m))
                .collect(Collectors.toList());

        return result;
    }

    @Data
    @AllArgsConstructor
    static class searchMemberDto {
        private Long id;
        private String name;
        private String email;
        private String picture;

        public searchMemberDto(Member member) {
            id = member.getId();
            name = member.getName();
            email = member.getEmail();
            picture = member.getPicture();
        }
    }
}
