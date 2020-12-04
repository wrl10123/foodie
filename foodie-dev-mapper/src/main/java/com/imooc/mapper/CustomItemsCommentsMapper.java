package com.imooc.mapper;

import com.imooc.pojo.vo.MyCommentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CustomItemsCommentsMapper {

    /**
     * 查询我的评价
     *
     * @param map
     * @return
     */
    List<MyCommentVO> queryMyComments(@Param("paramMap") Map<String, Object> map);

}