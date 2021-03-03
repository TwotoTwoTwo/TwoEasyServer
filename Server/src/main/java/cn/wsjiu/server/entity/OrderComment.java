package cn.wsjiu.server.entity;

public class OrderComment {
    /**
     * 评论时间戳
     */
    private String time;
    /**
     * 评论属于的订单的id
     */
    private Integer orderId;

    /**
     * 订单的买家id
     */
    private Integer buyerId;

    /**
     * 订单的卖家id
     */
    private Integer sellerId;

    /**
     * 评论内容
     */
    private String content;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Integer buyerId) {
        this.buyerId = buyerId;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }
}
