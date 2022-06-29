package com.lsh.service.impl;

import com.google.common.collect.Lists;
import com.lsh.dto.SearchDataDto;
import com.lsh.entity.Book;
import com.lsh.entity.WarnData;
import com.lsh.repository.BookRepository;
import com.lsh.repository.EsWarnDataRepository;
import com.lsh.repository.MovieRepository;
import com.lsh.service.EsService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author ：LiuShihao
 * @date ：Created in 2022/6/22 8:58 下午
 * @desc ：
 */
@Service
public class EsServiceImpl implements EsService {

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

//    @Autowired
//    ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    BookRepository bookRepository;
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    EsWarnDataRepository esWarnDataRepository;

    @Override
    public void insertBook(Book book) {
        bookRepository.save(book);
    }

    @Override
    public List<Book> findBooks() {
        ArrayList<Book> books = new ArrayList<>();
        Sort.Order idByDesc = new Sort.Order(Sort.Direction.DESC, "bookId");
        Iterable<Book> all = bookRepository.findAll(Sort.by(idByDesc));
        Iterator<Book> iterator = all.iterator();
//        while (iterator.hasNext()){
//            books.add(iterator.next());
//        }

        List<Book> resultList = Lists.newArrayList(iterator);

        return resultList;
    }

    @Override
    public Book searchBooks(String bookId) {
        return bookRepository.findByBookId(bookId);

    }

    /**
     * 注意 数字字符串不会进行分词，中文可以分词
     * matchQuery 相当于like 模糊查询  会进行分词，必须是text类型
     * matchPhraseQuery 查询 相当于 = ，不会进行分词
     * termQuery 等值搜索：相当于sql语句中的“=”，使用这个搜索一般是对索引中keyword的mapping进行等值搜索。term query 属于过滤器查询，可以处理数字（numbers）、布尔值（Booleans）、日期（dates）以及文本（text）
     * 组合查询 ： must（and，必须匹配）、should(or，或者)、mustNot(!=，必须不匹配)
     * 范围查询：rangeQuery，range query可以处理数字（numbers）、日期（dates）以及字符串，不过字符串还是不要用范围查询的好，效率会很低
     *
     * 闭区间：   where age >=2 and age <=4    QueryBuilders.rangeQuery("age").from(age1).to(age2)
     * 开区间：   where age >2 and age <4      QueryBuilders.rangeQuery("age").from(age1,false).to(age2,false)
     * 大于：    where age >1                  QueryBuilders.rangeQuery("age").gt(age)
     * 大于等于： where age >=1                 QueryBuilders.rangeQuery("age").gte(age1)
     * 小于：    where age <4                  QueryBuilders.rangeQuery("age").lt(age1)
     * 小于等于： where age <=4                 QueryBuilders.rangeQuery("age").lte(age1)
     *
     * @param searchDataDto
     * @return
     */
    @Override
    public Page<WarnData> find(SearchDataDto searchDataDto) {
        int pageNum = searchDataDto.getPageNum() == null ? 1: searchDataDto.getPageNum();
        int pageSize = searchDataDto.getPageSize() == null ? 10: searchDataDto.getPageSize();
        //创建查询对象
        BoolQueryBuilder base_query = QueryBuilders.boolQuery();
        if (searchDataDto.getWarnId() != null && !"".equals(searchDataDto.getWarnId())){
            base_query.must(QueryBuilders.termQuery("warn_id",searchDataDto.getWarnId() ));
        }
        if (searchDataDto.getEarlyWarningPhone() != null && !"".equals(searchDataDto.getEarlyWarningPhone())){
            base_query.should(QueryBuilders.matchQuery("warning_phone",searchDataDto.getEarlyWarningPhone() ));
        }
        if (searchDataDto.getEarlyWarningType() != null && !"".equals(searchDataDto.getEarlyWarningType())){
            base_query.must(QueryBuilders.termQuery("warning_type",searchDataDto.getEarlyWarningType() ));
        }
        if (searchDataDto.getEarlyWarningLevel() != null && !"".equals(searchDataDto.getEarlyWarningLevel())){
            base_query.must(QueryBuilders.termQuery("warning_level",searchDataDto.getEarlyWarningLevel() ));
        }
        if (searchDataDto.getProvinceCode() != null && !"".equals(searchDataDto.getProvinceCode())){
            base_query.must(QueryBuilders.termQuery("province_code",searchDataDto.getProvinceCode() ));
        }
        if (searchDataDto.getCityCode() != null && !"".equals(searchDataDto.getCityCode())){
            base_query.must(QueryBuilders.termQuery("city_code",searchDataDto.getCityCode() ));
        }
        if (searchDataDto.getAreaCode() != null && !"".equals(searchDataDto.getAreaCode())){
            base_query.must(QueryBuilders.termQuery("area_code",searchDataDto.getAreaCode() ));
        }
        if (searchDataDto.getPoliceCode() != null && !"".equals(searchDataDto.getPoliceCode())){
            base_query.must(QueryBuilders.termQuery("police_code",searchDataDto.getPoliceCode() ));
        }
        if (searchDataDto.getDataSources() != null && !"".equals(searchDataDto.getDataSources())){
            base_query.must(QueryBuilders.termQuery("data_source",searchDataDto.getDataSources() ));
        }

        //时间范围查询
        if(searchDataDto.getReceivingStartTime()!=null && !"".equals(searchDataDto.getReceivingStartTime())){
            base_query.must(QueryBuilders.rangeQuery("receive_time").
                    gte(searchDataDto.getReceivingStartTime()));//大于等于开始时间
        }
        if(searchDataDto.getReceivingEndTime()!=null && !"".equals(searchDataDto.getReceivingEndTime()) ){
            base_query.must(QueryBuilders.rangeQuery("receive_time").
                    lte(searchDataDto.getReceivingEndTime()));//小于等于结束时间
        }

        //设置分页和根据创建时间降序排序  注意：page从0开始
        PageRequest page = PageRequest.of
                (pageNum-1, pageSize, Sort.by(Sort.Order.desc("receive_time")));
        Page<WarnData> data = esWarnDataRepository.search(base_query, page);

        return data;
    }

    @Override
    public void insertWarnData(SearchDataDto searchDataDto) {
        WarnData warnData = new WarnData();
        warnData.setWarn_id(searchDataDto.getWarnId());
        warnData.setPolice_code(searchDataDto.getPoliceCode());
        warnData.setWarning_phone(searchDataDto.getEarlyWarningPhone());
        warnData.setProvince_code(searchDataDto.getProvinceCode());
        warnData.setCity_code(searchDataDto.getCityCode());
        warnData.setArea_code(searchDataDto.getAreaCode());
        warnData.setData_source(searchDataDto.getDataSources());
        warnData.setWarning_level(searchDataDto.getEarlyWarningLevel());
        warnData.setWarning_type(searchDataDto.getEarlyWarningType());
        warnData.setReceive_time(LocalDateTime.now().format(dtf));

        esWarnDataRepository.save(warnData);

    }
}
