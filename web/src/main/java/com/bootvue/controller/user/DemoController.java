package com.bootvue.controller.user;

import com.bootvue.common.result.Result;
import com.bootvue.common.result.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Api(tags = "user测试")
public class DemoController {

    @ApiOperation("user测试")
    @GetMapping("/admin/list")
    public String test() {

        return "admin";
    }

    @ApiOperation("test")
    @GetMapping("/test/list")
    public Result<DemoObject> test2() {

        return ResultUtil.success(new DemoObject("张三", 20));
    }
}
