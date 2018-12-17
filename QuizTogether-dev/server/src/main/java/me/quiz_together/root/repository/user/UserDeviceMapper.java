package me.quiz_together.root.repository.user;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import me.quiz_together.root.model.user.UserDevice;

@Mapper
public interface UserDeviceMapper {

    UserDevice selectUserDeviceByUserId(@Param("userId") long userId);

    void insertUserDevice(UserDevice userDevice);
}
