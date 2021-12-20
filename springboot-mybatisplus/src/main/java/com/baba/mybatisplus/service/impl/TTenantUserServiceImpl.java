package com.baba.mybatisplus.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baba.mybatisplus.mapper.TTenantUserMapper;
import com.baba.mybatisplus.model.TTenantRole;
import com.baba.mybatisplus.model.TTenantUser;
import com.baba.mybatisplus.service.TTenantUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author baba
 * @since 2021-09-08
 */
@Service
public class TTenantUserServiceImpl extends ServiceImpl<TTenantUserMapper, TTenantUser> implements TTenantUserService {

    @Resource
    private TTenantUserMapper userMapper;

    @Override
    public Page<TTenantUser> findUserByPage(Integer pageNo, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 100);
        Page<TTenantUser> page = new Page<>(pageNo, pageSize);
        page.setRecords(userMapper.selectUserPage(map, page));
        return page;
    }

    @Override
    public TTenantUser findUserById(Integer id) {
        return userMapper.getUserById(id);
    }

    @Override
    public TTenantUser findUserByName(String name) {
        return userMapper.getUserByName(name);
    }

    @Override
    public TTenantRole findRoleUnderUser(Integer roleId) {
        return userMapper.findRoleUnderUser(roleId);
    }


}
