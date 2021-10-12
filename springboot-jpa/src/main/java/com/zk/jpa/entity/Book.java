package com.zk.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Book 实体类
 * <p>
 * Created by bysocket on 30/09/2017.
 */
@Entity
public class Book implements Serializable {

    /**
     * JPA提供的GenerationType四种标准用法为
     *
     * TABLE：使用一个特定的数据库表格来保存主键。
     *
     * SEQUENCE：根据底层数据库的序列来生成主键，条件是数据库支持序列。
     *
     * IDENTITY：主键由数据库自动生成（主要是自动增长型）
     *
     * AUTO：主键由程序控制。默认的生成策略就是GenerationType.AUTO
     *
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 书名
     */
    private String name;

    /**
     * 作者
     */
    private String writer;

    /**
     * 简介
     */
    private String introduction;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
