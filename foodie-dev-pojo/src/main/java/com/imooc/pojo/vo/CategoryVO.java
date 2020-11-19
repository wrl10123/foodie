package com.imooc.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 * 二级分类
 */
@Data
public class CategoryVO {

    private Integer id;
    private String name;
    private Integer type;
    private Integer fatherId;
    private List<SubCategoryVO> subCatList;
}
