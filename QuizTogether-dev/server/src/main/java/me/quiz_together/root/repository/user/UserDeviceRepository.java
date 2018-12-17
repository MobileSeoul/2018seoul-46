package me.quiz_together.root.repository.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import me.quiz_together.root.model.user.UserDevice;

@Repository
public class UserDeviceRepository {
    @Autowired
    private UserDeviceMapper userDeviceMapper;

    public UserDevice selectUserDeviceByUserId(long userId) {
        return userDeviceMapper.selectUserDeviceByUserId(userId);
    }

    public void insertUserDevice(UserDevice userDevice) {
        userDeviceMapper.insertUserDevice(userDevice);
    }
}
