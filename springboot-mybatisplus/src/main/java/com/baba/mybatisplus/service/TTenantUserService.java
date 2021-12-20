package com.baba.mybatisplus.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baba.mybatisplus.model.TTenantRole;
import com.baba.mybatisplus.model.TTenantUser;

/**
 * @author baba
 * @since 2021-09-08
 */
public interface TTenantUserService extends IService<TTenantUser> {

    Page<TTenantUser> findUserByPage(Integer pageNo, Integer pageSize);

    TTenantUser findUserById(Integer id);

    TTenantUser findUserByName(String name);

    TTenantRole findRoleUnderUser(Integer roleId);

}
