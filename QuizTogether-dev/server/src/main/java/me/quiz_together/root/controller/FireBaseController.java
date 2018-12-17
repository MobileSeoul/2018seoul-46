package me.quiz_together.root.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import me.quiz_together.root.model.firebase.FcmResponse;
import me.quiz_together.root.model.firebase.PushType;
import me.quiz_together.root.model.request.firebase.BroadcastPlayInfoRequest;
import me.quiz_together.root.model.request.firebase.ChatMessageRequest;
import me.quiz_together.root.model.request.firebase.OpenAnswerRequest;
import me.quiz_together.root.model.request.firebase.OpenQuestionRequest;
import me.quiz_together.root.model.request.firebase.OpenWinnersRequest;
import me.quiz_together.root.model.supoort.ResultContainer;
import me.quiz_together.root.service.FcmService;

@Slf4j
@RestController
public class FireBaseController implements ApiController {
    @Autowired
    private FcmService fcmService;

    @PostMapping("/firebase/openAnswer")
    public ResultContainer<FcmResponse> openAnswer(@RequestBody @Valid OpenAnswerRequest openAnswerRequest) {
        log.debug("openAnswerRequest : {}", openAnswerRequest.toString());
        return new ResultContainer<>(fcmService.sendAnswer(openAnswerRequest));
    }

    @PostMapping("/firebase/openQuestion")
    public ResultContainer<FcmResponse> openQuestion(@RequestBody @Valid OpenQuestionRequest openQuestionRequest) {
        log.debug("openQuestionRequest : {}", openQuestionRequest.toString());
        return new ResultContainer<>(fcmService.sendQuestion(openQuestionRequest));
    }

    @PostMapping("/firebase/openWinners")
    public ResultContainer<FcmResponse> openWinners(@RequestBody @Valid OpenWinnersRequest openWinnersRequest) {
        log.debug("openWinnersRequest : {}", openWinnersRequest.toString());

        return new ResultContainer<>(fcmService.sendWinners(openWinnersRequest));
    }

    @PostMapping("/firebase/sendChatMessage")
    public ResultContainer<FcmResponse> sendChatMessage(@RequestBody @Valid ChatMessageRequest chatMessageRequest) {
        return new ResultContainer<>(fcmService.sendChatMessage(chatMessageRequest, PushType.CHAT_MESSAGE));
    }

    @PostMapping("/firebase/sendAdminChatMessage")
    public ResultContainer<FcmResponse> sendAdminChatMessage(@RequestBody @Valid ChatMessageRequest chatMessageRequest) {
        return new ResultContainer<>(fcmService.sendChatMessage(chatMessageRequest, PushType.ADMIN_MESSAGE));
    }

    @PostMapping("/firebase/sendBroadcastPlayInfo")
    public ResultContainer<FcmResponse> sendBroadcastPlayInfo(@RequestBody @Valid BroadcastPlayInfoRequest broadcastPlayInfoRequest) {
        return new ResultContainer<>(fcmService.sendBroadcastPlayInfo(broadcastPlayInfoRequest));
    }

}
