package cn.wsjiu.server.entity;

/**
 * 订单实体
 * @author wsj
 */
public class Order {
    /**
     * 订单id
     */
    private Integer orderId;
    /**
     * 卖家id
     */
    private Integer sellerId;
    /**
     * 买家id
     */
    private Integer buyerId;
    /**
     * 物品id
     */
    private Integer goodsId;

    /**
     * 交易模式
     * offline 线下 =  1
     * express 快递 = 1 << 1
     */
    private Integer transactionMode;

    /**
     * 交易地点，也可能是邮寄地点，由transactionMode决定
     */
    private String site;

    /**
     * 订单时间戳
     */
    private String time;

    /**
     * 订单状态
     */
    private Integer state;

    /**
     * 快递单号
     */
    private String expressNumber;

    /**
     * 联系电话
     */
    private String phoneNumber;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public Integer getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Integer buyerId) {
        this.buyerId = buyerId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getTransactionMode() {
        return transactionMode;
    }

    public void setTransactionMode(Integer transactionMode) {
        this.transactionMode = transactionMode;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getExpressNumber() {
        return expressNumber;
    }

    public void setExpressNumber(String expressNumber) {
        this.expressNumber = expressNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
