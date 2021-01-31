package com.bean;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: ocean
 * @since: 2021-01-31
 **/
@Data
public class Order {
    private Long orderId;
    private BigDecimal price;
    private Long userId;
    private String status;
}
