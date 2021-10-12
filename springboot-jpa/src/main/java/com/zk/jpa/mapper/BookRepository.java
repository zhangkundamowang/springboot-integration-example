package com.zk.jpa.mapper;

import com.zk.jpa.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Book 数据持久层操作接口
 *JpaRepository<实体类类型,主键类型> 用来完成基本CRUD操作
 * JpaSpecificationExecutor<实体类类型> 用来完成复杂查询操作(分页)
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Integer>,JpaSpecificationExecutor<Book> {

    @Query("select s from Book s where s.id = :id")
    Book selectBookById(@Param("id") Integer id );

}
