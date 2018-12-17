package me.quiz_together.root.service.user.view;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.quiz_together.root.exceptions.ConflictFollowerException;
import me.quiz_together.root.model.request.user.UserFollowerRequest;
import me.quiz_together.root.model.response.user.UserFollowerView;
import me.quiz_together.root.model.response.user.UserFollowerViewList;
import me.quiz_together.root.model.user.UserFollower;
import me.quiz_together.root.service.user.UserFollowerService;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserFollowerViewService {
    private UserFollowerService userFollowerService;

    public void insertFollower(UserFollowerRequest userFollowerRequest) {
        if (userFollowerRequest.getUserId().equals(userFollowerRequest.getFollower())) {
            throw new ConflictFollowerException();
        }
        userFollowerService.insertFollower(convertUserFollower(userFollowerRequest));
    }

    public void deleteFollower(UserFollowerRequest userFollowerRequest) {
        userFollowerService.deleteFollower(convertUserFollower(userFollowerRequest));
    }

    public UserFollowerViewList getFollowerListByUserId(Long userId) {
        List<UserFollower> userFollowerList = userFollowerService.getFollowerListByUserId(userId);

        return UserFollowerViewList.builder()
                                   .userFollowerList(userFollowerList.stream().map(this::buildUserFollowerView)
                                                                     .collect(Collectors.toList()))
                                   .build();
    }

    private UserFollowerView buildUserFollowerView(UserFollower userFollower) {
        return UserFollowerView.builder()
                               .follower(userFollower.getFollower())
                               .build();
    }

    private UserFollower convertUserFollower(UserFollowerRequest userFollowerRequest) {
        UserFollower userFollower = new UserFollower();
        userFollower.setFollower(userFollowerRequest.getFollower());
        userFollower.setUserId(userFollowerRequest.getUserId());

        return userFollower;
    }
}
