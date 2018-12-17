package me.quiz_together.root.repository.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;
import me.quiz_together.root.model.user.UserReferral;

@Repository
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserReferralRepository {
    private UserReferralMapper userReferralMapper;

    public void insertReferralUser(UserReferral userReferral) {
        userReferralMapper.insertReferralUser(userReferral);
    }
}
