SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tenant
-- ----------------------------
DROP TABLE IF EXISTS `tenant`;
CREATE TABLE `tenant`
(
    `id`          bigint                                                       NOT NULL AUTO_INCREMENT,
    `code`        varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '租户编号',
    `name`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '租户名称',
    `create_time` datetime(0)                                                  NOT NULL,
    `update_time` datetime(0)                                                  NULL DEFAULT NULL,
    `delete_time` datetime(0)                                                  NULL DEFAULT NULL COMMENT '删除时需要同步删除租户下的用户',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `code_index` (`code`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tenant
-- ----------------------------
INSERT INTO `tenant`
VALUES (1, '000000', '平台', now(), NULL, NULL);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`          bigint                                                        NOT NULL AUTO_INCREMENT,
    `tenant_code` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   NOT NULL COMMENT '租户编号',
    `username`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '用户名',
    `phone`       varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL DEFAULT '' COMMENT '手机号, 不同租户下手机号可以重复',
    `password`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '密码',
    `avatar`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL DEFAULT '' COMMENT '头像',
    `roles`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '角色 逗号分隔',
    `status`      tinyint(1)                                                    NOT NULL DEFAULT 0 COMMENT '0:正常 1:禁用',
    `create_time` datetime(0)                                                   NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
    `update_time` datetime(0)                                                   NULL     DEFAULT NULL,
    `delete_time` datetime(0)                                                   NULL     DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `username_index` (`username`) USING BTREE,
    UNIQUE INDEX `phone_index` (`phone`, `tenant_code`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user`
VALUES (1, '000000', 'test1', '17705920000', md5('123456'), '', 'ROLE_admin', 0, now(), NULL, NULL);

INSERT INTO `user`
VALUES (2, '000000', 'test2', '17705920001', md5('123456'), '', 'ROLE_user', 0, now(), NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
