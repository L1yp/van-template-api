-- 新增用户请求的Token字段
alter table user_operate_log add token varchar(64) null after param;

