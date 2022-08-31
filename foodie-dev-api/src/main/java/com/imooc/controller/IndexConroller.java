package com.imooc.controller;

import com.imooc.enums.YesOrNo;
import com.imooc.pojo.Carousel;
import com.imooc.pojo.Category;
import com.imooc.pojo.vo.CategoryVO;
import com.imooc.pojo.vo.NewItemsVO;
import com.imooc.service.CarouselService;
import com.imooc.service.CategoryService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.utils.RedisOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api(value = "首页",tags = {"首页展示的相关接口"})
@RestController
@RequestMapping("index")
public class IndexConroller {
    @Autowired
    private CarouselService carouselService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisOperator redisOperator;
    @ApiOperation(value = "获取首页轮播图列表",notes = "获取首页轮播图列表",httpMethod = "GET")
    @GetMapping("/carousel")
    public IMOOCJSONResult carousel() {
        String carouselStr = redisOperator.get("carousel");
        List<Carousel> list = new ArrayList<>();
        if(StringUtils.isBlank(carouselStr)){
            list = carouselService.queryAll(YesOrNo.Yes.type);
            redisOperator.set("carousel", JsonUtils.objectToJson(list));
        }
        list= JsonUtils.jsonToList(carouselStr,Carousel.class);
        return IMOOCJSONResult.ok(list);
    }
    /**
     * 首页分类展示需求：
     * 1.第一次刷新主页查询大分类，渲染展示到首页
     * 2.如果鼠标移到大分类，则加载其子类的内容，如果已经存在子分类，
     * 则不需要加载
     */
    @ApiOperation(value = "获取商品分类(一级分类)",notes = "获取商品分类(一级分类)",httpMethod = "GET")
    @GetMapping("/cats")
    public IMOOCJSONResult queryAllRootLevelCat() {
        String cats = redisOperator.get("cats");
        List<Category> list = new ArrayList<>();
        if(StringUtils.isBlank(cats)){
            list = categoryService.queryAllRootLevelCat();
            redisOperator.set("cats",JsonUtils.objectToJson(list));
        }
        list= JsonUtils.jsonToList(cats, Category.class);
        return IMOOCJSONResult.ok(list);
    }

    @ApiOperation(value = "获取商品子分类",notes = "获取商品子分类",httpMethod = "GET")
    @GetMapping("/subCat/{rootCatId}")
    public IMOOCJSONResult subCat(
            @ApiParam(name = "rootCatId",value = "一级分类id",required = true)
            @PathVariable Integer rootCatId){
        if(rootCatId==null){
            return IMOOCJSONResult.errorMsg("分类不存在");
        }
        //这里使用了hash数据类型，这是自己的想法，如果用string类型，
        //那么第一个一级分类下二级分类的缓存，其他分类下会直接使用，这就产生了一个bug
        String subCat = redisOperator.hget(rootCatId.toString(),"subCat");
        List<CategoryVO> list = new ArrayList<>();
        if(StringUtils.isBlank(subCat)) {
            list = categoryService.getSubCatlist(rootCatId);
            redisOperator.hset(rootCatId.toString(),"subCat",JsonUtils.objectToJson(list));
        }
        list= JsonUtils.jsonToList(subCat, CategoryVO.class);
        return IMOOCJSONResult.ok(list);
    }

    @ApiOperation(value = "查询每个一级分类下最新商品数据",notes = "查询每个一级分类下最新商品数据",httpMethod = "GET")
    @GetMapping("/sixNewItems/{rootCatId}")
    public IMOOCJSONResult sixNewItems(
            @ApiParam(name = "rootCatId",value = "一级分类id",required = true)
            @PathVariable Integer rootCatId){
        if(rootCatId==null){
            return IMOOCJSONResult.errorMsg("分类不存在");
        }
        List<NewItemsVO> list = categoryService.getSixNewItemsLazy(rootCatId);
        return IMOOCJSONResult.ok(list);
    }

}

