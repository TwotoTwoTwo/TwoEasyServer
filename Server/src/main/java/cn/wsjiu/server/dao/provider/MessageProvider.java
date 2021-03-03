package cn.wsjiu.server.dao.provider;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public class MessageProvider {
    /**
     * 生成动态sql
     * @param msgIdList 消息id集合
     * @return 动态拼接后的update sql
     */
    public String updateToReadProvider(@Param("msgIdList") List<Integer> msgIdList) {
        StringBuilder updateSql = new StringBuilder("update message set isRead = true where msgId in ");
        updateSql.append("(");
        if(msgIdList == null) {
            updateSql.append(')');
            return updateSql.toString();
        }
        for (Integer msgId : msgIdList
        ) {
            updateSql.append(msgId);
            updateSql.append(',');
        }
        if(msgIdList.size() != 0) {
            updateSql.deleteCharAt(updateSql.length() - 1);
        }
        updateSql.append(')');
        return updateSql.toString();
    }
}
