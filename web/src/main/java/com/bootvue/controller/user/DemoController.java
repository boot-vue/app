package com.bootvue.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class DemoController {

    @GetMapping("/admin/list")
    public String test() {

        return "admin";
    }

    @GetMapping("/test/list")
    public String test2() {

        return "test";
    }
}
