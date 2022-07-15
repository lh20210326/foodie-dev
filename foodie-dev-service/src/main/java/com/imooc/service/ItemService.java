package com.imooc.service;

import com.imooc.pojo.Items;
import com.imooc.pojo.ItemsImg;
import com.imooc.pojo.ItemsParam;
import com.imooc.pojo.ItemsSpec;
import com.imooc.pojo.vo.CommentLevelCountsVO;
import com.imooc.pojo.vo.ItemCommentVO;
import com.imooc.pojo.vo.ShopcartVO;
import com.imooc.utils.PagedGridResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemService {
    /**
     * 根据商品id查询商品详情
     * @param itemId
     * @return
     */
    public Items queryItemById(String itemId);

    /**
     * 根据商品id查询商品图片列表
     * @param itemId
     * @return
     */
    public List<ItemsImg> queryItemImgList(String itemId);

    /**
     * 根据商品id查询商品规格
     * @param itemId
     * @return
     */
    public List<ItemsSpec> queryItemSpecList(String itemId);
    /**
     * 根据商品id查询商品属性
     * @param itemId
     * @return
     */
    public ItemsParam queryItemParam(String itemId);
    /**
     * 根据商品id查询所有的商品评价
     * @param itemId
     * @return
     */
    public CommentLevelCountsVO queryCommentCounts(String itemId);

    /**
     * 根据商品id查询商品评价
     * @param itemId
     * @param level
     * @return
     */
    public PagedGridResult queryPagedComment(String itemId, Integer level, Integer page, Integer pageSize);

    /**
     * 商品搜索列表
     * @param keywords
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    public PagedGridResult searchItems(String keywords,String sort,Integer page, Integer pageSize);
    /**
     * 根据分类id搜索商品列表
     * @param
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    public PagedGridResult searchItems(Integer cartId,String sort,Integer page, Integer pageSize);

    /**
     * 根据规格ids查询最新商品购物车的数据（用于刷新购物车）
     * @param specIds
     * @return
     */
    public List<ShopcartVO> queryItemsBySpecIds(String specIds);
}
