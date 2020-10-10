package com.bootvue.controller.user;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "demo response")
public class DemoObject {

    @NotEmpty(message = "必填, 不能为空")
    private String userName;
    private Integer age;

}
