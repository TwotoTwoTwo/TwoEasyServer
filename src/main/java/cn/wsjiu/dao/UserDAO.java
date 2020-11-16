package cn.wsjiu.dao;
import cn.wsjiu.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
public interface UserDAO {

    User query(String accountName);

    int insert(User user);
}
