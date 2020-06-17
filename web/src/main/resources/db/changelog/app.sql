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
CREATE TABLE `user`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `roles` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NOT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'test1', '827ccb0eea8a706c4c34a16891f84e7b', NULL, '2020-06-17 17:10:24', NULL);
INSERT INTO `user` VALUES (2, 'test2', '827ccb0eea8a706c4c34a16891f84e7b', 'ROLE_admin', '2020-06-17 17:10:45', NULL);
INSERT INTO `user` VALUES (3, 'test3', '827ccb0eea8a706c4c34a16891f84e7b', 'ROLE_user', '2020-06-17 17:11:06', NULL);

SET FOREIGN_KEY_CHECKS = 1;
