package cn.wsjiu.server.util;

public class StringCheckUtils {
    private final static int ACCOUNT_NAME_MAX_LENGTH = 11;
    private final static int PASSWORD_MIN_LENGTH = 8;

    public static boolean checkAccountName(String accountName) {
        if(accountName == null || accountName.length() != ACCOUNT_NAME_MAX_LENGTH) {
            return false;
        }
        return true;
    }

    public static boolean checkPassword(String password) {
        if(password == null || password.length() < PASSWORD_MIN_LENGTH) {
            return false;
        }
        return true;
    }
}
