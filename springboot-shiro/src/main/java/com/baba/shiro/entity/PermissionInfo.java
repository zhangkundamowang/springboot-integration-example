package com.baba.shiro.entity;
 
import lombok.Data;

import javax.persistence.*;
 
/**
 * 权限信息实体类
 **/
@Data
@Entity
@Table(name="permission_info")
public class PermissionInfo
{
    //权限ID（主键、自增）
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private int permissionId;
 
    //权限编号
    @Column(name = "permission_code")
    private String permissionCode;
 
    //权限名称
    @Column(name = "permission_name")
    private String permissionName;

}