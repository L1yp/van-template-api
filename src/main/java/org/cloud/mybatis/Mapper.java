package org.cloud.mybatis;

import io.mybatis.mapper.BaseMapper;
import io.mybatis.mapper.list.ListMapper;

import java.io.Serializable;

public interface Mapper<T, I extends Serializable> extends BaseMapper<T, I>, ListMapper<T> {

}
