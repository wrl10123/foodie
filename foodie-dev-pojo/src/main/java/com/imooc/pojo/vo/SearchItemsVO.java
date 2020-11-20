package com.imooc.pojo.vo;

import lombok.Data;

/**
 * 展示商品搜索结果
 */
@Data
public class SearchItemsVO {

    private String itemId;
    private String itemName;
    private Integer sellCounts;
    private String imgUrl;
    private Integer price;

}
