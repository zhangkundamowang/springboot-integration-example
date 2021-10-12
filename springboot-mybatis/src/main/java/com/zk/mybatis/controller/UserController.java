package com.zk.mybatis.controller;

import com.github.pagehelper.PageInfo;
import com.zk.mybatis.entity.User;
import com.zk.mybatis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     *  mybatis 结合pageHelper 分页
     * @param pageCount 当前页码
     * @param pageSize  每页条数
     */
    @GetMapping(value = "findUserByPage")
    public PageInfo<User> findUserByPage(@RequestParam int pageCount, @RequestParam int pageSize){
      // return  userService.selectPage(pageCount,pageSize);
       return userService.queryUserInfo(pageCount,pageSize);
    }

    @GetMapping(value = "findByName")
    public User findByName(@RequestParam String name){
       return userService.findByName(name);
    }

}
