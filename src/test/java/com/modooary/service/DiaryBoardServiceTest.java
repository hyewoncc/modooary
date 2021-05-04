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

    /**
     * 각기 다른 다이어리에 다른 계정으로 프스트를 등록하고,
     * 그 포스트의 다이어리 id 값과 계정 id 값이
     * 등록된 다이어리, 등록한 계정과 일치하는지 검증
     */
    @Test
    //@Rollback(value = false)
    public void 각기_다른_다이어리에_프스트_등록(){
        ///게정 생성과 저장
        Member member1 = Member.createMember("cat", "cat@gmail.com", "1111");
        Long memberId1 = memberService.join(member1);
        Member member2 = Member.createMember("dog", "dog@gmail.com", "2222");
        Long memberId2 = memberService.join(member2);

        //다이어리 생성과 저장
        Diary diary1 = Diary.createDiary("고양이모임");
        Long diaryId1 = diarySetService.registerDiary(diary1, member1);
        Diary diary2 = Diary.createDiary("강아지모임");
        Long diaryId2 = diarySetService.registerDiary(diary2, member2);

        //다이어리1에 포스트 생성과 저장
        DiaryPost diaryPost1 = DiaryPost.createPost(diary1, member1, "야옹");
        Long postId1 = diaryBoardService.registerDiaryPost(diaryPost1);

        //다이어리2에 포스트 생성과 저장
        DiaryPost diaryPost2 = DiaryPost.createPost(diary2, member2, "멍멍");
        Long postId2 = diaryBoardService.registerDiaryPost(diaryPost2);

        //각각 포스트가 맞는 다이어리 id를 갖고 있는지 확인
        Assert.assertEquals("포스트1의 다이어리 id는 다이어리1과 같다",
                diaryId1, diaryBoardService.findOne(postId1).getDiary().getId());
        Assert.assertEquals("포스트2의 다이어리id는 다이어리2와 같다",
                diaryId2, diaryBoardService.findOne(postId2).getDiary().getId());

        //각각 포스트가 맞는 계정 id를 갖고 있는지 확인
        Assert.assertEquals("포스트1의 멤버 id는 멤버1과 같다",
                memberId1, diaryBoardService.findOne(postId1).getMember().getId());
        Assert.assertEquals("포스트2의 멤버 id는 멤버2와 같다",
                memberId2, diaryBoardService.findOne(postId2).getMember().getId());
    }
}
