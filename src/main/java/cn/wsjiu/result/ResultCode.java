package cn.wsjiu.result;

public enum ResultCode {

    /**
     * operation success
     */
    SUCCESS(0, "success"),

    /**
     * operation have server error and msg about detail
     */
    SERRVER_ERROR(1, "服务器异常"),

    /**
     * authorize have error
     */
    AUTHORIZE_ERROR(2, "授权异常"),

    /**
     *
     */
    ACCOUNT_ERROR(3, "请输入正确的账户"),

    /**
     *
     */
    PASSWORD_ERROR(4, "密码错误"),

    /**
     *
     */
    MYSQL_ERROR(5, "mysql error"),

    /**
     *
     */
    REGISTER_ERROR(6, "注册失败，请检查账户或者认证是否重复"),

    PARAM_ERROR(7, "参数异常");

    private int code;
    private String msg;
    ResultCode(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
