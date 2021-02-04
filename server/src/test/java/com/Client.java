package com;

import com.alibaba.fastjson.JSON;
import com.bean.RequestInfo;
import com.utils.HmacUtil;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: ocean
 * @since: 2021-02-03
 **/
@RunWith(SpringRunner.class)
public class Client {
    private String key = "d79742ea7ec2543963620a128cfef8acb45492fec3c035b0070b7fcc485068d3";

    @Test
    public void test() throws Exception {
        String url = "http://localhost:56000/digest";
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type",
                "application/json;charset=utf-8");
        String content = JSON.toJSONString(new RequestInfo("helloWorld"));
        StringEntity entity = new StringEntity(content, ContentType.APPLICATION_JSON);
        httpPost.setEntity(entity);
        String digest = HmacUtil.hmacSHA256Str(key, content);
        StringBuilder builder = new StringBuilder();
        builder.append("Digest appID=")
                .append("yourAppId")// 这里设置服务器标识
                .append(",")
                .append("response=")
                .append(digest);
        httpPost.setHeader("Authorization", builder.toString());
        CloseableHttpResponse response = HttpClients.createDefault().execute(httpPost);
        System.out.println(response.getStatusLine().getStatusCode());
    }
}
