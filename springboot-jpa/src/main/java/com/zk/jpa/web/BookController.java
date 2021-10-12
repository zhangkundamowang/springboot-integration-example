package com.zk.jpa.web;

import com.zk.jpa.entity.Book;
import com.zk.jpa.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Book 控制层
 */
@Controller
@RequestMapping(value = "/book")
public class BookController {

    private static final String BOOK_FORM_PATH_NAME = "bookForm";
    private static final String BOOK_LIST_PATH_NAME = "bookList";
    private static final String REDIRECT_TO_BOOK_URL = "redirect:/book";

    @Autowired
    BookService bookService;

    /**
     * 获取 Book 列表
     */
    @RequestMapping(method = RequestMethod.GET)
    public String getBookList(ModelMap map) {
        //通过ModelMap传递数据
        map.addAttribute("bookList", bookService.findAll());
        //逻辑视图名为BOOK_LIST_PATH_NAME
        return BOOK_LIST_PATH_NAME;
    }

    /**
     * 获取创建 Book 表单
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String createBookForm(ModelMap map) {
        map.addAttribute("book", new Book());
        map.addAttribute("action", "create");
        return BOOK_FORM_PATH_NAME;
    }

    /**
     * 创建 Book
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String postBook(@ModelAttribute Book book) {
        bookService.insertByBook(book);
        return REDIRECT_TO_BOOK_URL;
    }

    /**
     * 获取更新 Book 表单
     */
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String getUser(@PathVariable Integer id, ModelMap map) {
        map.addAttribute("book", bookService.findById(id));
        map.addAttribute("action", "update");
        return BOOK_FORM_PATH_NAME;
    }

    /**
     * 更新 Book
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String putBook(@ModelAttribute Book book) {
        bookService.update(book);
        return REDIRECT_TO_BOOK_URL;
    }

    /**
     * 删除 Book
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deleteBook(@PathVariable Integer id) {
        bookService.delete(id);
        return REDIRECT_TO_BOOK_URL;
    }

}
