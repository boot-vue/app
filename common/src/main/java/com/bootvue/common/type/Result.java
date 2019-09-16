package com.bootvue.common.type;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1943472663923455548L;

    private Integer code;
    private String msg;
    private T data;

}
