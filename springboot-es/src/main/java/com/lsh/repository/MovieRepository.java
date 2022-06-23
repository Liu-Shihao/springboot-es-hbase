package com.lsh.repository;

import com.lsh.entity.Movie;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author ：LiuShihao
 * @date ：Created in 2022/6/22 10:22 下午
 * @desc ：
 */
public interface MovieRepository extends ElasticsearchRepository<Movie,String> {
}
