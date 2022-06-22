package com.lsh.repository;

import com.lsh.entity.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.ArrayList;

/**
 * @author ：LiuShihao
 * @date ：Created in 2022/6/22 10:22 下午
 * @desc ：
 */
public interface BookRepository extends ElasticsearchRepository<Book,String> {

    ArrayList<Book> findByBookContentOrBookTitleOrBookId (String content, String title, String id);
    Book findByBookId (String bookId);
}
