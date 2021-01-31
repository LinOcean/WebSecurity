package com;

import com.bean.Order;
import com.mapper.OrderMapper;
import com.utils.CipherUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

/**
 * @author: ocean
 * @since: 2021-01-31
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class DataBaseCipherTest {
    @Autowired
    private OrderMapper mapper;

    @Test
    public void selectWithNoEncrypt() {
        List<Order> results = mapper.findAll();
        System.out.println(results.size());
    }
}
