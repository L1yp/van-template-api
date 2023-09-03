package org.cloud.model.common;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class PageData<T> {

    private int pageIdx;
    private int pageSize;
    private long total;
    private List<T> rows;

    private Object additional;

    public PageData(){}

    public PageData(PageDTO param) {
        pageIdx = param.getPageIdx();
        pageSize = param.getPageSize();
    }

    public PageData(PageDTO param, long total, List<T> rows) {
        pageIdx = param.getPageIdx();
        pageSize = param.getPageSize();
        this.total = total;
        this.rows = rows;
    }


    public PageData(int pageIdx, int pageSize) {
        this.pageIdx = pageIdx;
        this.pageSize = pageSize;
    }

    public static <T> PageData<T> empty(PageDTO param) {
        PageData<T> pageData = new PageData<>(param);
        pageData.setTotal(0);
        pageData.setRows(Collections.emptyList());
        return pageData;
    }

    public static <T> PageData<T> empty(int pageIdx, int pageSize) {
        PageData<T> pageData = new PageData<>(pageIdx, pageSize);
        pageData.setTotal(0);
        pageData.setRows(Collections.emptyList());
        return pageData;
    }

    public static <T> PageData<T> ok(int pageIdx, int pagesize, int total, List<T> data) {
        PageData<T> pageData = new PageData<>(pageIdx, pagesize);
        pageData.setTotal(total);
        pageData.setRows(data);
        return pageData;
    }

    public static <T> PageData<T> ok(PageDTO pageDTO, long total, List<T> data) {
        PageData<T> pageData = new PageData<>(pageDTO);
        pageData.setTotal((int) total);
        pageData.setRows(data);
        return pageData;
    }

}
