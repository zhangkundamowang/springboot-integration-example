package com.baba.mybatis.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.baba.mybatis.entity.User;
import com.baba.mybatis.mapper.UserMapper;
import com.baba.mybatis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public PageInfo<User> queryUserInfo(int pageCount, int pageSize) {
        //PageHelper分页
        PageHelper.startPage(pageCount,pageSize);
        List<User> userList= userMapper.queryUserInfo();
        return new PageInfo<>(userList);
    }

    @Override
    public PageInfo<User> selectPage(int pageCount, int pageSize) {
        PageHelper.startPage(pageCount,pageSize);
        List<User> userList= userMapper.selectPage();
        return new PageInfo<>(userList);
    }

    @Override
    public User findByName(String name) {
        return userMapper.findByName(name);
    }

}
