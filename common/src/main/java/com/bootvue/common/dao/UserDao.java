package com.bootvue.common.dao;

import com.bootvue.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByPhone(String phone);
}
