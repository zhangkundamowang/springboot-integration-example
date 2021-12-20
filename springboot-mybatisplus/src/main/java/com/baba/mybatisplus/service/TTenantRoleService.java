package com.baba.mybatisplus.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baba.mybatisplus.model.TTenantRole;
import com.baba.mybatisplus.model.TTenantUser;

import java.util.List;

/**
 * @author baba
 * @since 2021-09-08
 */
public interface TTenantRoleService extends IService<TTenantRole> {

    IPage<TTenantRole> findRoleByPage(Integer pageNo, Integer pageSize);

    TTenantRole findById(Integer id);

    List<TTenantUser> findUsersByRoleId(Integer id);

}
