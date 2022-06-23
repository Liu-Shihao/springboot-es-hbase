package com.lsh.controller;

import com.lsh.entity.Book;
import com.lsh.service.EsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：LiuShihao
 * @date ：Created in 2022/6/22 8:57 下午
 * @desc ：
 */
@RequestMapping("/es")
@RestController
public class EsController {

    private LocalDateTime now = LocalDateTime.now();

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:ss:mm");

    @Autowired
    EsService esService;

    @GetMapping("findAll")
    public String findAll(){
        return "success";
    }


    @PostMapping("/insert/book")
    public Map<String,Object> insertBook(@RequestBody Book book){
        HashMap<String, Object> hashMap = new HashMap<>();
        esService.insertBook(book);
        hashMap.put("code","200");
        hashMap.put("msg","新增成功");
        hashMap.put("timestamp",now.format(formatter));
        return hashMap;
    }

    @GetMapping("/findBooks")
    public Map<String,Object> findBooks(){
        HashMap<String, Object> hashMap = new HashMap<>();
        ArrayList<Book> list = esService.findBooks();
        hashMap.put("code","200");
        hashMap.put("msg","查询成功");
        hashMap.put("timestamp",now.format(formatter));
        hashMap.put("data",list);
        return hashMap;
    }

    @GetMapping("/searchBooks/{bookId}")
    public Map<String,Object> searchBooks(@PathVariable("bookId") String bookId){
        HashMap<String, Object> hashMap = new HashMap<>();
        Book list = esService.searchBooks(bookId);
        hashMap.put("code","200");
        hashMap.put("msg","查询成功");
        hashMap.put("timestamp",now.format(formatter));
        hashMap.put("data",list);
        return hashMap;

    }



    }
