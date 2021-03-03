package cn.wsjiu.server.result;

public enum ResultCode {

    /**
     * 请求成功
     */
    SUCCESS(0, "success"),

    /**
     * 服务异常
     */
    SERVER_ERROR(1, "服务异常"),

    /**
     * 授权异常
     */
    AUTHORIZE_ERROR(2, "授权异常"),

    /**
     * 账户名异常
     */
    ACCOUNT_ERROR(3, "请输入正确的账户"),

    /**
     * 密码是错误的
     */
    PASSWORD_ERROR(4, "密码错误"),

    /**
     *  数据库发生异常
     */
    MYSQL_ERROR(5, "mysql error"),

    /**
     *  注册失败
     */
    REGISTER_ERROR(6, "注册失败，请检查账户或者认证是否重复"),

    /**
     *  参数异常
     */
    PARAM_ERROR(7, "参数异常"),

    /**
     *  编码格式异常
     */
    BASE64_FORMAT_ERROR(8, "图片的base64编码格式异常"),

    /**
     * 登录异常
     */
    LOGIN_ERROR(9, "登录失败"),


    /**
     * 图片上传失败
     */
    IMAGE_UPLOAD__ERROR(10, "图片上传失败"),

    /**
     * 网络异常
     */
    NET__ERROR(11, "网络异常"),

    /**
     *  用户离线状态
     */
    USER_OFFLINE_ERROR(12, "用户不在线"),

    /**
     *  买家开眼失败
     */
    BUYER_OPEN_EYE_ERROR(13, "卖家未开眼");



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
