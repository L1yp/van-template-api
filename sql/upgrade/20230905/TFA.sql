-- 新增 二次验证key数据表
CREATE TABLE `user_2fa_key` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint unsigned NOT NULL,
  `type` int NOT NULL,
  `secret_key` varchar(256) COLLATE utf8mb4_bin NOT NULL,
  `bound` tinyint NOT NULL,
  `create_by` bigint unsigned NOT NULL,
  `create_time` datetime(3) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uindex_user_id_type` (`user_id`,`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
