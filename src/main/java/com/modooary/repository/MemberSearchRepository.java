package com.modooary.repository;

import com.modooary.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberSearchRepository extends JpaRepository <Member, Long> {
    List<Member> findByNameLike(String keyword);

    List<Member> findByEmailLike(String keyword);
}
