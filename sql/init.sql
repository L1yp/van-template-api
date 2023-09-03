-- MySQL dump 10.13  Distrib 8.0.29, for Linux (x86_64)
--
-- ------------------------------------------------------
-- Server version	8.0.29-0ubuntu0.20.04.3

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `data-check`
--


--
-- Table structure for table `sys_dept`
--

DROP TABLE IF EXISTS `sys_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dept` (
  `id` bigint unsigned NOT NULL COMMENT '主键ID',
  `name` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '部门名称',
  `simple_name` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '部门简称',
  `description` varchar(512) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '部门描述',
  `code` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '部门编号',
  `order_no` int unsigned NOT NULL DEFAULT '0' COMMENT '排序号',
  `pid` bigint DEFAULT NULL COMMENT '上级部门ID',
  `owner` bigint DEFAULT NULL COMMENT '部门领导',
  `assistant` bigint DEFAULT NULL COMMENT '部门助理',
  `phone` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '部门电话',
  `office_location` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '办公地点',
  `address` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '详细地址',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态',
  `create_by` bigint unsigned NOT NULL COMMENT '创建人',
  `update_by` bigint unsigned NOT NULL COMMENT '更新人',
  `update_time` datetime(3) NOT NULL COMMENT '更新时间',
  `create_time` datetime(3) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='部门表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dept`
--

LOCK TABLES `sys_dept` WRITE;
/*!40000 ALTER TABLE `sys_dept` DISABLE KEYS */;
INSERT INTO `sys_dept` VALUES (1,'腾讯科技有限责任公司','腾讯','','Tencent',1,NULL,NULL,NULL,NULL,NULL,NULL,1,1,1,'2023-08-27 12:03:46.887','2022-02-20 00:33:19.000'),(2,'全球研发中心','全球研发中心','','CE',1,1,NULL,NULL,NULL,NULL,NULL,1,1,1,'2022-11-06 23:37:10.000','2022-02-20 00:56:30.000'),(3,'总部','总部','','HQ',2,1,NULL,NULL,NULL,NULL,NULL,1,1,1,'2022-10-14 14:16:08.000','2022-02-20 00:59:41.000'),(4,'长沙','长沙','','WL',3,1,NULL,NULL,NULL,NULL,NULL,1,1,1,'2022-11-06 23:36:54.000','2022-02-20 01:00:14.000'),(5,'常州','常州','','EX',4,1,NULL,NULL,NULL,NULL,NULL,1,1,1,'2022-11-06 23:37:00.000','2022-02-20 01:14:19.000'),(6,'杭州','杭州','','DL',5,1,NULL,NULL,NULL,NULL,NULL,1,1,1,'2022-11-06 23:37:04.000','2022-02-20 01:14:48.000'),(7,'数字信息','数字信息','','CE001',1,2,NULL,NULL,NULL,NULL,NULL,1,1,1,'2022-10-14 14:11:43.000','2022-02-20 01:17:21.000'),(8,'工业设计','工业设计','','CE013',2,2,NULL,NULL,NULL,NULL,NULL,1,1,1,'2022-11-08 22:30:57.000','2022-02-20 01:17:38.000'),(9,'智能设计院','智能设计院','','CE006',3,2,NULL,NULL,NULL,NULL,NULL,1,1,1,'2022-10-14 14:11:06.000','2022-02-20 01:18:31.000'),(10,'大数据','大数据','','CE014',1,9,NULL,NULL,NULL,NULL,NULL,1,1,1,'2022-11-08 22:31:06.000','2022-02-20 01:18:48.000'),(11,'智能施工','智能施工','','CE016',3,9,NULL,NULL,NULL,NULL,NULL,1,1,1,'2022-11-08 22:31:14.000','2022-02-20 01:24:25.000'),(12,'智能产品','智能产品','','CE015',4,9,NULL,NULL,NULL,NULL,NULL,1,1,1,'2022-11-08 22:31:24.000','2022-02-20 01:26:03.000'),(13,'总部DT','总部DT','总部DT','HQ-DT',1,3,NULL,NULL,NULL,NULL,NULL,1,1,1,'2022-11-08 22:31:29.000','2022-08-26 00:50:28.000'),(14,'HQ-IT','HQ-IT','总部IT','HQ-IT',2,3,NULL,NULL,NULL,NULL,NULL,1,1,1,'2022-11-08 22:31:35.000','2022-08-26 21:56:08.000');
/*!40000 ALTER TABLE `sys_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '菜单名称',
  `pid` bigint unsigned DEFAULT NULL COMMENT '上级菜单',
  `type` int unsigned NOT NULL COMMENT '菜单类型',
  `path` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '路由地址',
  `component` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '组件地址',
  `meta` json DEFAULT NULL COMMENT '元数据',
  `order_no` int NOT NULL DEFAULT '1' COMMENT '排序号',
  `status` int NOT NULL COMMENT '状态',
  `remark` varchar(512) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '说明',
  `create_by` bigint unsigned NOT NULL COMMENT '创建人',
  `update_by` bigint unsigned NOT NULL COMMENT '更新人',
  `update_time` datetime(3) NOT NULL COMMENT '更新时间',
  `create_time` datetime(3) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='系统菜单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (1,'Dashboard',NULL,3,'/home','/home','{\"icon\": \"House\"}',1,1,NULL,1,1,'2023-08-15 13:38:58.467','2023-08-12 22:30:01.000'),(2,'系统管理',NULL,1,'/sys',NULL,'{\"icon\": \"Setting\"}',2,1,NULL,1,1,'2023-08-12 22:30:00.000','2023-08-12 22:30:01.000'),(3,'菜单管理',2,3,'/sys/menu','/sys/menu/menus','{\"icon\": \"Menu\", \"closeable\": true}',2,1,NULL,1,1,'2023-08-13 10:27:25.448','2023-08-12 22:30:01.000'),(4,'角色管理',2,3,'/sys/role','/sys/role/roles','{\"icon\": \"UserRole\", \"closeable\": true}',1,1,NULL,1,1,'2023-08-13 10:27:19.428','2023-08-13 00:23:51.629'),(5,'用户管理',2,3,'/sys/user','/sys/user/users','{\"icon\": \"User\", \"closeable\": true}',3,1,'用户管理',1,1,'2023-08-13 01:02:49.788','2023-08-13 01:02:49.788'),(7,'数据管理',NULL,1,NULL,NULL,'{\"icon\": \"Menu\"}',3,1,NULL,1,1,'2023-08-13 14:57:00.364','2023-08-13 14:57:00.364'),(9,'数据列表',7,3,'/data/many','/business/data/data-list','{\"icon\": \"Menu\", \"closeable\": true}',1,1,NULL,1,1,'2023-08-20 00:06:47.315','2023-08-13 14:58:45.052'),(10,'权限管理',2,3,'/sys/perm','/sys/perm/perms','{\"icon\": \"UserRole\", \"closeable\": true}',4,1,'',1,1,'2023-08-15 13:47:04.096','2023-08-15 13:47:04.096'),(12,'登录日志',2,3,'/sys/login/log','/sys/login-log/log','{\"icon\": \"Operation\", \"closeable\": true}',6,1,'登录日志',1,1,'2023-08-27 10:04:24.046','2023-08-15 15:15:44.955'),(13,'部门管理',2,3,'/sys/dept','/sys/dept/department','{\"icon\": \"Department\", \"closeable\": true}',5,1,'部门管理页面',1,1,'2023-08-27 10:05:31.151','2023-08-27 10:05:31.151');
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_perm`
--

DROP TABLE IF EXISTS `sys_perm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_perm` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `pid` bigint DEFAULT NULL,
  `perm_key` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '权限标识符',
  `name` varchar(128) COLLATE utf8mb4_bin NOT NULL COMMENT '名称',
  `order_no` int NOT NULL,
  `create_by` bigint unsigned NOT NULL,
  `create_time` datetime(3) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `perms_perm_key_index` (`perm_key`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='权限列表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_perm`
--

LOCK TABLES `sys_perm` WRITE;
/*!40000 ALTER TABLE `sys_perm` DISABLE KEYS */;
INSERT INTO `sys_perm` VALUES (1,6,'data.import.multiple','多行导入数据',0,1,'2023-08-13 14:44:08.000'),(2,6,'data.page','分页查询数据',0,1,'2023-08-15 08:58:15.000'),(3,6,'data.export','导出数据',0,1,'2023-08-15 08:58:15.000'),(4,6,'data.delete','删除数据',0,1,'2023-08-15 08:58:15.000'),(5,6,'data.truncate','清空数据',0,1,'2023-08-15 08:58:15.000'),(6,NULL,'data','数据管理',5,1,'2023-08-15 08:58:15.000'),(7,NULL,'menu','菜单管理',3,1,'2023-08-15 08:58:15.000'),(8,7,'menu.add','添加菜单',0,1,'2023-08-15 08:58:15.000'),(9,7,'menu.delete','删除菜单',0,1,'2023-08-15 08:58:15.000'),(10,7,'menu.list','查询全部菜单',0,1,'2023-08-15 08:58:15.000'),(11,7,'menu.update','更新菜单',0,1,'2023-08-15 08:58:15.000'),(12,7,'menu.entity.list','查询菜单列表',0,1,'2023-08-15 08:58:15.000'),(13,NULL,'perm','权限管理',4,1,'2023-08-15 08:58:15.000'),(14,13,'perm.add','添加权限',0,1,'2023-08-15 08:58:15.000'),(15,13,'perm.delete','删除权限',0,1,'2023-08-15 08:58:15.000'),(16,13,'perm.list','查询全部权限',0,1,'2023-08-15 08:58:15.000'),(17,13,'perm.entity.list','查询权限列表',0,1,'2023-08-15 08:58:15.000'),(18,13,'perm.page','查询分页权限',0,1,'2023-08-15 08:58:15.000'),(19,13,'perm.update','更新权限',0,1,'2023-08-15 08:58:15.000'),(20,NULL,'role','角色管理',2,1,'2023-08-15 08:58:15.000'),(21,20,'role.add','添加角色',0,1,'2023-08-15 08:58:15.000'),(22,20,'role.delete','删除角色',0,1,'2023-08-15 08:58:15.000'),(23,20,'role.list','查询全部角色',0,1,'2023-08-15 08:58:15.000'),(24,20,'role.entity.list','查询角色列表',0,1,'2023-08-15 08:58:15.000'),(25,20,'role.page','查询分页角色',0,1,'2023-08-15 08:58:15.000'),(26,20,'role.update','更新角色',0,1,'2023-08-15 08:58:15.000'),(27,20,'role.menu.list','查询绑定菜单列表',0,1,'2023-08-15 08:58:15.000'),(28,20,'role.menu.bind','绑定角色菜单',0,1,'2023-08-15 08:58:15.000'),(29,20,'role.perm.list','查询绑定权限列表',0,1,'2023-08-15 08:58:15.000'),(30,20,'role.perm.bind','绑定角色权限',0,1,'2023-08-15 08:58:15.000'),(31,NULL,'user','用户管理',1,1,'2023-08-15 08:58:15.000'),(32,31,'user.role.list','查询用户角色列表',0,1,'2023-08-15 08:58:15.000'),(33,31,'user.role.bind','绑定用户角色',0,1,'2023-08-15 08:58:15.000'),(34,31,'user.perm.list','查询用户权限列表',0,1,'2023-08-15 08:58:15.000'),(35,31,'user.add','添加用户',0,1,'2023-08-15 08:58:15.000'),(36,31,'user.delete','删除用户',0,1,'2023-08-15 08:58:15.000'),(37,31,'user.list','查询全部用户',0,1,'2023-08-15 08:58:15.000'),(38,31,'user.entity.list','查询用户列表',0,1,'2023-08-15 08:58:15.000'),(39,31,'user.page','查询分页用户',0,1,'2023-08-15 08:58:15.000'),(40,31,'user.update','更新用户',0,1,'2023-08-15 08:58:15.000'),(41,31,'user.password.change','修改密码',0,1,'2023-08-15 08:58:15.000'),(42,NULL,'user.login.log','用户登录日志',6,1,'2023-08-19 23:56:12.002'),(44,42,'user.login.log.page','分页查询',1,1,'2023-08-20 00:01:08.743'),(45,NULL,'dept','部门管理',7,1,'2023-08-26 12:11:51.425'),(46,45,'dept.add','新增部门',1,1,'2023-08-26 12:12:07.624'),(47,45,'dept.update','更新部门',2,1,'2023-08-26 12:12:42.007'),(48,45,'dept.list','查询部门列表',3,1,'2023-08-26 12:12:55.150'),(49,45,'dept.delete','删除部门',4,1,'2023-08-26 12:13:08.710'),(50,31,'user.dept.list','查询用户兼职部门列表',0,1,'2023-08-26 23:27:58.070');
/*!40000 ALTER TABLE `sys_perm` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '角色名称',
  `category` varchar(16) COLLATE utf8mb4_bin NOT NULL COMMENT '分类',
  `order_no` int NOT NULL COMMENT '排序号',
  `status` tinyint DEFAULT '0' COMMENT '状态',
  `create_by` bigint unsigned NOT NULL COMMENT '更新人',
  `update_by` bigint unsigned NOT NULL COMMENT '更新人',
  `update_time` datetime(3) DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime(3) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='角色';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'系统管理员','默认',1,1,1,1,'2023-08-10 22:50:28.000','2023-08-10 22:50:29.000'),(2,'成员','默认',2,1,1,1,'2023-08-10 23:01:05.000','2023-08-10 23:01:06.000');
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_menu`
--

DROP TABLE IF EXISTS `sys_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` bigint unsigned NOT NULL COMMENT '角色ID',
  `menu_id` bigint unsigned NOT NULL COMMENT '菜单ID',
  `create_by` bigint unsigned NOT NULL COMMENT '创建人',
  `create_time` datetime(3) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sys_role_menu_role_id_menu_id_uindex` (`role_id`,`menu_id`),
  KEY `sys_role_menu_menu_idx` (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=142 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='角色菜单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_menu`
--

LOCK TABLES `sys_role_menu` WRITE;
/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;
INSERT INTO `sys_role_menu` VALUES (119,2,1,1,'2023-08-26 12:10:32.920'),(120,2,2,1,'2023-08-26 12:10:32.922'),(121,2,7,1,'2023-08-26 12:10:32.924'),(122,2,9,1,'2023-08-26 12:10:32.926'),(132,1,1,1,'2023-08-27 10:05:43.444'),(133,1,2,1,'2023-08-27 10:05:43.450'),(134,1,3,1,'2023-08-27 10:05:43.451'),(135,1,4,1,'2023-08-27 10:05:43.452'),(136,1,5,1,'2023-08-27 10:05:43.453'),(137,1,10,1,'2023-08-27 10:05:43.454'),(138,1,12,1,'2023-08-27 10:05:43.455'),(139,1,13,1,'2023-08-27 10:05:43.456'),(140,1,7,1,'2023-08-27 10:05:43.456'),(141,1,9,1,'2023-08-27 10:05:43.457');
/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_perm`
--

DROP TABLE IF EXISTS `sys_role_perm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_perm` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `role_id` bigint unsigned NOT NULL,
  `perm` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '权限标识符',
  `create_by` bigint unsigned NOT NULL,
  `create_time` datetime(3) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_perm_perm_role_id_uindex` (`perm`,`role_id`),
  UNIQUE KEY `role_perm_role_id_perm_uindex` (`role_id`,`perm`)
) ENGINE=InnoDB AUTO_INCREMENT=374 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_perm`
--

LOCK TABLES `sys_role_perm` WRITE;
/*!40000 ALTER TABLE `sys_role_perm` DISABLE KEYS */;
INSERT INTO `sys_role_perm` VALUES (290,2,'dept.list',1,'2023-08-26 12:13:37.326'),(332,1,'user.role.list',1,'2023-08-26 23:28:14.552'),(333,1,'user.role.bind',1,'2023-08-26 23:28:14.556'),(334,1,'user.perm.list',1,'2023-08-26 23:28:14.559'),(335,1,'user.add',1,'2023-08-26 23:28:14.562'),(336,1,'user.delete',1,'2023-08-26 23:28:14.564'),(337,1,'user.list',1,'2023-08-26 23:28:14.566'),(338,1,'user.entity.list',1,'2023-08-26 23:28:14.568'),(339,1,'user.page',1,'2023-08-26 23:28:14.570'),(340,1,'user.update',1,'2023-08-26 23:28:14.571'),(341,1,'user.password.change',1,'2023-08-26 23:28:14.573'),(342,1,'user.dept.list',1,'2023-08-26 23:28:14.580'),(343,1,'role.add',1,'2023-08-26 23:28:14.582'),(344,1,'role.delete',1,'2023-08-26 23:28:14.583'),(345,1,'role.list',1,'2023-08-26 23:28:14.584'),(346,1,'role.entity.list',1,'2023-08-26 23:28:14.585'),(347,1,'role.page',1,'2023-08-26 23:28:14.586'),(348,1,'role.update',1,'2023-08-26 23:28:14.588'),(349,1,'role.menu.list',1,'2023-08-26 23:28:14.591'),(350,1,'role.menu.bind',1,'2023-08-26 23:28:14.593'),(351,1,'role.perm.list',1,'2023-08-26 23:28:14.596'),(352,1,'role.perm.bind',1,'2023-08-26 23:28:14.597'),(353,1,'menu.add',1,'2023-08-26 23:28:14.599'),(354,1,'menu.delete',1,'2023-08-26 23:28:14.600'),(355,1,'menu.list',1,'2023-08-26 23:28:14.601'),(356,1,'menu.update',1,'2023-08-26 23:28:14.602'),(357,1,'menu.entity.list',1,'2023-08-26 23:28:14.603'),(358,1,'perm.add',1,'2023-08-26 23:28:14.604'),(359,1,'perm.delete',1,'2023-08-26 23:28:14.605'),(360,1,'perm.list',1,'2023-08-26 23:28:14.607'),(361,1,'perm.entity.list',1,'2023-08-26 23:28:14.609'),(362,1,'perm.page',1,'2023-08-26 23:28:14.611'),(363,1,'perm.update',1,'2023-08-26 23:28:14.613'),(364,1,'data.import.multiple',1,'2023-08-26 23:28:14.614'),(365,1,'data.page',1,'2023-08-26 23:28:14.616'),(366,1,'data.export',1,'2023-08-26 23:28:14.617'),(367,1,'data.delete',1,'2023-08-26 23:28:14.618'),(368,1,'data.truncate',1,'2023-08-26 23:28:14.619'),(369,1,'user.login.log.page',1,'2023-08-26 23:28:14.620'),(370,1,'dept.add',1,'2023-08-26 23:28:14.621'),(371,1,'dept.update',1,'2023-08-26 23:28:14.622'),(372,1,'dept.list',1,'2023-08-26 23:28:14.625'),(373,1,'dept.delete',1,'2023-08-26 23:28:14.627');
/*!40000 ALTER TABLE `sys_role_perm` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '用户名',
  `nickname` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户姓名',
  `nickname_pinyin` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '昵称拼音',
  `password` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '用户密码',
  `dept_id` bigint unsigned DEFAULT NULL COMMENT '用户所属部门',
  `phone` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '手机号',
  `avatar` varchar(1024) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '头像地址',
  `register_ip` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '注册IP',
  `parent_id` bigint unsigned DEFAULT NULL,
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '用户状态',
  `email` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '邮箱',
  `update_by` bigint unsigned DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(3) DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime(3) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sys_user_username_uindex` (`username`),
  UNIQUE KEY `sys_user_email_uindex` (`email`),
  UNIQUE KEY `sys_user_phone_uindex` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,'admin','韩信','han xin,shen','a23cece66c3ceed73100d9d72eea04cc',NULL,NULL,'https://q4.qlogo.cn/g?b=qq&nk=942664114&s=0','127.0.0.1',NULL,1,NULL,1,'2023-08-16 23:51:27.459','2023-08-13 13:55:02.779'),(5,'admin2','赵云','zhao yun','f23e1932edce35277180eaf5ba843d27',NULL,NULL,'','127.0.0.1',NULL,1,NULL,1,'2023-08-15 20:01:45.474','2023-08-15 11:59:03.412');
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_dept`
--

DROP TABLE IF EXISTS `sys_user_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_dept` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint unsigned NOT NULL,
  `department_id` bigint unsigned NOT NULL,
  `type` int NOT NULL COMMENT '1: 主部门; 2: 兼职部门',
  `create_by` bigint unsigned NOT NULL,
  `create_time` datetime(3) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unidx_uid_dept_id` (`user_id`,`department_id`),
  KEY `idx_dept_id` (`department_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='兼职部门';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_dept`
--

LOCK TABLES `sys_user_dept` WRITE;
/*!40000 ALTER TABLE `sys_user_dept` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_user_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint unsigned NOT NULL COMMENT '用户ID',
  `role_id` bigint unsigned NOT NULL COMMENT '角色ID',
  `create_by` bigint unsigned NOT NULL COMMENT '创建人',
  `create_time` datetime(3) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` VALUES (1,1,1,1,'2023-08-12 20:17:43.000'),(2,1,2,1,'2023-08-12 20:17:43.000'),(6,5,2,1,'2023-08-27 08:28:12.178');
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_login_log`
--

DROP TABLE IF EXISTS `user_login_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_login_log` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(64) COLLATE utf8mb4_bin NOT NULL,
  `nickname` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL,
  `token` varchar(128) COLLATE utf8mb4_bin NOT NULL,
  `login_type` int NOT NULL,
  `login_ip` varchar(32) COLLATE utf8mb4_bin NOT NULL,
  `create_by` bigint unsigned NOT NULL,
  `create_time` datetime(3) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_login_log`
--

--
-- Table structure for table `user_operate_log`
--

DROP TABLE IF EXISTS `user_operate_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_operate_log` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `request_id` varchar(32) COLLATE utf8mb4_bin NOT NULL,
  `object_type` varchar(36) COLLATE utf8mb4_bin NOT NULL COMMENT '操作对象实体',
  `remark` varchar(4096) COLLATE utf8mb4_bin NOT NULL COMMENT '操作描述',
  `method` varchar(8) COLLATE utf8mb4_bin NOT NULL COMMENT '请求方式',
  `url` varchar(256) COLLATE utf8mb4_bin NOT NULL COMMENT 'qi',
  `param` text COLLATE utf8mb4_bin COMMENT '请求参数',
  `duration` int NOT NULL,
  `request_ip` varchar(32) COLLATE utf8mb4_bin NOT NULL,
  `error_message` text COLLATE utf8mb4_bin COMMENT '错误信息',
  `create_by` bigint unsigned DEFAULT NULL,
  `create_time` datetime(3) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1037 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-08-29 19:29:28
