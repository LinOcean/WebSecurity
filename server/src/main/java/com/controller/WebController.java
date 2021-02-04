package com.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: ocean
 * @since: 2021-02-02
 **/
@RestController
public class WebController {

    @PostMapping("digest")
    public String testDigest() {
        System.out.println("ok");
        return "ok";
    }
}
