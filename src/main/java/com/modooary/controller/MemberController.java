package com.modooary.controller;

import com.modooary.controller.form.MemberForm;
import com.modooary.domain.*;
import com.modooary.service.DiaryBoardService;
import com.modooary.service.DiarySetService;
import com.modooary.service.MemberService;
import com.modooary.utils.EmailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final EmailUtil emailUtil;
    private final DiarySetService diarySetService;
    private final DiaryBoardService diaryBoardService;

    @GetMapping("/sign-in")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "signIn";
    }

    @PostMapping("/sign-in")
    public String sendSignInMail(@Valid MemberForm memberForm, BindingResult result, HttpSession session) {

        //입력 정보를 받아 임시 회원으로 저장
        PreMember preMember = PreMember.createPreMember(
                memberForm.getEmail(), memberForm.getPassword(), memberForm.getName());
        memberService.joinPreMember(preMember);

        //임시 회원 정보를 바탕으로 확인 이메일 발송 후 메인으로 리다이렉트
        try {
            emailUtil.sendJoinMail(
                    preMember.getName(), preMember.getEmail(), preMember.getId(), preMember.getKey());
            session.setAttribute("emailAlert", 1);
        } catch (MessagingException | UnsupportedEncodingException e) {
            session.setAttribute("emailAlert", -1);
            e.printStackTrace();
        }

        return "redirect:/";
    }

    @PostMapping("/sign-in/check-email")
    @ResponseBody
    public boolean checkEmail(HttpServletRequest request) {
        //이미 가입된 이메일인지 확인하고 사용 가능할 시 true, 불가일 시 false 반환
        String email = request.getParameter("email");
        return memberService.checkEmailUsable(email);
    }

    //인증 메일 링크로 들어올 시 파라미터 값을 DB와 비교하여 일치 시 정회원으로 등록
    @GetMapping("/sign-up/confirm")
    public String signUpConfirm(
            @RequestParam("id") Long id, @RequestParam("key") String key) {

        //임시 회원의 키값 조회 후 일치시 정회원 전환
        if (memberService.checkPreMemberKey(id, key)) {
            Long memberId = memberService.approveMember(id);
            Member member = memberService.findOneMember(memberId);
            createWelcomeMessage(member);
        }

        return "redirect:/";
    }

    //임시 비밀번호 설정
    @PostMapping("/reset-password")
    public String resetPassword(HttpServletRequest request){
        //이메일로 멤버 조회
        String email = request.getParameter("reset-password-email");
        Member member = memberService.findOneByEmail(email);

        //비밀번호를 랜덤 숫자 6자리로 변경
        memberService.resetPassword(member);

        HttpSession session = request.getSession();

        //임시 비밀번호를 포함한 메일 보내기
        try {
            emailUtil.sendResetPasswordMail(member);
            session.setAttribute("passwordAlert", "success");
        } catch (MessagingException | UnsupportedEncodingException e) {
            session.setAttribute("passwordAlert", "fail");
            e.printStackTrace();
        }

        return "redirect:/";
    }

    //등록된 회원인지 이메일로 획인
    @PostMapping("/reset-password/check-email")
    @ResponseBody
    public boolean checkEmailMember(HttpServletRequest request) {
        //등록된 회원이면 true를, 아니면 false를 반환
        String email = request.getParameter("email");
        return !(memberService.checkEmailUsable(email));
    }

    //개인 정보 수정
    @PostMapping("/info")
    public String editInfo(HttpServletRequest request, @RequestParam("upload-picture") MultipartFile file)
            throws MissingServletRequestPartException {
        //회원 찾기
        HttpSession session = request.getSession();
        Long memberId = (Long) session.getAttribute("memberId");
        Member member = memberService.findOneMember(memberId);

        //회원 정보를 받은 값으로 변경
        memberService.editMemberName(member, request.getParameter("info-name"));
        //비밀번호를 입력했다면 받은 값으로 변경
        if(request.getParameter("new-password") != null) {
            memberService.editMemberPassword(member, request.getParameter("new-password"));
        }

        //프로필 사진이 업로드 되었다면 업로드 된 사진 저장하고 변경
        String path = new File("").getAbsolutePath();
        if(file.getSize() != 0){
            try{
                //이미지 유형 추출하기
                String imgType = file.getContentType().substring(file.getContentType().indexOf('/') + 1);
                String imgName = "profile_member" + memberId + "." + imgType;
                String imgPath = path + "/src/main/resources/static/img/" + imgName;
                //기존 이미지가 있다면 삭제
                File newFile = new File(imgPath);
                if(newFile.exists()){
                    newFile.delete();
                    file.transferTo(new File(imgPath));
                    System.out.println(file);
                }else{
                    file.transferTo(newFile);
                }
                memberService.editMemberPicture(member, imgName);
            }catch(IOException e) {
                e.printStackTrace();
            }
        }
        //사진이 업로드 되지 않았다면 선택된 기본 사진을 저장
        else {
            memberService.editMemberPicture(member, request.getParameter("past-picture"));
        }

        return "redirect:/";
    }

    private void createWelcomeMessage(Member member) {
        Diary diary = Diary.createDiary("환영합니다");
        diary.changeColor("6947b4");
        Member admin = memberService.findOneByEmail("modooary");
        diarySetService.registerDiary(diary, admin);
        diarySetService.registerDiaryMember(diary, member);
        DiaryPost diaryPost = DiaryPost.createPost(diary, member, "\uD83C\uDF89모두어리에 가입하신 걸 환영합니다\uD83C\uDF89" +
                "\n새로운 모두어리를 개설하고 친구를 초대하거나," +
                "\n친구에게 초대를 부탁해보세요!");
        diaryBoardService.registerDiaryPost(diaryPost);
        PostReply postReply1 = PostReply.createPostReply(diaryPost, member,
                "이 모두어리는 다른 모두어리에 가입하면 자동으로 사라져요!");
        diaryBoardService.registerPostReply(postReply1);
        PostReply postReply2 = PostReply.createPostReply(diaryPost, admin,
                "모두어리에서 친구들과 좋은 추억을 남기세요✨");
        diaryBoardService.registerPostReply(postReply2);
    }
}
