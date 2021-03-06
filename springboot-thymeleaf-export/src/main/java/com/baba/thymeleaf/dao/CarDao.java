package com.baba.thymeleaf.dao;

import com.baba.thymeleaf.entity.Car;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@Mapper
public interface CarDao {
    /**
     * 查询全部的车信息
     *
     * @return
     */
    List<Car> carList();

    /**
     * 批量添加车信息
     *
     * @param cars
     * @return
     */
    Integer insertCar(List<Car> cars);
}
