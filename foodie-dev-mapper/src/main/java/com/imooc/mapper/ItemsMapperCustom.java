package com.imooc.mapper;

import com.imooc.pojo.vo.ItemCommentVO;
import com.imooc.pojo.vo.SearchItemsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsMapperCustom {

    List<ItemCommentVO> queryItemComments(@Param("paramMap") Map<String, Object> map);

    /**
     * 根据关键字搜索
     * @param map
     * @return
     */
    List<SearchItemsVO> searchItems(@Param("paramMap") Map<String, Object> map);

    /**
     * 根据分类id
     *
     * @param map
     * @return
     */
    List<SearchItemsVO> searchItemsByCat(@Param("paramMap") Map<String, Object> map);

}