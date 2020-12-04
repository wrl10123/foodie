package com.imooc.service.center;

import com.imooc.pojo.OrderItems;
import com.imooc.pojo.bo.center.OrderItemsCommentsBO;
import com.imooc.pojo.vo.MyCommentVO;
import com.imooc.utils.PagedGridResult;

import java.util.List;

public interface MyCommentsService {

    /**
     * 根据订单id查询关联的商品
     *
     * @param orderId
     * @return
     */
    List<OrderItems> queryPendingComment(String orderId);

    /**
     * 保存用户评论
     *
     * @param userId
     * @param orderId
     * @param commentList
     */
    void saveComments(String userId, String orderId, List<OrderItemsCommentsBO> commentList);

    /**
     * 查询我的评价列表
     *
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult queryMyComments(String userId, Integer page, Integer pageSize);


}
