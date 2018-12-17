package me.quiz_together.root.repository.user;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import me.quiz_together.root.model.user.User;

@Repository
public class UserRepository {
    @Autowired
    private UserMapper userMapper;

    public void insertUser(User user) {
        userMapper.insertUser(user);
    }

    public User selectUserById(long id) {
        return userMapper.selectUserById(id);
    }

    public Map<Long, User> getUserByIds(Collection<Long> userIds) {
        return userMapper.getUserByIds(userIds);
    }

    public int updateUserProfile(long userId, String profileImageUrl) {
        return userMapper.updateUserProfile(userId, profileImageUrl);
    }

    public int deleteUserById(long id) {
        return userMapper.deleteUserById(id);
    }

    public User selectUserByName(String name) {
        return userMapper.selectUserByName(name);
    }

    public User login(long userId) {
        return userMapper.login(userId);
    }
}
