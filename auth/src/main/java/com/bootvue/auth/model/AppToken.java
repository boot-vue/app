package com.bootvue.auth.model;

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

    @ApiModelProperty("用户id")
    private Long userId;
    @ApiModelProperty("access token, 有效时间7200s")
    private String accessToken;
    @ApiModelProperty("refresh token, 有效时间7d")
    private String refreshToken;

    @ApiModelProperty("username")
    private String username;

    @ApiModelProperty("权限")
    private String authorities;

    private String phone;
    private String avatar;
    private String tenantCode;
}
