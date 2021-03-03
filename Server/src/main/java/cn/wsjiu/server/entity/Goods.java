package cn.wsjiu.server.entity;

import java.time.LocalDateTime;
import java.util.Date;

public class Goods {
    /**
     * 物品唯一id，主键
     */
    private Integer goodsId;
    /**
     * 发布者的id
     */
    private Integer userId;
    /**
     * 二手物品的标题
     */
    private String title;
    /**
     * 物品具体的详情
     */
    private String detail;
    /**
     * 二手物品的相关图片url
     */
    private String imageUrl;
    /**
     * 售卖价格
     */
    private Float sellPrice;
    /**
     * 入手价
     */
    private Float  buyPrice;
    /**
     * 物品的标签
     */
    private String label;
    /**
     * 发布时间
     */
    private String time;
    /**
     * 分类
     */
    private String classification;
    /**
     * 品牌
     */
    private String brand;
    /**
     * 物品当前状态
     */
    private Integer state;
    /**
     * 感兴趣的人数
     */
    private Integer wants;

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Float getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Float sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Float getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(Float buyPrice) {
        this.buyPrice = buyPrice;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getWants() {
        return wants;
    }

    public void setWants(Integer wants) {
        this.wants = wants;
    }
}
