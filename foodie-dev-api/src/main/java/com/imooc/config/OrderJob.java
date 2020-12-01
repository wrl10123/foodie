package com.imooc.config;

import com.imooc.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderJob {

    @Autowired
    private OrderService orderService;

    /**
     * 存在的弊端
     * 1. 会有时间差
     * 2. 不支持集群
     * 3. 会对数据库全表搜索，及其影响性能
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void autoCloseOrder() {
        log.info("定时关单");
        orderService.closeOrder();

    }

}
