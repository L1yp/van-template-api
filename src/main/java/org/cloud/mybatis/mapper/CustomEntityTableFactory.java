package org.cloud.mybatis.mapper;

import io.mybatis.config.ConfigHelper;
import io.mybatis.provider.Entity;
import io.mybatis.provider.EntityTable;
import io.mybatis.provider.EntityTableFactory;
import io.mybatis.provider.Style;

public class CustomEntityTableFactory implements EntityTableFactory {
    @Override
    public EntityTable createEntityTable(Class<?> entityClass, Chain chain) {
        if (entityClass.isAnnotationPresent(Entity.Table.class)) {
            Entity.Table table = entityClass.getAnnotation(Entity.Table.class);
            EntityTable entityTable = CustomEntityTable.of(entityClass)
                    .table(table.value().isEmpty() ? Style.getStyle(table.style()).tableName(entityClass) : table.value())
                    .catalog(table.catalog().isEmpty() ? ConfigHelper.getStr("mybatis.provider.catalog") : table.catalog())
                    .schema(table.schema().isEmpty() ? ConfigHelper.getStr("mybatis.provider.schema") : table.schema())
                    .style(table.style())
                    .resultMap(table.resultMap())
                    .autoResultMap(table.autoResultMap())
                    .excludeSuperClasses(table.excludeSuperClasses())
                    .excludeFieldTypes(table.excludeFieldTypes())
                    .excludeFields(table.excludeFields());
            for (Entity.Prop prop : table.props()) {
                entityTable.setProp(prop);
            }
            return entityTable;
        }
        return null;
    }

    @Override
    public int getOrder() {
        return 10000;
    }
}
