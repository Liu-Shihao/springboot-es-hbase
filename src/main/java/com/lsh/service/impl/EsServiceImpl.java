package com.lsh.service.impl;

import com.lsh.entity.Book;
import com.lsh.repository.BookRepository;
import com.lsh.repository.MovieRepository;
import com.lsh.service.EsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author ：LiuShihao
 * @date ：Created in 2022/6/22 8:58 下午
 * @desc ：
 */
@Service
public class EsServiceImpl implements EsService {
    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    BookRepository bookRepository;
    @Autowired
    MovieRepository movieRepository;

    @Override
    public void insertBook(Book book) {
        bookRepository.save(book);
    }

    @Override
    public ArrayList<Book> findBooks() {
        ArrayList<Book> books = new ArrayList<>();
        Sort.Order idByDesc = new Sort.Order(Sort.Direction.DESC, "bookId");
        Iterable<Book> all = bookRepository.findAll(Sort.by(idByDesc));
        Iterator<Book> iterator = all.iterator();
        while (iterator.hasNext()){
            books.add(iterator.next());
        }
        return books;
    }

    @Override
    public Book searchBooks(String bookId) {
        return bookRepository.findByBookId(bookId);

    }
}
