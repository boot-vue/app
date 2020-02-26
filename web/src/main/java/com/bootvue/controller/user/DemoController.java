package com.bootvue.controller.user;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/admin/test")
    public String test() {
        SecurityContext context = SecurityContextHolder.getContext();

        return "test";
    }
}
