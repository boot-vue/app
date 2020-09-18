/*
 Navicat Premium Data Transfer

 Source Server         : master
 Source Server Type    : MySQL
 Source Server Version : 80020
 Source Host           : 192.168.221.80:3306
 Source Schema         : app

 Target Server Type    : MySQL
 Target Server Version : 80020
 File Encoding         : 65001

 Date: 17/06/2020 17:13:29
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`          bigint(0)                                                     NOT NULL AUTO_INCREMENT,
    `username`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL,
    `phone`       varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci           DEFAULT NULL,
    `password`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL,
    `roles`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL,
    `status`      tinyint(1)                                                    NOT NULL DEFAULT 0,
    `create_time` datetime(0)                                                   NOT NULL DEFAULT NOW(),
    `update_time` datetime(0)                                                   NULL     DEFAULT NULL,
    `delete_time` datetime(0)                                                   NULL     DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `username_index` (`username`),
    UNIQUE INDEX `phone_index` (`phone`)
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user`
VALUES (1, 'test1', '12345678900', '827ccb0eea8a706c4c34a16891f84e7b', NULL, 0, '2020-06-17 17:10:24', NULL, NULL);
INSERT INTO `user`
VALUES (2, 'test2', '12345678901', '827ccb0eea8a706c4c34a16891f84e7b', 'ROLE_admin', 0, '2020-06-17 17:10:45', NULL,
        NULL);
INSERT INTO `user`
VALUES (3, 'test3', '12345678902', '827ccb0eea8a706c4c34a16891f84e7b', 'ROLE_user', 0, '2020-06-17 17:11:06', NULL,
        NULL);

SET FOREIGN_KEY_CHECKS = 1;
