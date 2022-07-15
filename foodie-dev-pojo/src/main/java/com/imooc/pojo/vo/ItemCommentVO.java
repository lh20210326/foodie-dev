package com.imooc.pojo.vo;

import java.util.Date;

/**
 * 用于展示商品评价的VO
 */
public class ItemCommentVO {
//    ic.`comment_level`AS commentLevel,
//    ic.`content`AS content,
//    ic.`sepc_name`AS sepcName,
//    ic.`created_time`AS createdTime,
//    u.face AS userFace,
//    u.nickname AS nickName
    private Integer commentLevel;
    private String content;
    private String specName;
    private Date createdTime;
    private String userFace;
    private String nickname;

    public Integer getCommentLevel() {
        return commentLevel;
    }

    public void setCommentLevel(Integer commentLevel) {
        this.commentLevel = commentLevel;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getUserFace() {
        return userFace;
    }

    public void setUserFace(String userFace) {
        this.userFace = userFace;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
