
package com.baba.elasticsearch.service;

import com.baba.elasticsearch.domain.City;

import java.util.List;

public interface CityService {

    /**
     * 新增城市信息
     *
     */
    Long saveCity(City city);

    /**
     * 根据关键词，function score query 权重分分页查询
     */
    List<City> searchCity(Integer pageNumber, Integer pageSize, String searchContent);
}