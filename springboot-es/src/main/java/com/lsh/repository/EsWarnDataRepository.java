package com.lsh.repository;

import com.lsh.entity.WarnData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author ：LiuShihao
 * @date ：Created in 2022/6/20 10:26 下午
 * @desc ：
 */
public interface EsWarnDataRepository extends ElasticsearchRepository<WarnData,String> {



}
