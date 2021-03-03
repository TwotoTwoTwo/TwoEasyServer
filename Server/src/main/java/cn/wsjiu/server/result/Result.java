package cn.wsjiu.server.result;

public class Result<T> {
    private int code;
    private String msg;
    private boolean success;
    private T data;

    private Result(){
        success = true;
    }

    public Result(T data){
        this.success = true;
        this.data = data;
        this.code = ResultCode.SUCCESS.getCode();
        this.msg = ResultCode.SUCCESS.getMsg();
    }

    public Result(int code, String msg) {
        this.success = code == ResultCode.SUCCESS.getCode();
        this.code = code;
        this.msg = msg;
    }

    public Result(ResultCode resultCode) {
        this(resultCode.getCode(), resultCode.getMsg());
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
