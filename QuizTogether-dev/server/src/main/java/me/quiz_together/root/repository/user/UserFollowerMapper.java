package me.quiz_together.root.repository.user;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import me.quiz_together.root.model.user.UserFollower;

@Mapper
public interface UserFollowerMapper {

    void insertFollower(UserFollower userFollower);

    void deleteFollower(UserFollower userFollower);

    List<UserFollower> selectFollowerListByUserId(@Param("userId") Long userId);
}
