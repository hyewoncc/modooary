package com.modooary.configuration.interceptor;

import com.modooary.service.DiarySetService;
import com.modooary.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    private final MemberService memberService;
    private final DiarySetService diarySetService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        //세션에서 로그인 정보를 얻을 수 없다면 false 반환, 로그인 화면으로 보내기
        if(request.getSession().getAttribute("memberId") == null){
            response.sendRedirect("/");
            return false;
        }else {
            return true;
        }
    }
}
