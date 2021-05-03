package com.modooary.service;

import com.modooary.domain.Diary;
import com.modooary.domain.DiaryPost;
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
public class DiaryBoardServiceTest {

    @Autowired
    DiaryBoardService diaryBoardService;
    @Autowired
    MemberService memberService;
    @Autowired
    DiarySetService diarySetService;

    @Test
    public void 다이어리_포스트_등록(){

        //게정 생성과 저장
        Member member = Member.createMember("cat", "cat@gmail.com", "1111");
        Long memberId = memberService.join(member);
        //다이어리 생성과 저장
        Diary diary = Diary.createDiary("일기장A");
        Long diaryId = diarySetService.registerDiary(diary, member);

        //포스트 생성
        DiaryPost diaryPost = DiaryPost.createPost(diary, member, "첫 게시글");

        //포스트 저장
        Long postId = diaryBoardService.registerDiaryPost(diaryPost);

        //포스트 조회 후 일치하는지 확인
        Assert.assertEquals("생성된 포스트와 조회한 포스트가 동일한지 테스트", diaryPost, diaryBoardService.findOne(postId));
    }

    @Test
    //@Rollback(value = false)
    public void 여러_포스트_등록_및_조회(){
        //게정 생성과 저장
        Member member = Member.createMember("cat", "cat@gmail.com", "1111");
        Long memberId = memberService.join(member);
        //다이어리 생성과 저장
        Diary diary = Diary.createDiary("일기장A");
        Long diaryId = diarySetService.registerDiary(diary, member);

        List<DiaryPost> posts = new ArrayList<>();
        List<Long> postIds = new ArrayList<>();

        //다중 포스트 생성과 저장
        for(int i = 0; i < 5; i++) {
            DiaryPost diaryPost = DiaryPost.createPost(diary, member, i + "번째 게시글");
            posts.add(diaryPost);
            diaryBoardService.registerDiaryPost(diaryPost);
        }

        //배열로 조회한 포스트 목록이 일치하는지 확인
        Assert.assertEquals("저장한 포스트 목록과 조회한 포스트 목록을 비교", posts, diaryBoardService.listDiaryPosts(diary));

    }
}
