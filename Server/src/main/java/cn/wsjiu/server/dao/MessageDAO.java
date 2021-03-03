package cn.wsjiu.server.dao;

import cn.wsjiu.server.dao.provider.MessageProvider;
import cn.wsjiu.server.entity.IM.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MessageDAO {
    /**
     * 插入消息记录
     * @param message 消息实体
     * @return 消息的唯一id
     */
    @Insert("insert message(chatId, sendId, goodsId, receiveId, content, contentType, timeStamp, isRead) " +
            "values(#{chatId}, #{sendId}, #{goodsId}, #{receiveId}, #{content}, #{contentType}," +
            "#{timeStamp}, #{isRead})")
    int insert(Message message);

    /**
     * 查询用户在离线状态下的未读的消息
     * @param userId 用户id
     * @return 返回所有未读消息
     */
    @Select("select * from message where receiveId = #{userId} and isRead = false")
    List<Message> queryUnReadByUserId(Integer userId);

    /**
     * 批量更新记录为已读
     * @param msgIdList  消息id集合
     * @return 受影响的行记录数
     */
    @UpdateProvider(type = MessageProvider.class, method = "updateToReadProvider")
    int updateToRead(@Param("msgIdList") List<Integer> msgIdList);
}
