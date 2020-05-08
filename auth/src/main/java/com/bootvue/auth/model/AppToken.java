package com.bootvue.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AppToken implements Serializable {
    private static final long serialVersionUID = 6189321296358268491L;
    private String accessToken;
    private String refreshToken;
    private Long expire; // 剩余过期时间  秒

    @JsonIgnore
    private String username;
    @JsonIgnore
    private String authorities;
}
