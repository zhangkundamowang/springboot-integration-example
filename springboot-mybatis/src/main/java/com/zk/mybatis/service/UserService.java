package com.zk.mybatis.service;

import com.github.pagehelper.PageInfo;
import com.zk.mybatis.entity.User;

public interface UserService {

   PageInfo<User> queryUserInfo(int pageCount, int pageSize);

   PageInfo<User> selectPage(int pageCount, int pageSize);

   User findByName(String name);

}
