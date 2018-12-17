package me.quiz_together.root.repository.user;

import java.util.Collection;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import me.quiz_together.root.model.user.User;

@Mapper
public interface UserMapper {

    void insertUser(User user);

    User selectUserById(@Param("id")long id);

    @MapKey("id")
    Map<Long, User> getUserByIds(@Param("userIds") Collection<Long> userIds);

    int updateUserProfile(long userId, String profileImageUrl);

    int deleteUserById(@Param("id")long id);

    User selectUserByName(@Param("name") String name);

    User login(@Param("userId") long userId);
}
