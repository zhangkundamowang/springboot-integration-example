package com.baba.mybatis.service;

import com.github.pagehelper.PageInfo;
import com.baba.mybatis.entity.User;

public interface UserService {

   PageInfo<User> queryUserInfo(int pageCount, int pageSize);

   PageInfo<User> selectPage(int pageCount, int pageSize);

   User findByName(String name);

}
