package com.bootvue.controller.user;

import com.bootvue.common.result.Result;
import com.bootvue.common.result.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
    @PostMapping("/test/list")
    public Result<DemoObject> test2(@RequestBody @Valid DemoObject object, BindingResult result) {
        log.info("test: {}", object);
        ResultUtil.handleErr(result);
        return ResultUtil.success(new DemoObject("<div>333</div>", 20));
    }
}
