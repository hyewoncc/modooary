package com.modooary.service;

import com.modooary.domain.Diary;
import com.modooary.domain.DiaryPost;
import com.modooary.domain.DiaryReply;
import com.modooary.domain.Member;
import com.modooary.repository.DiaryPostRepository;
import com.modooary.repository.DiaryReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DiaryBoardService {

    private final DiaryPostRepository diaryPostRepository;
    private final DiaryReplyRepository diaryReplyRepository;

    /* 포스트 CRUD */
    //포스트 생성과 저장
    @Transactional
    public Long registerDiaryPost(DiaryPost diaryPost) {
        //포스트를 저장
        diaryPostRepository.save(diaryPost);

        return diaryPost.getId();
    }

    //포스트 삭제
    @Transactional
    public void deleteDiaryPost(DiaryPost diaryPost) {
        diaryPostRepository.delete(diaryPost);
    }

    //단일 포스트 조회
    public DiaryPost findOne(Long id) {
        return diaryPostRepository.findOne(id);
    }

    //해당 다이어리의 모든 포스트 조회
    public List<DiaryPost> listDiaryPosts(Diary diary) {
        List<DiaryPost> diaryPosts = new ArrayList<>();

        //해당 다이어리의 모든 포스트를 조회
        diaryPosts = diaryPostRepository.findDiaryPosts(diary.getId());
        return diaryPosts;
    }


    /* 댓글 CRUD */
    //댓글 생성과 저장
    @Transactional
    public Long registerDiaryReply(DiaryReply diaryReply) {
        //댓글을 저장
        diaryReplyRepository.save(diaryReply);

        return diaryReply.getId();
    }

    //단일 댓글 삭제
    @Transactional
    public void deleteDiaryReply(DiaryReply diaryReply) {
        diaryReplyRepository.delete(diaryReply);
    }

    //특정 포스트의 모든 댓글 조회
    public List<DiaryReply> listPostReplies (DiaryPost diaryPost) {
        List<DiaryReply> diaryReplies = new ArrayList<>();

        // 해당 포스트의 모든 댓글 조회
        diaryReplies = diaryReplyRepository.findPostReplies(diaryPost.getId());
        return diaryReplies;
    }


}
