package com.bootvue.common.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.BindingResult;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ApiModel(description = "响应结果")
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1943472663923455548L;

    @ApiModelProperty("响应码")
    private Integer code;
    @ApiModelProperty("msg信息")
    private String msg;
    @ApiModelProperty("data")
    private T data;

    public static <T> R<T> success(T data) {
        return new R<>(RCode.SUCCESS.getCode(), RCode.SUCCESS.getMsg(), data);
    }

    public static <T> R<T> success() {
        return success(null);
    }

    public static <T> R<T> error(AppException e) {
        return new R<>(e.getCode(), e.getMsg(), null);
    }

    public static void handleErr(BindingResult result) {
        if (result.hasErrors()) {
            String msg = Objects.requireNonNull(result.getFieldError()).getDefaultMessage();
            throw new AppException(RCode.PARAM_ERROR.getCode(), msg);
        }
    }
}
