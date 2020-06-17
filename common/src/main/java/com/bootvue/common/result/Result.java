package com.bootvue.common.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ApiModel(description = "响应结果")
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1943472663923455548L;

    @ApiModelProperty("响应码")
    private Integer code;
    @ApiModelProperty("msg信息")
    private String msg;
    @ApiModelProperty("data")
    private T data;

}
