package com.imooc.pojo.vo;

import lombok.Data;

@Data
public class SubCategoryVO {
    private Integer subId;
    private String subName;
    private Integer subType;
    private Integer subFatherId;
}
