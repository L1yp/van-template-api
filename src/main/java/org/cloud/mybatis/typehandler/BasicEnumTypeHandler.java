package org.cloud.mybatis.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.cloud.model.enums.base.BasicEnum;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class BasicEnumTypeHandler<E extends BasicEnum> extends BaseTypeHandler<E> {

    /**
     * number类型统一转为int
     */
    private final Map<Object, E> valMap = new HashMap<>();

    public BasicEnumTypeHandler(Class<E> type) {
        if (type == null || !type.isEnum()) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }

        E[] values = type.getEnumConstants();
        for (E value : values) {
            int key = value.getValue();
            valMap.put(key, value);
        }

    }



    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getValue());
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int value = rs.getInt(columnName);
        return valMap.get(value);
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int value = rs.getInt(columnIndex);
        return valMap.get(value);
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int value = cs.getInt(columnIndex);
        return valMap.get(value);
    }
}
