-- 操作日志新增列: 浏览器UA
alter table user_operate_log add user_agent varchar(1024) null after request_ip;

