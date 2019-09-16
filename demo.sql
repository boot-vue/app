/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.126.81
 Source Server Type    : MySQL
 Source Server Version : 80017
 Source Host           : 192.168.126.81:30001
 Source Schema         : demo

 Target Server Type    : MySQL
 Target Server Version : 80017
 File Encoding         : 65001

 Date: 16/09/2019 18:49:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(255) UNSIGNED NOT NULL AUTO_INCREMENT,
  `username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `roles` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `create_time` datetime(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'demo1', '34172de9e299bcc0cfebf6d1ac2f8588', 'user', '2019-09-14 16:56:22');
INSERT INTO `user` VALUES (2, 'demo2', '764fd6cdf6c498df9519e0e4e3ae5095', 'user,vip', '2019-09-14 16:56:33');

SET FOREIGN_KEY_CHECKS = 1;
