package org.cloud.mybatis.typehandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.cloud.mybatis.MybatisConfig;
import org.cloud.mybatis.interceptor.ParameterizeInterceptor;
import org.cloud.util.JSON;

import java.lang.reflect.Type;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 通用的json数据类型，自动映射为字段的对象类型
 * @see org.cloud.mybatis.mapper.CustomSqlSourceCustomize 处理BaseMapper的内置方法参数
 * @see MybatisConfig#resetTypeHandlerInResultMapping 处理mybatis中所有resultMapping的jacksonHandler通用类型
 * @see ParameterizeInterceptor 处理非BaseMapper的ParameterMapping
 */
@Getter
public class JacksonTypeHandler extends BaseTypeHandler<Object> {

    private Type fieldType;

    public JacksonTypeHandler() {}

    public JacksonTypeHandler(Type fieldType) {
        this.fieldType = fieldType;
    }

    public void setFieldType(Type fieldType) {
        this.fieldType = fieldType;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        try {
            String val = JSON.mapper.writeValueAsString(parameter);
            ps.setString(i, val);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Object parse(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        JavaType javaType = JSON.mapper.constructType(fieldType);
        try {
            return JSON.mapper.readValue(str, javaType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parse(rs.getString(columnName));
    }

    @Override
    public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parse(rs.getString(columnIndex));
    }

    @Override
    public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parse(cs.getString(columnIndex));
    }
}