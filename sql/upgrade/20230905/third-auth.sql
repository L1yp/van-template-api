-- 第三方登录数据表
CREATE TABLE `user_third_auth` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint unsigned NOT NULL,
  `type` int NOT NULL,
  `open_id` varchar(64) COLLATE utf8mb4_bin NOT NULL,
  `union_id` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL,
  `access_token` varchar(512) COLLATE utf8mb4_bin NOT NULL,
  `avatar_url` varchar(512) COLLATE utf8mb4_bin DEFAULT NULL,
  `nickname` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL,
  `last_login_ip` varchar(64) COLLATE utf8mb4_bin NOT NULL,
  `last_login_time` datetime(3) NOT NULL,
  `status` int NOT NULL,
  `create_by` bigint unsigned NOT NULL,
  `create_time` datetime(3) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unidx_user_id_type` (`user_id`,`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
