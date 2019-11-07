package com.bootvue.controller.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class Test {

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/admin/xxx")
    public String xxx() {
        return "xxx";
    }

    @GetMapping("/user/ooo")
    public String ooo() {
        return "ooo";
    }

    @GetMapping("/oo")
    public String oo() {
        return "oo";
    }
}
