package com.imooc.mapper;

import com.imooc.pojo.vo.CategoryVO;
import com.imooc.pojo.vo.SimpleItemVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CustomCategoryMapper {

    List<CategoryVO> getSubCatList(Integer rootCatId);

    List<SimpleItemVO> getSixNewItemsLazy(@Param("paramMap") Map<String, Object> map);

}