package com.Kim.blog.interceptor;

import com.Kim.blog.config.auth.PrincipalDetail;
import com.Kim.blog.model.User;
import com.Kim.blog.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class NotificationInterceptor implements HandlerInterceptor {

    private final ReplyRepository replyRepository;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (modelAndView != null && authentication != null && !authentication.getName().equals("anonymousUser")) {

            User user = ((PrincipalDetail) authentication.getPrincipal()).getUser();
            modelAndView.addObject("alarms", replyRepository.findReplyNotification(user.getId()));
        }
    }
}