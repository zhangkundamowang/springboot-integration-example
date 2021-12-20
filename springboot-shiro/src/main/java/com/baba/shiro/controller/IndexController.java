package com.baba.shiro.controller;

import com.baba.shiro.entity.UserInfo;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
 
/**
 * 首页控制器
 **/
@Controller
public class IndexController
{
    /**
     * 首页
     */
    @RequestMapping("/index")
    public String index(HttpServletRequest request)
    {
        //获取当前登录人
        UserInfo userInfo = (UserInfo) SecurityUtils.getSubject().getPrincipal();
        request.setAttribute("userInfo",userInfo);
        return "index.html";
    }
}