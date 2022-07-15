package com.imooc.service;

import com.imooc.pojo.Category;
import com.imooc.pojo.vo.CategoryVO;
import com.imooc.pojo.vo.NewItemsVO;
import io.swagger.models.auth.In;

import java.util.List;

public interface CategoryService {
    /**
     * 查询所有一级分类
     * @return
     */
    public List<Category> queryAllRootLevelCat();
    /**
     *根据一级分类id查询分类信息
     */
    public List<CategoryVO> getSubCatlist(Integer rootId);

    /**
     * 查询每个一级分类下对应6条最新商品
     * @param rootCartId
     * @return
     */
    public List<NewItemsVO> getSixNewItemsLazy(Integer rootCartId);
}
