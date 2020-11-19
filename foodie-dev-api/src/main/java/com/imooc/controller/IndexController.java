package com.imooc.controller;

import com.imooc.enums.StatusEnum;
import com.imooc.pojo.Carousel;
import com.imooc.pojo.Category;
import com.imooc.pojo.vo.CategoryVO;
import com.imooc.pojo.vo.SimpleItemVO;
import com.imooc.service.CarouselService;
import com.imooc.service.CategoryService;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 首页
 */
@Api(value = "首页", tags = {"首页展示的相关接口"})
@RestController
@RequestMapping("index")
public class IndexController {

    @Autowired
    private CarouselService carouselService;
    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value = "获取首页轮播图列表", httpMethod = "GET")
    @GetMapping("carousel")
    public IMOOCJSONResult carousel() {
        List<Carousel> carousels = carouselService.queryAll(StatusEnum.YES.type);
        return IMOOCJSONResult.ok(carousels);
    }

    @ApiOperation(value = "获取商品分类(一级分类)", httpMethod = "GET")
    @GetMapping("cats")
    public IMOOCJSONResult cats() {
        List<Category> categories = categoryService.queryAllRootLevelCat();
        return IMOOCJSONResult.ok(categories);
    }

    @ApiOperation(value = "获取商品分类", httpMethod = "GET")
    @GetMapping("subCat/{rootCatId}")
    public IMOOCJSONResult subCat(
            @ApiParam(name = "rootCatId", value = "一级分类id", required = true)
            @PathVariable Integer rootCatId) {
        if (rootCatId == null) {
            return IMOOCJSONResult.errorMsg("");
        }
        List<CategoryVO> subCatList = categoryService.getSubCatList(rootCatId);
        return IMOOCJSONResult.ok(subCatList);
    }

    @ApiOperation(value = "查询每个一级分类下面的最6条商品", httpMethod = "GET")
    @GetMapping("sixNewItems/{rootCatId}")
    public IMOOCJSONResult sixNewItems(
            @ApiParam(name = "rootCatId", value = "一级分类id", required = true)
            @PathVariable Integer rootCatId) {
        if (rootCatId == null) {
            return IMOOCJSONResult.errorMsg("");
        }
        List<SimpleItemVO> sixNewItemsLazy = categoryService.getSixNewItemsLazy(rootCatId);
        return IMOOCJSONResult.ok(sixNewItemsLazy);
    }

}
