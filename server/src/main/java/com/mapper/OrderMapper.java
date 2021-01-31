package com.mapper;

import com.bean.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;
import java.util.Map;

/**
 * @author: ocean
 * @since: 2021-01-20
 **/
@Mapper
public interface OrderMapper {
    List<Order> findAll();
}
