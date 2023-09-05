package org.cloud.util;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class SpringContext implements ApplicationListener<ApplicationStartedEvent> {

    private static ApplicationContext ctx;


    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        SpringContext.ctx = event.getApplicationContext();
    }

    public static <T> T getBean(Class<T> clazz) {
        String[] beanNamesForType = ctx.getBeanNamesForType(clazz);
        if (beanNamesForType.length == 0) {
            return null;
        }
        return ctx.getBean(clazz);
    }

}
