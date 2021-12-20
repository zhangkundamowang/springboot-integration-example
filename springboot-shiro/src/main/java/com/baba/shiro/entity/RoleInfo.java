package com.baba.shiro.entity;
 
import lombok.Data;

import javax.persistence.*;
import java.util.List;
 
/**
 * 角色信息实体类
 **/
@Data
@Entity
@Table(name="role_info")
public class RoleInfo
{
    //角色ID（主键、自增）
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int roleId;
 
    //角色编号
    @Column(name = "role_code")
    private String roleCode;
 
    //角色名称
    @Column(name = "role_name")
    private String roleName;
 
    //权限实体对象集合
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "role_permission_mapping",joinColumns = {@JoinColumn(name = "role_id")},inverseJoinColumns = {@JoinColumn(name="permission_id")})
    private List<PermissionInfo> permissionInfoList;

}