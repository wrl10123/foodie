package com.imooc.controller.center;

import com.imooc.controller.BaseController;
import com.imooc.enums.StatusEnum;
import com.imooc.pojo.OrderItems;
import com.imooc.pojo.Orders;
import com.imooc.pojo.bo.center.OrderItemsCommentsBO;
import com.imooc.service.center.MyCommentsService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Api(value = "用户中心评价", tags = {"用户评价相关的api"})
@RestController
@RequestMapping("mycomments")
public class MyCommentsController extends BaseController {

    @Autowired
    private MyCommentsService myCommentsService;

    @ApiOperation(value = "查询订单列表", httpMethod = "POST")
    @PostMapping("pending")
    public IMOOCJSONResult pending(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId) {
        //判断用户和订单是否关联
        IMOOCJSONResult checkOrder = checkUserOrder(userId, orderId);
        if (checkOrder.getStatus() != HttpStatus.OK.value()) {
            return checkOrder;
        }
        Orders order = (Orders) checkOrder.getData();
        if (StatusEnum.YES.type.equals(order.getIsComment())) {
            return IMOOCJSONResult.errorMsg("该订单已经评价");
        }

        List<OrderItems> orderItems = myCommentsService.queryPendingComment(orderId);
        return IMOOCJSONResult.ok(orderItems);
    }

    @ApiOperation(value = "保存评论列表", httpMethod = "POST")
    @PostMapping("saveList")
    public IMOOCJSONResult saveList(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId,
            @RequestBody List<OrderItemsCommentsBO> commentList) {
        //判断用户和订单是否关联
        IMOOCJSONResult checkOrder = checkUserOrder(userId, orderId);
        if (checkOrder.getStatus() != HttpStatus.OK.value()) {
            return checkOrder;
        }
        if (CollectionUtils.isEmpty(commentList)) {
            return IMOOCJSONResult.errorMsg("评论内容不能为空");
        }
        myCommentsService.saveComments(userId, orderId, commentList);
        return checkOrder;
    }


    @ApiOperation(value = "查询订单列表", httpMethod = "POST")
    @PostMapping("query")
    public IMOOCJSONResult query(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "page", value = "当前页数", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "每页条数", required = false)
            @RequestParam Integer pageSize) {
        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = COMMENT_PAGE_SIZE;
        }
        PagedGridResult result =
                myCommentsService.queryMyComments(userId, page, pageSize);
        return IMOOCJSONResult.ok(result);
    }
}
