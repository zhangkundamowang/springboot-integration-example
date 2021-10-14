package com.zk.elasticsearch.repository;

import com.zk.elasticsearch.domain.City;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CityRepository extends ElasticsearchRepository<City,Long> {


}
