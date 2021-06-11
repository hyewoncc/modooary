package com.modooary.repository;

import com.modooary.domain.Diary;
import com.modooary.domain.DiaryPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepositoryJpa extends JpaRepository<DiaryPost, Long> {

    Page<DiaryPost> findByDiary(Diary diary, Pageable pageable);
}
