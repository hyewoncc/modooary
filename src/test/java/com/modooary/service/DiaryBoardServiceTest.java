package com.modooary.service;

import com.modooary.domain.Diary;
import com.modooary.domain.DiaryPost;
import com.modooary.domain.Member;
import com.modooary.domain.PostReply;
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
        Assert.assertEquals("생성된 포스트와 조회한 포스트가 동일한지 테스트", diaryPost, diaryBoardService.findOnePost(postId));
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
                diaryId1, diaryBoardService.findOnePost(postId1).getDiary().getId());
        Assert.assertEquals("포스트2의 다이어리id는 다이어리2와 같다",
                diaryId2, diaryBoardService.findOnePost(postId2).getDiary().getId());

        //각각 포스트가 맞는 계정 id를 갖고 있는지 확인
        Assert.assertEquals("포스트1의 멤버 id는 멤버1과 같다",
                memberId1, diaryBoardService.findOnePost(postId1).getMember().getId());
        Assert.assertEquals("포스트2의 멤버 id는 멤버2와 같다",
                memberId2, diaryBoardService.findOnePost(postId2).getMember().getId());
    }

    /**
     * 단일 다이어리에 같은 계정으로 다른 포스트 등록
     * 그 후 각각 포스트에 댓글 작성
     * 각각의 포스트 id가 댓글의 포스트 id와 동일한지 검증
     * */
    @Test
    @Rollback(value = false)
    public void 각기_다른_포스트에_댓글_등록() {
        //게정 생성과 저장
        Member member = Member.createMember("cat", "cat@gmail.com", "1111");
        Long memberId = memberService.join(member);
        //다이어리 생성과 저장
        Diary diary = Diary.createDiary("일기장A");
        Long diaryId = diarySetService.registerDiary(diary, member);

        //각기 다른 포스트 생성과 저장
        DiaryPost diaryPost1 = DiaryPost.createPost(diary, member, "안녕하세요");
        Long postId1 = diaryBoardService.registerDiaryPost(diaryPost1);
        DiaryPost diaryPost2 = DiaryPost.createPost(diary, member, "저녁 메뉴는 무엇입니까");
        Long postId2 = diaryBoardService.registerDiaryPost(diaryPost2);

        //각 포스트에 댓글 생성 후 저장
        PostReply postReply1 = PostReply.createPostReply(diaryPost1, member, "반갑습니다");
        Long replyId1 = diaryBoardService.registerPostReply(postReply1);
        PostReply postReply2 = PostReply.createPostReply(diaryPost2, member, "고등어 구이 입니다");
        Long replyId2 = diaryBoardService.registerPostReply(postReply2);

        //저장된 댓글의 포스트 id 조회값이 기존값과 동일한지 검증
        Assert.assertEquals("포스트1의 id 값이 댓글1의 포스트 id값과 같은지 검증",
                postId1, diaryBoardService.findOneReply(replyId1).getDiaryPost().getId());
        Assert.assertEquals("포스트2의 id 값이 댓글2의 포스트 id값과 같은지 검증",
                postId2, diaryBoardService.findOneReply(replyId2).getDiaryPost().getId());
    }
}
