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
import io.swagger.annotations.ApiImplicitParams;
import me.quiz_together.root.model.request.broadcast.BroadcastRequest;
import me.quiz_together.root.model.request.broadcast.BroadcastUpdateRequest;
import me.quiz_together.root.model.request.broadcast.DeleteBroadcastRequest;
import me.quiz_together.root.model.request.broadcast.EndBroadcastRequest;
import me.quiz_together.root.model.request.broadcast.LeaveBroadcastRequest;
import me.quiz_together.root.model.request.broadcast.SendAnswerRequest;
import me.quiz_together.root.model.request.broadcast.SendHeartRequest;
import me.quiz_together.root.model.request.broadcast.StartBroadcastRequest;
import me.quiz_together.root.model.request.broadcast.UpdateBroadcastStatusRequest;
import me.quiz_together.root.model.response.broadcast.BroadcastForUpdateView;
import me.quiz_together.root.model.response.broadcast.BroadcastView;
import me.quiz_together.root.model.response.broadcast.JoinBroadcastView;
import me.quiz_together.root.model.response.broadcast.PagingBroadcastListView;
import me.quiz_together.root.model.response.broadcast.StartBroadcastView;
import me.quiz_together.root.model.supoort.ResultContainer;
import me.quiz_together.root.service.broadcast.BroadcastViewService;
import me.quiz_together.root.support.hashid.HashBroadcastId;
import me.quiz_together.root.support.hashid.HashUserId;

@RestController
public class BroadcastController implements ApiController {
    @Autowired
    private BroadcastViewService broadcastViewService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "next", value = "broadcast hash Id", paramType = "query", required = false,
                    dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "user hash Id", paramType = "query", required = false, dataType = "string")
    })
    @GetMapping("/broadcast/getPagingBroadcastList")
    public ResultContainer<PagingBroadcastListView> getPagingBroadcastList(@RequestParam(defaultValue = "b0") @HashBroadcastId Long next, @RequestParam(defaultValue = "50") int limit,
                                                                           @RequestParam(required = false) @HashUserId Long userId) {

        return new ResultContainer<>(broadcastViewService.getPagingBroadcastList(next, limit, userId));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "broadcastId", value = "broadcast hash Id", paramType = "query", required = true,
                    dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "user hash Id", paramType = "query", required = true, dataType = "string")
    })
    @GetMapping("/broadcast/joinBroadcast")
    public ResultContainer<JoinBroadcastView> joinBroadcast(@RequestParam @NotNull @HashBroadcastId Long broadcastId, @RequestParam @NotNull @HashUserId
            Long userId) {
        return new ResultContainer<>(broadcastViewService.getJoinBroadcastView(broadcastId, userId));
    }

    @ApiImplicitParam(name = "broadcastId", value = "broadcast hash Id", paramType = "query", required = true,
            dataType = "string")
    @GetMapping("/broadcast/getBroadcastForUpdateById")
    public ResultContainer<BroadcastForUpdateView> getBroadcastForUpdateById(@RequestParam @NotNull @HashBroadcastId Long broadcastId) {
        return new ResultContainer<>(broadcastViewService.getBroadcastForUpdateById(broadcastId));
    }

    @ApiImplicitParam(name = "broadcastId", value = "broadcast hash Id", paramType = "query", required = true,
            dataType = "string")
    @GetMapping("/broadcast/getBroadcastInfoById")
    public ResultContainer<BroadcastView> getBroadcastInfoById(@RequestParam @NotNull @HashBroadcastId Long broadcastId) {
        return new ResultContainer<>(broadcastViewService.getBroadcastById(broadcastId));
    }

    @PostMapping("/broadcast/updateBroadcastStatus")
    public ResultContainer<Void> updateBroadcastStatus(@RequestBody @Valid UpdateBroadcastStatusRequest updateBroadcastStatusRequest) {
        broadcastViewService.updateBroadcastStatus(updateBroadcastStatusRequest);
        return new ResultContainer<>();
    }

    @PostMapping("/broadcast/updateBroadcast")
    public ResultContainer<String> updateBroadcast(@RequestBody @Valid BroadcastUpdateRequest broadcastUpdateRequest) {
        return new ResultContainer<>(broadcastViewService.updateBroadcast(broadcastUpdateRequest));
    }

    @PostMapping("/broadcast/sendAnswer")
    public ResultContainer<Void> sendAnswer(@RequestBody @Valid SendAnswerRequest sendAnswerRequest) {
        broadcastViewService.sendAnswer(sendAnswerRequest);
        return new ResultContainer<>();
    }

    @PostMapping("/broadcast/createBroadcast")
    public ResultContainer<String> createBroadcast(@RequestBody @Valid BroadcastRequest broadcastRequest) {
        return new ResultContainer<>(broadcastViewService.createBroadcast(broadcastRequest));
    }

    @PostMapping("/broadcast/endBroadcast")
    public ResultContainer<Void> endBroadcast(@RequestBody @Valid EndBroadcastRequest endBroadcastRequest) {
        broadcastViewService.endBroadcast(endBroadcastRequest);
        return new ResultContainer<>();
    }

    @PostMapping("/broadcast/startBroadcast")
    public ResultContainer<StartBroadcastView> startBroadcast(@RequestBody @Valid StartBroadcastRequest startBroadcastRequest) {
        return new ResultContainer<>(broadcastViewService.startBroadcast(startBroadcastRequest));
    }

    @PostMapping("/broadcast/leaveBroadcast")
    public ResultContainer<Void> leaveBroadcast(@RequestBody @Valid LeaveBroadcastRequest leaveBroadcastRequest) {
        broadcastViewService.leaveBroadcast(leaveBroadcastRequest);
        return new ResultContainer<>();
    }

    @PostMapping("/broadcast/deleteBroadcast")
    public ResultContainer<Void> deleteBroadcast(@RequestBody @Valid DeleteBroadcastRequest deleteBroadcastRequest) {
        broadcastViewService.deleteBroadcast(deleteBroadcastRequest);
        return new ResultContainer<>();
    }

    @PostMapping("/broadcast/sendHeart")
    public ResultContainer<Void> sendHeart(@RequestBody @Valid SendHeartRequest sendHeartRequest) {
        broadcastViewService.sendHeart(sendHeartRequest);
        return new ResultContainer<>();
    }

}
