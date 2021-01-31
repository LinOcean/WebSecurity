package com.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.utils.CipherUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author: ocean
 * @since: 2021-01-31
 **/
@Configuration
@PropertySource("classpath:workKey.properties")
@EnableConfigurationProperties(DataSourceProperties.class)
public class DataSourceConfiguration {
    @Resource
    DataSourceProperties dataSourceProperties;

    @Value("${db.workKey}")
    private String dbWorkKey;

    @Bean
    public DataSource dataSource() {
        DruidDataSource druidDataSource = DruidDataSourceBuilder.create().build();
        druidDataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
        druidDataSource.setUrl(dataSourceProperties.getUrl());
        druidDataSource.setUsername(dataSourceProperties.getUsername());
        try {
            String password = CipherUtils.decrypt(dataSourceProperties.getPassword(), dbWorkKey);
            druidDataSource.setPassword(password);
            return druidDataSource;
        } catch (Exception e) {
            System.out.println("解密失败");
            throw new RuntimeException();
        }
    }
}
