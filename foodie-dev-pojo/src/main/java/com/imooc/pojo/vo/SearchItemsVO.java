package com.imooc.pojo.vo;

/**
 * 用于展示商品搜索列表结果的VO
 */
public class SearchItemsVO {
//    i.id AS itemId,
//    i.`item_name` AS itemName,
//    i.`sell_counts` AS sellCounts,
//    ii.url AS imgUrl,
//    tempSpec.price_discount AS price
    private String itemId;
    private String itemName;
    private Integer sellCounts;
    private String imgUrl;
    private Integer price;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getSellCounts() {
        return sellCounts;
    }

    public void setSellCounts(Integer sellCounts) {
        this.sellCounts = sellCounts;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
