package com.bootvue.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AppException extends RuntimeException {
    private Integer code;
    private String msg;

    public AppException(RCode rCode) {
        this.code = rCode.getCode();
        this.msg = rCode.getMsg();
    }
}
