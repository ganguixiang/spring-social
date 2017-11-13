package com.lese.spring.social.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by ganguixiang on 2017/11/11.
 */
@Controller
@RequestMapping("login")
public class LoginController {

    @RequestMapping("wechat")
    public void wechat(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String code = request.getParameter("code");
//        String url = "/connect/wechat?code=" + code;
//        response.sendRedirect(url);
        request.getRequestDispatcher("/connect/wechat").forward(request,response);
    }
}
