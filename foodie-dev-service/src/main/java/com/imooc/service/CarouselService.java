package com.imooc.service;

import com.imooc.pojo.Carousel;

import java.util.List;

public interface CarouselService {

    /**
     * 查询轮播图
     *
     * @param isShow
     * @return
     */
    List<Carousel> queryAll(Integer isShow);

}
