package me.quiz_together.root.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.quiz_together.root.model.user.UserDevice;
import me.quiz_together.root.repository.user.UserDeviceRepository;

@Service
public class UserDeviceService {
    @Autowired
    private UserDeviceRepository userDeviceRepository;

    public UserDevice selectUserDeviceByUserId(long userId) {
        return userDeviceRepository.selectUserDeviceByUserId(userId);
    }

    public void insertUserDevice(UserDevice userDevice) {
        userDeviceRepository.insertUserDevice(userDevice);
    }

}
