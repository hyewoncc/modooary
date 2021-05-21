package com.modooary.controller;

import com.modooary.controller.form.MemberForm;
import com.modooary.domain.Member;
import com.modooary.domain.PreMember;
import com.modooary.service.MemberService;
import com.modooary.utils.EmailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final EmailUtil emailUtil;

    @GetMapping("/sign-in")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "signIn";
    }

    @PostMapping("/sign-in")
    public String sendSignInMail(@Valid MemberForm memberForm, BindingResult result, Model model) {

        //공란이 있을 시 재작성
        if (result.hasErrors()) {
            return "signIn";
        }

        //입력 정보를 받아 임시 회원으로 저장
        PreMember preMember = PreMember.createPreMember(
                memberForm.getEmail(), memberForm.getPassword(), memberForm.getName());
        memberService.joinPreMember(preMember);

        //임시 회원 정보를 바탕으로 확인 이메일 발송 후 메인으로 리다이렉트
        try {
            emailUtil.sendMail(
                    preMember.getName(), preMember.getEmail(), preMember.getId(), preMember.getKey());
            model.addAttribute("emailAlert", "success");
        } catch (MessagingException e) {
            model.addAttribute("emailAlert", "fail");
            e.printStackTrace();
        }

        return "redirect:/";
    }

    //인증 메일 링크로 들어올 시 파라미터 값을 DB와 비교하여 일치 시 정회원으로 등록
    @GetMapping("/sign-up/confirm")
    public String signUpConfirm(
            @RequestParam("id") Long id, @RequestParam("key") String key) {

        //임시 회원의 키값 조회 후 일치시 정회원 전환
        if (memberService.checkPreMemberKey(id, key)) {
            memberService.approveMember(id);
        }

        return "redirect:/";
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
}
