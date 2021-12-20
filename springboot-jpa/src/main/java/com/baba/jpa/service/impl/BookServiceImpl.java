package com.baba.jpa.service.impl;

import com.baba.jpa.entity.Book;
import com.baba.jpa.mapper.BookRepository;
import com.baba.jpa.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Book 业务层实现
 */
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    BookRepository bookRepository;

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book insertByBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book update(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book delete(Integer id) {
        Book book = bookRepository.findById(id).get();
        bookRepository.delete(book);
        return book;
    }

    @Override
    public Book findById(Integer id) {
        return bookRepository.findById(id).get();
    }

    @Override
    public Page<Book> selectBookByPage(Book book, Integer currentPage, Integer pageSize) {
        Specification<Book> specification = new Specification<Book>() {
            @Override
            public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate1 = criteriaBuilder.like(root.get("name").as(String.class), "%" + book.getName() + "%");
                Predicate predicate2 = criteriaBuilder.equal(root.get("writer").as(String.class), book.getWriter());
                Predicate finalPredicate = criteriaBuilder.and(predicate1, predicate2);
                return finalPredicate;
            }
        };
        Pageable pageRequest = PageRequest.of(currentPage, pageSize);
        return bookRepository.findAll(specification,pageRequest);
    }
}
