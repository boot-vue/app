package com.bootvue.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ApiModel(description = "app token")
public class AppToken implements Serializable {
    private static final long serialVersionUID = 6189321296358268491L;
    @ApiModelProperty("access token, 有效时间7200s")
    private String accessToken;
    @ApiModelProperty("refresh token, 有效时间30d")
    private String refreshToken;
    @ApiModelProperty("剩余过期时间")
    private Long expire; // 剩余过期时间  秒

    @ApiModelProperty("username")
    @JsonIgnore
    private String username;

    @JsonIgnore
    @ApiModelProperty("权限")
    private String authorities;
}
