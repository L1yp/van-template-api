alter table sys_user_dept
    drop key unidx_uid_dept_id;

alter table sys_user_dept
    add constraint unidx_uid_dept_id
        unique (user_id, department_id, type);

