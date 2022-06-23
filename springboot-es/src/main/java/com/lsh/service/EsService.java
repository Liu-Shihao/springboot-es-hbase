package com.lsh.service;

import com.lsh.entity.Book;

import java.util.ArrayList;

/**
 * @author ：LiuShihao
 * @date ：Created in 2022/6/22 8:58 下午
 * @desc ：
 */
public interface EsService {
    void insertBook(Book book);

    ArrayList<Book> findBooks();

    Book searchBooks(String bookId);
}
