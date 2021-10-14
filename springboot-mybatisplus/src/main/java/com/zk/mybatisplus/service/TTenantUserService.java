package com.zk.mybatisplus.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zk.mybatisplus.model.TTenantRole;
import com.zk.mybatisplus.model.TTenantUser;

/**
 * @author zk
 * @since 2021-09-08
 */
public interface TTenantUserService extends IService<TTenantUser> {

    Page<TTenantUser> findUserByPage(Integer pageNo, Integer pageSize);

    TTenantUser findUserById(Integer id);

    TTenantUser findUserByName(String name);

    TTenantRole findRoleUnderUser(Integer roleId);

}
