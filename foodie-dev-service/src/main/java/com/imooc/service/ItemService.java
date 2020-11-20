package com.imooc.service;

import com.imooc.pojo.Items;
import com.imooc.pojo.ItemsImg;
import com.imooc.pojo.ItemsParam;
import com.imooc.pojo.ItemsSpec;
import com.imooc.pojo.vo.CommentLevelCountsVO;
import com.imooc.utils.PagedGridResult;

import java.util.List;

public interface ItemService {

    /**
     * 根据商品id查询详情
     *
     * @param itemId
     * @return
     */
    Items queryItemById(String itemId);

    /**
     * 根据商品id查询图片列表
     *
     * @param itemId
     * @return
     */
    List<ItemsImg> queryItemImgList(String itemId);

    /**
     * 根据商品id 查询商品规格
     *
     * @param itemId
     * @return
     */
    List<ItemsSpec> queryItemSpecList(String itemId);

    /**
     * 根据商品id 查询商品属性
     *
     * @param itemId
     * @return
     */
    ItemsParam queryItemParam(String itemId);

    /**
     * 根据商品id查询商品的评价等级
     *
     * @param itemId
     */
    CommentLevelCountsVO queryCommentCounts(String itemId);

    /**
     * 根据商品id查询商品的评价（分页）
     *
     * @param itemId
     * @param level
     * @param page     第几页
     * @param pageSize 每页显示的条数
     * @return
     */
    PagedGridResult queryPagedComments(
            String itemId, Integer level, Integer page, Integer pageSize);

    /**
     *
     * @param keywords 搜索的关键字
     * @param sort 排序条件
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult searchItems(
            String keywords, String sort, Integer page, Integer pageSize);

    /**
     *
     * @param catId 分类id
     * @param sort 排序条件
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult searchItemsByCat(
            Integer catId, String sort, Integer page, Integer pageSize);
}
