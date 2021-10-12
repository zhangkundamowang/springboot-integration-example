package com.zk.mybatis.mapper;

import com.zk.mybatis.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;
import java.util.List;

public interface UserMapper extends Mapper<User> {

    @Select("SELECT ID,NAME,AGE FROM T_USER WHERE NAME = #{name}")
    User findByName(@Param("name") String name);

    /**
     * 分页查询 使用XML
     */
    List<User> queryUserInfo();

    /**
     * 分页查询用户全部用户
     */
    @Select("SELECT ID,NAME,AGE FROM T_USER ")
    List<User> selectPage();

}
