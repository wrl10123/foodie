package com.imooc.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 * 二级分类
 */
@Data
public class NewItemsVO {

    private Integer rootCatId;
    private String cootCatName;
    private String slogan;
    private String catImage;
    private String bgColor;
    private List<SimpleItemVO> simpleItemList;
}
