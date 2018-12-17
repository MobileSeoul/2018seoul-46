package me.quiz_together.root.repository.user;

import org.apache.ibatis.annotations.Mapper;

import me.quiz_together.root.model.user.UserReferral;

@Mapper
public interface UserReferralMapper {

    void insertReferralUser(UserReferral userReferral);
}
