package org.cloud.controller.intercepter.task;

import org.cloud.util.SpringContext;
import org.cloud.web.mapper.system.UserOperateLogMapper;
import org.cloud.web.model.DO.system.UserOperateLogDO;

public class OperateLogStorageTask implements Runnable {

    private final UserOperateLogDO model;

    public OperateLogStorageTask(UserOperateLogDO model) {
        this.model = model;
    }

    @Override
    public void run() {
        UserOperateLogMapper operateLogMapper = SpringContext.getBean(UserOperateLogMapper.class);
        operateLogMapper.insert(model);
    }
}
