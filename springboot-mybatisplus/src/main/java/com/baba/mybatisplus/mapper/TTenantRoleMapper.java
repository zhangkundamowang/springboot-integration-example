package com.baba.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baba.mybatisplus.model.TTenantRole;
import com.baba.mybatisplus.model.TTenantUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author baba
 * @since 2021-09-08
 */
public interface TTenantRoleMapper extends BaseMapper<TTenantRole> {

    List<TTenantRole> findAll();

    TTenantRole findById(@Param("id") Integer id);

    List<TTenantUser> findUsersByRoleId(@Param("id") Integer id);
}
