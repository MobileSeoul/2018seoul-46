package me.quiz_together.root.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import me.quiz_together.root.model.user.UserReferral;
import me.quiz_together.root.repository.user.UserReferralRepository;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserReferralService {
    private final UserReferralRepository userReferralRepository;
    private final UserInventoryService userInventoryService;

    public void insertReferralUser(Long userId, Long referralUser) {
        UserReferral userReferral = new UserReferral();
        userReferral.setUserId(userId);
        userReferral.setReferralUser(referralUser);

        userReferralRepository.insertReferralUser(userReferral);

        userInventoryService.updateUserHeartCount(userId, 1);
        userInventoryService.updateUserHeartCount(referralUser, 1);
    }
}
