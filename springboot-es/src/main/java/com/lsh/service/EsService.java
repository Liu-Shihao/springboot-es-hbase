package com.lsh.service;

import com.lsh.dto.SearchDataDto;
import com.lsh.entity.Book;
import com.lsh.entity.WarnData;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author ：LiuShihao
 * @date ：Created in 2022/6/22 8:58 下午
 * @desc ：
 */
public interface EsService {
    void insertBook(Book book);

    List<Book> findBooks();

    Book searchBooks(String bookId);

    Page<WarnData> find(SearchDataDto searchDataDto);

    void insertWarnData(SearchDataDto searchDataDto);
}
