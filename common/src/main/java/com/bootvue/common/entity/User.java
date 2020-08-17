package com.bootvue.common.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
public class User implements Serializable {
    private static final long serialVersionUID = 7253892273370121860L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String phone;
    private String password;
    // 必须以ROLE_开头  , 分隔
    private String roles;

    private Date createTime;
    private Date updateTime;
}
