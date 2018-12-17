package me.quiz_together.root.repository.user;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;
import me.quiz_together.root.model.user.UserFollower;

@Repository
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserFollowerRepository {
    private UserFollowerMapper userFollowerMapper;

    public void insertFollower(UserFollower userFollower) {
        userFollowerMapper.insertFollower(userFollower);
    }

    public void deleteFollower(UserFollower userFollower) {
        userFollowerMapper.deleteFollower(userFollower);
    }

    public List<UserFollower> selectFollowerListByUserId(@Param("userId") Long userId) {
        return userFollowerMapper.selectFollowerListByUserId(userId);
    }
}
