package cn.wsjiu.server.entity;

public enum GoodsState {

    UNSOLD(0, "待售出"),
    TRANSACTION(1, "交易中"),
    SOLD(1 << 1, "交易结束"),
    OFFLINE(1 << 2, "已下架");

    public int mask;
    public String state;
    GoodsState(int mask, String state) {
        this.mask = mask;
        this.state = state;
    }
}
