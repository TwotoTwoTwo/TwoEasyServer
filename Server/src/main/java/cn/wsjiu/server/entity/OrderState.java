package cn.wsjiu.server.entity;

public enum OrderState {
    TRANSACTION_IN(1, "交易中"),
    TRANSACTION_FINISH(1 << 1, "交易完成—未评论"),
    TRANSACTION_CANCEL(1 << 2, "交易取消"),
    TRANSACTION_COMMENTED(1 << 3, "交易完成—已评论");

    public int mask;
    public String state;
    OrderState(int mask, String state) {
        this.mask = mask;
        this.state = state;
    }
}
