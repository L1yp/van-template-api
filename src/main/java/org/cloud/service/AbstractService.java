package org.cloud.service;

import io.mybatis.mapper.BaseMapper;
import io.mybatis.mapper.example.ExampleWrapper;
import io.mybatis.mapper.fn.Fn.Fns;
import lombok.extern.slf4j.Slf4j;
import org.cloud.exception.BusinessException;
import org.cloud.model.AbstractModel;
import org.cloud.model.AbstractUpdateDTO;
import org.cloud.model.Converter;
import org.cloud.model.common.PageDTO;
import org.cloud.model.common.PageData;
import org.cloud.mybatis.Mapper;
import org.cloud.mybatis.MapperUtil;
import org.cloud.util.MessageUtils;
import org.cloud.util.SQLTool;
import org.springframework.aop.framework.AopContext;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Slf4j
public abstract class AbstractService<DO extends AbstractModel<DTO> & Converter<DTO>, DTO extends Converter<DO>, QueryDTO extends PageDTO> {

    protected Mapper<DO, String> baseMapper;

    public AbstractService() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType parameterizedType) {
            Type rawType = parameterizedType.getRawType();
            if (rawType.equals(AbstractService.class)) {
                Type modelType = parameterizedType.getActualTypeArguments()[0];
                baseMapper = MapperUtil.getMapper(modelType);
            }
        }
    }


    @SuppressWarnings("unchecked")
    protected AbstractService<DO, DTO, QueryDTO> getProxy() {
        return (AbstractService<DO, DTO, QueryDTO>) AopContext.currentProxy();
    }

    /**
     * 分页查询前的参数设置方法
     * @param wrapper 查询容器
     * @param pageDTO 参数
     */
    protected void preparePageQuery(ExampleWrapper<DO, String> wrapper, QueryDTO pageDTO) {

    }

    public PageData<DTO> page(QueryDTO param) {
        ExampleWrapper<DO, String> wrapper = baseMapper.wrapper();
        preparePageQuery(wrapper, param);
        long count = wrapper.count();
        if (count == 0) {
            return PageData.empty(param);
        }
        List<DO> list = wrapper.endSql("LIMIT " + param.getOffset() + "," + param.getPageSize()).list();
        List<DTO> data = list.stream().map(Converter::convert).toList();
        return PageData.ok(param, count, data);
    }



    /**
     * 查询列表前设置查询条件
     * @param wrapper 查询容器
     * @param param 参数
     */
    protected void prepareQuery(ExampleWrapper<DO, String> wrapper, QueryDTO param) {

    }

    public List<DTO> list(QueryDTO param) {
        ExampleWrapper<DO, String> wrapper = baseMapper.wrapper();
        prepareQuery(wrapper, param);
        List<DO> list = wrapper.list().stream().toList();
        return list.stream().map(Converter::convert).toList();
    }

    /**
     * 更新和新增对参数进行处理
     * @param param 参数
     * @param consumer 消费者
     * @return DO
     */
    protected DO prepare(Converter<DO> param, Consumer<DO> consumer) {
        DO modelDO = param.convert();
        if (consumer != null) {
            consumer.accept(modelDO);
        }
        return modelDO;
    }

    protected void prepareAdd(DO model) {

    }

    @Transactional
    public void insert(Converter<DO> param, Consumer<DO> consumer) {
        DO modelDO = prepare(param, consumer);
        getProxy().prepareAdd(modelDO);
        baseMapper.insert(modelDO);
        getProxy().afterAdd(modelDO);
    }

    @Transactional
    public void insertSelective(Converter<DO> param, Consumer<DO> consumer) {
        DO modelDO = prepare(param, consumer);
        getProxy().prepareAdd(modelDO);
        baseMapper.insertSelective(modelDO);
        getProxy().afterAdd(modelDO);
    }

    @Transactional
    public void insertSelective(Converter<DO> param) {
        getProxy().insertSelective(param, null);
    }

    @Transactional
    public void insert(Converter<DO> param) {
        insert(param, null);
    }


    @Transactional
    public void insert(DO modelDO) {
        getProxy().prepareAdd(modelDO);
        baseMapper.insert(modelDO);
        getProxy().afterAdd(modelDO);
    }

    @Transactional
    public void insertSelective(DO modelDO) {
        getProxy().prepareAdd(modelDO);
        baseMapper.insertSelective(modelDO);
        getProxy().afterAdd(modelDO);
    }

    protected void afterAdd(DO model) { }

    /**
     * 默认无缓存，若要缓存，则重写此方法加上Cacheable注解
     * @param id 主键ID
     */
    public DO getById(String id) {
        Optional<DO> optional = baseMapper.selectByPrimaryKey(id);
        return optional.orElse(null);
    }

    /**
     * 检查id必须存在，否则报错
     * @param id id
     */
    public DO assertIdPresentAndGetDO(String id) {
        DO model = getProxy().getById(id);
        if (model == null) {
            throw new BusinessException(404, MessageUtils.getMessage("id.not-found", id));
        }
        return model;
    }

    protected void prepareUpdate(DO paramDO, DO modelDO) {

    }

    @Transactional
    public void update(AbstractUpdateDTO<DO> param, Consumer<DO> consumer) {
        DO paramDO = prepare(param, consumer);
        DO modelDO = assertIdPresentAndGetDO(param.getId());

        getProxy().prepareUpdate(paramDO, modelDO);
        baseMapper.updateByPrimaryKey(paramDO);
        getProxy().afterUpdate(paramDO, modelDO);
    }

    @Transactional
    public void update(AbstractUpdateDTO<DO> param) {
        getProxy().update(param, null);
    }


    @Transactional
    public void updateByPrimaryKeySelective(AbstractUpdateDTO<DO> param, Consumer<DO> consumer) {
        DO paramDO = prepare(param, consumer);
        DO modelDO = assertIdPresentAndGetDO(param.getId());

        getProxy().prepareUpdate(paramDO, modelDO);
        baseMapper.updateByPrimaryKeySelective(paramDO);
        getProxy().afterUpdate(paramDO, modelDO);
    }

    @Transactional
    public void updateByPrimaryKeySelective(AbstractUpdateDTO<DO> param) {
        getProxy().updateByPrimaryKeySelective(param, null);
    }

    @Transactional
    public void updateByPrimaryKeySelectiveWithForceFields(AbstractUpdateDTO<DO> param, Fns<DO> fns) {
        DO paramDO = prepare(param, null);
        DO modelDO = assertIdPresentAndGetDO(param.getId());

        getProxy().prepareUpdate(paramDO, modelDO);
        baseMapper.updateByPrimaryKeySelectiveWithForceFields(paramDO, fns);
        getProxy().afterUpdate(paramDO, modelDO);
    }

    @Transactional
    public void updateByPrimaryKeySelectiveWithForceFields(DO paramDO, Fns<DO> fns) {
        DO modelDO = assertIdPresentAndGetDO(paramDO.getId());

        getProxy().prepareUpdate(paramDO, modelDO);
        baseMapper.updateByPrimaryKeySelectiveWithForceFields(paramDO, fns);
        getProxy().afterUpdate(paramDO, modelDO);
    }

    private final Consumer<DO> EMPTY = model -> {};

    @Transactional
    public void updateByPrimaryKeySelective(DO model) {
        DO modelDO = assertIdPresentAndGetDO(model.getId());
        getProxy().prepareUpdate(model, modelDO);
        baseMapper.updateByPrimaryKeySelective(model);
        getProxy().afterUpdate(model, modelDO);
    }

    protected void afterUpdate(DO paramDO, DO modelDO) {

    }


    /**
     * 删除前执行的校验
     * @param model DTO
     */
    protected void prepareDelete(DO model) { }

    @Transactional
    public void deleteById(String id) {
        DO dto = assertIdPresentAndGetDO(id);

        getProxy().prepareDelete(dto);
        baseMapper.deleteByPrimaryKey(id);
        getProxy().afterDelete(dto);
    }

    /**
     *
     * @param modelDO
     */
    protected void afterDelete(DO modelDO) { }

    public BaseMapper<DO, String> getBaseMapper() {
        return baseMapper;
    }

    protected String escapeLikeSQL(String key) {
        return SQLTool.escapeSql(key);
    }


}
