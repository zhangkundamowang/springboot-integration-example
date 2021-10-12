package com.zk.jpa.service;

import com.zk.jpa.entity.Book;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Book 业务接口层
 *
 */
public interface BookService {
    /**
     * 获取所有 Book
     */
    List<Book> findAll();

    /**
     * 新增 Book
     */
    Book insertByBook(Book book);

    /**
     * 更新 Book
     */
    Book update(Book book);

    /**
     * 删除 Book
     */
    Book delete(Integer id);

    /**
     * 获取 Book
     */
    Book findById(Integer id);

    Page<Book> selectBookByPage(Book book, Integer currentPage,Integer pageSize);

}
