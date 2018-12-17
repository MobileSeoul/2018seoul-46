package me.quiz_together.root.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiImplicitParam;
import me.quiz_together.root.model.request.user.UserIdRequest;
import me.quiz_together.root.model.request.user.UserReferralRequest;
import me.quiz_together.root.model.request.user.UserSignupRequest;
import me.quiz_together.root.model.response.user.UserInfoView;
import me.quiz_together.root.model.response.user.UserProfileView;
import me.quiz_together.root.model.supoort.ResultContainer;
import me.quiz_together.root.service.user.view.UserViewService;
import me.quiz_together.root.support.hashid.HashUserId;


@RestController
public class UserController implements ApiController {
    @Autowired
    private UserViewService userViewService;

    @PostMapping("/user/signup")
    public ResultContainer<UserInfoView> signup(@RequestBody @Valid UserSignupRequest user) {

        return new ResultContainer<>(userViewService.insertUser(user));
    }

    @PostMapping("/user/deleteUserById")
    public ResultContainer<Void> deleteUserById(@RequestBody @Valid UserIdRequest userIdRequest) {
        userViewService.deleteUserById(userIdRequest);

        return new ResultContainer<>();
    }

    @PostMapping("/user/login")
    public ResultContainer<UserInfoView> login(@RequestBody @Valid UserIdRequest userIdRequest) {
        return new ResultContainer<>(userViewService.login(userIdRequest));
    }

    @ApiImplicitParam(name = "userId", value = "user hash Id", paramType = "query",
            dataType = "string")
    @PostMapping("/user/updateUserProfile")
    public ResultContainer<Void> updateUserProfile(@RequestParam @NotNull @HashUserId Long userId,
                                                     @RequestPart MultipartFile profileImage) {
        userViewService.updateUserProfile(userId, profileImage);
        return new ResultContainer<>();
    }

    @PostMapping("/user/insertReferralCode")
    public ResultContainer<Void> insertReferralCode(UserReferralRequest userReferralRequest) {
        userViewService.insertReferralCode(userReferralRequest);
        return new ResultContainer<>();
    }

    @ApiImplicitParam(name = "userId", value = "user hash Id", paramType = "query",
            dataType = "string")
    @GetMapping("/user/getUserProfile")
    public ResultContainer<UserProfileView> getUserProfile(@RequestParam @NotNull @HashUserId Long userId) {

        return new ResultContainer<>(userViewService.getUserProfileView(userId));
    }

    @ApiImplicitParam(name = "userId", value = "user hash Id", paramType = "query",
            dataType = "string")
    @GetMapping("/user/logout")
    public ResultContainer<Void> logout(@RequestParam @NotNull @HashUserId Long userId) {

        return new ResultContainer<>();
    }

    @GetMapping("/user/findUserByName")
    public ResultContainer<Void> findUserByName(@RequestParam @NotNull String name) {
        userViewService.findUserByName(name);
        return new ResultContainer<>();
    }

}
