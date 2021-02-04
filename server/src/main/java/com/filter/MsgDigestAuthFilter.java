package com.filter;

import com.utils.CipherUtils;
import com.utils.HmacUtil;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: ocean
 * @since: 2021-02-02
 **/
@PropertySource("classpath:workKey.properties")
@WebFilter(filterName = "digestAuthFilter", urlPatterns = "/*")
public class MsgDigestAuthFilter implements Filter {
    @Value("${digest.workKey}")
    private String digestWorkKey;

    private byte[] key;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        try {
            // 这里简单地对密钥进行转码，实际上要进行解密
            key = Hex.decodeHex(digestWorkKey);
        } catch (DecoderException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 这里对摘要进行校验
        HttpServletRequest request = (HttpServletRequest) req;
        String jsonStr = getJsonStr(request);
        String head = request.getHeader("Authorization");
        if (checkHeadAuthorization(head, jsonStr)) {
            filterChain.doFilter(request, servletResponse);
            return;
        }
        // log,抛自定义异常
        throw new ServletException("No Permission!");
    }

    private boolean checkHeadAuthorization(String head, String body) {
        if (StringUtils.isEmpty(head)) {
            //log
            return false;
        }
        Map<String, String> authValue = getAuthValue(head);
        String response = authValue.get("response");
        if (StringUtils.isEmpty(response)) {
            return false;
        }
        try {
            String currentResp = HmacUtil.hmacSHA256Str(key, body);
            return response.equals(currentResp);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getJsonStr(HttpServletRequest request) {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();

    }

    /**
     * 这里根据自己定义的规则获取鉴权信息，仅供参考
     *
     * @param authorization authorization
     * @return Map<String, String>
     */
    private Map<String, String> getAuthValue(String authorization) {
        Map<String, String> map = new HashMap<String, String>();
        String[] token = authorization.split(",");
        for (String each : token) {
            String[] subToken = each.split("=");
            if (subToken.length > 1) {
                String key = subToken[0].trim();
                String[] keyArray = key.split(" ");
                if (keyArray.length > 1) {
                    key = keyArray[keyArray.length - 1];
                }
                map.put(key, subToken[1]);
            }
        }
        return map;
    }
}
