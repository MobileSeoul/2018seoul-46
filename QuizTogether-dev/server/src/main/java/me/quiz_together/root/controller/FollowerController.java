package me.quiz_together.root.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import lombok.AllArgsConstructor;
import me.quiz_together.root.model.request.user.UserFollowerRequest;
import me.quiz_together.root.model.response.user.UserFollowerViewList;
import me.quiz_together.root.model.supoort.ResultContainer;
import me.quiz_together.root.service.user.view.UserFollowerViewService;
import me.quiz_together.root.support.hashid.HashUserId;

@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class FollowerController implements ApiController {

    private UserFollowerViewService userFollowerViewService;

    @PostMapping("/follower/insertFollower")
    public ResultContainer<Void> insertFollower(@RequestBody @Valid UserFollowerRequest userFollowerRequest) {
        userFollowerViewService.insertFollower(userFollowerRequest);
        return new ResultContainer<>();
    }

    @PostMapping("/follower/deleteFollower")
    public ResultContainer<Void> deleteFollower(@RequestBody @Valid UserFollowerRequest userFollowerRequest) {
        userFollowerViewService.deleteFollower(userFollowerRequest);
        return new ResultContainer<>();
    }

    @ApiImplicitParam(name = "userId", value = "user hash Id", paramType = "query",
            dataType = "string")
    @GetMapping("/follower/getFollowerList")
    public ResultContainer<UserFollowerViewList> getFollowerList(@RequestParam @NotNull @HashUserId Long userId) {

        return new ResultContainer<>(userFollowerViewService.getFollowerListByUserId(userId));
    }
}
