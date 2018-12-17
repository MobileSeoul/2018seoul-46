package me.quiz_together.root.service.user.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import me.quiz_together.root.model.request.user.UserIdRequest;
import me.quiz_together.root.model.request.user.UserReferralRequest;
import me.quiz_together.root.model.request.user.UserSignupRequest;
import me.quiz_together.root.model.response.user.UserInfoView;
import me.quiz_together.root.model.response.user.UserProfileView;
import me.quiz_together.root.model.user.User;
import me.quiz_together.root.model.user.UserInventory;
import me.quiz_together.root.service.user.UserInventoryService;
import me.quiz_together.root.service.user.UserReferralService;
import me.quiz_together.root.service.user.UserService;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserViewService {
    private final UserService userService;
    private final UserReferralService userReferralService;
    private final UserInventoryService userInventoryService;

    public UserInfoView login(UserIdRequest userIdRequest) {
        User user = userService.login(userIdRequest);

        return UserInfoView.builder()
                           .userId(user.getId())
                           .name(user.getName())
                           .build();
    }

    public UserProfileView getUserProfileView(long userId) {
        User user = userService.getUserById(userId);
        UserInventory userInventory = userInventoryService.getUserInventoryByUserId(userId);

        return UserProfileView.builder()
                              .userId(user.getId())
                              .profilePath(user.getProfilePath())
                              .money(user.getMoney())
                              .name(user.getName())
                              .heartCount(userInventory.getHeartCount())
                              .build();
    }

    public UserInfoView insertUser(UserSignupRequest userSignupRequest) {
        User user = userService.insertUser(userSignupRequest);

        return UserInfoView.builder()
                           .userId(user.getId())
                           .name(user.getName())
                           .build();
    }

    public void deleteUserById(UserIdRequest userIdRequest) {
        userService.deleteUserById(userIdRequest.getUserId());
    }

    public void updateUserProfile(long userId, MultipartFile profileImage) {
        userService.updateUserProfile(userId, profileImage);
    }

    public void findUserByName(String name) {
        userService.findUserByName(name);
    }

    public void insertReferralCode(UserReferralRequest userReferralRequest) {
        userReferralService.insertReferralUser(userReferralRequest.getUserId(),
                                               userReferralRequest.getReferralUser());
    }
}
