package com.imooc.service;

import com.github.pagehelper.PageInfo;
import com.imooc.utils.PagedGridResult;

import java.util.List;

public class BaseService {

    /**
     * 分页详情
     *
     * @param voList
     * @param page
     * @return
     */
    public PagedGridResult setterPageGrid(List<?> voList, Integer page) {
        PageInfo<?> pageInfo = new PageInfo<>(voList);
        PagedGridResult grid = new PagedGridResult();
        grid.setPage(page);
        grid.setRows(voList);
        grid.setTotal(pageInfo.getPages());
        grid.setRecords(pageInfo.getTotal());
        return grid;
    }
}
