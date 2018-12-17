package me.quiz_together.root.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.quiz_together.root.exceptions.InaccessibleBroadcastException;
import me.quiz_together.root.model.broadcast.Broadcast;
import me.quiz_together.root.model.broadcast.BroadcastStatus;
import me.quiz_together.root.model.firebase.AnswerMessage;
import me.quiz_together.root.model.firebase.BroadcastPlayInfoMessage;
import me.quiz_together.root.model.firebase.ChatMessage;
import me.quiz_together.root.model.firebase.EndBroadcastMessage;
import me.quiz_together.root.model.firebase.FcmContainer;
import me.quiz_together.root.model.firebase.FcmMessage;
import me.quiz_together.root.model.firebase.FcmResponse;
import me.quiz_together.root.model.firebase.FollowBroadcastMessage;
import me.quiz_together.root.model.firebase.PushType;
import me.quiz_together.root.model.firebase.QuestionMessage;
import me.quiz_together.root.model.firebase.WinnersMessage;
import me.quiz_together.root.model.question.Question;
import me.quiz_together.root.model.request.broadcast.EndBroadcastRequest;
import me.quiz_together.root.model.request.firebase.BroadcastPlayInfoRequest;
import me.quiz_together.root.model.request.firebase.ChatMessageRequest;
import me.quiz_together.root.model.request.firebase.OpenAnswerRequest;
import me.quiz_together.root.model.request.firebase.OpenQuestionRequest;
import me.quiz_together.root.model.request.firebase.OpenWinnersRequest;
import me.quiz_together.root.model.user.User;
import me.quiz_together.root.service.broadcast.BroadcastService;
import me.quiz_together.root.service.question.QuestionService;
import me.quiz_together.root.service.user.UserService;
import me.quiz_together.root.support.FcmRestTemplate;
import me.quiz_together.root.support.HashIdUtils;
import me.quiz_together.root.support.HashIdUtils.HashIdType;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class FcmService {
    private static final Pattern COMPILE = Pattern.compile("[\r\n]+");
    private static final String TO_PREFIX = "/topics/";
    private final UserService userService;
    private final BroadcastService broadcastService;
    private final QuestionService questionService;
    private final FcmRestTemplate fcmRestTemplate;
    private final ObjectMapper objectMapper;

    public FcmResponse sendChatMessage(ChatMessageRequest chatMessageRequest, PushType pushType) {
        User user = userService.getUserById(chatMessageRequest.getUserId());

        String to = generateTopics(chatMessageRequest.getBroadcastId(), HashIdType.BROADCAST_ID);
        ChatMessage chatMessage = ChatMessage.builder()
                                             .message(chatMessageRequest.getMessage())
                                             .userName(user.getName())
                                             .pushType(pushType)
                                             .build();

        return postForMessage(to, chatMessage);
    }

    public FcmResponse sendQuestion(OpenQuestionRequest openQuestionRequest) {
        broadcastService.checkPermissionBroadcast(openQuestionRequest.getBroadcastId(),
                                                  openQuestionRequest.getUserId());
        //broadcast step validation
        // 문제 제출 시에는 step이 올라가있지 않아 -1을 해줌
        broadcastService.validCurrentBroadcastStep(openQuestionRequest.getBroadcastId(),
                                                   openQuestionRequest.getStep() - 1);
        // TODO: 문제 제출 마감시간은 update 이후 n초가 좋아보임
        //방송 상태 validation
        Broadcast broadcast = broadcastService.getBroadcastById(openQuestionRequest.getBroadcastId());
        broadcastService.validNextBroadcastStatusAndUpdateBroadcastStatus(broadcast.getBroadcastStatus(),
                                                                          BroadcastStatus.OPEN_QUESTION,
                                                                          broadcast.getId());
        //문제 제출 마감시간 설정

        //현재의 방송 step 등록
        broadcastService.insertBroadcastStep(openQuestionRequest.getBroadcastId(),
                                             openQuestionRequest.getStep());
        Question question = questionService.getQuestionByBroadcastIdAndStep(
                openQuestionRequest.getBroadcastId(),
                openQuestionRequest.getStep());

        String to = generateTopics(openQuestionRequest.getBroadcastId(), HashIdType.BROADCAST_ID);
        QuestionMessage questionMessage = QuestionMessage.builder()
                                                         .questionProp(question.getQuestionProp())
                                                         .step(openQuestionRequest.getStep())
                                                         .pushType(PushType.QUESTION_MESSAGE)
                                                         .build();

        return postForMessage(to, questionMessage);
    }

    public FcmResponse sendAnswer(OpenAnswerRequest openAnswerRequest) {
        broadcastService.checkPermissionBroadcast(openAnswerRequest.getBroadcastId(),
                                                  openAnswerRequest.getUserId());
        // TODO: 방송 마감 시간 이후 인지 확인하는 로직 추가
        ///// this ///////

        //broadcast step validation
        broadcastService.validCurrentBroadcastStep(openAnswerRequest.getBroadcastId(),
                                                   openAnswerRequest.getStep());

        //방송 상태 validation
        Broadcast broadcast = broadcastService.getBroadcastById(openAnswerRequest.getBroadcastId());
        broadcastService.validNextBroadcastStatusAndUpdateBroadcastStatus(broadcast.getBroadcastStatus(),
                                                                          BroadcastStatus.OPEN_ANSWER,
                                                                          broadcast.getId());

        Question question = questionService.getQuestionByBroadcastIdAndStep(openAnswerRequest.getBroadcastId(),
                                                                            openAnswerRequest.getStep());

        String to = generateTopics(openAnswerRequest.getBroadcastId(), HashIdType.BROADCAST_ID);
        Map<Integer, Integer> questionAnswerStat = broadcastService.getQuestionAnswerStat(
                openAnswerRequest.getBroadcastId(), openAnswerRequest.getStep());

        AnswerMessage answerMessage = AnswerMessage.builder()
                                                   .step(openAnswerRequest.getStep())
                                                   .questionProp(question.getQuestionProp())
                                                   .answerNo(question.getAnswerNo())
                                                   .questionStatistics(questionAnswerStat)
                                                   .pushType(PushType.ANSWER_MESSAGE)
                                                   .build();

        return postForMessage(to, answerMessage);
    }

    public FcmResponse sendWinners(OpenWinnersRequest openWinnersRequest) {
        broadcastService.checkPermissionBroadcast(openWinnersRequest.getBroadcastId(),
                                                  openWinnersRequest.getUserId());
        //broadcast step validation
        Broadcast broadcast = broadcastService.getBroadcastById(openWinnersRequest.getBroadcastId());
        int totalStep = broadcast.getQuestionCount();
        broadcastService.validCurrentBroadcastStep(openWinnersRequest.getBroadcastId(), totalStep);
        //방송 상태 validation
        broadcastService.validNextBroadcastStatusAndUpdateBroadcastStatus(broadcast.getBroadcastStatus(),
                                                                          BroadcastStatus.OPEN_WINNER,
                                                                          broadcast.getId());

        String to = generateTopics(openWinnersRequest.getBroadcastId(), HashIdType.BROADCAST_ID);

        Set<Long> userIds = broadcastService.getPlayUserIds(openWinnersRequest.getBroadcastId(), totalStep);
        Map<Long, User> userList = userService.getUserByIds(userIds);
        List<String> userNameList = userList.values().stream().map(User::getName).collect(Collectors.toList());

        //TODO : 상금이 Prize인 경우 우승 자 수 만큼 나눈 뒤에 소수 자리 정하기

        WinnersMessage winnersMessage = WinnersMessage.builder()
                                                      .giftDescription(broadcast.getGiftDescription())
                                                      .giftType(broadcast.getGiftType())
                                                      .prize(broadcast.getPrize())
                                                      .userName(userNameList)
                                                      .winnerMessage(broadcast.getWinnerMessage())
                                                      .pushType(PushType.WINNERS_MESSAGE)
                                                      .build();

        return postForMessage(to, winnersMessage);
    }

    public FcmResponse sendEndBroadcast(EndBroadcastRequest endBroadcastRequest) {
        broadcastService.checkPermissionBroadcast(endBroadcastRequest.getBroadcastId(),
                                                  endBroadcastRequest.getUserId());
        //방송 상태 validation
        //TODO 강제종료 기능 추가시엔 해당 기능 검토 필요
//        Broadcast broadcast = broadcastService.getBroadcastById(endBroadcastRequest.getBroadcastId());
//        validNextBroadcastStatusAndUpdateBroadcastStatus(broadcast.getBroadcastStatus(), BroadcastStatus.COMPLETED, broadcast.getId());

        String to = generateTopics(endBroadcastRequest.getBroadcastId(), HashIdType.BROADCAST_ID);

        EndBroadcastMessage endBroadcastMessage = EndBroadcastMessage.builder()
                                                                     .pushType(PushType.END_MESSAGE)
                                                                     .build();

        return postForMessage(to, endBroadcastMessage);
    }

    public FcmResponse sendFollowBroadcast(Broadcast broadcast) {
        broadcastService.validNextBroadcastStatusAndUpdateBroadcastStatus(broadcast.getBroadcastStatus(),
                                                                          BroadcastStatus.WATING,
                                                                          broadcast.getId());
        User user = userService.getUserById(broadcast.getUserId());

        String to = generateTopics(broadcast.getUserId(), HashIdType.USER_ID);
        FollowBroadcastMessage followBroadcastMessage = FollowBroadcastMessage.builder()
                                                                              .title(broadcast.getTitle())
                                                                              .broadcastId(broadcast.getId())
                                                                              .title(broadcast.getTitle())
                                                                              .description(broadcast
                                                                                                   .getDescription())
                                                                              .userName(user.getName())
                                                                              .pushType(
                                                                                      PushType.FOLLOW_BROADCAST)
                                                                              .build();

        return postForMessage(to, followBroadcastMessage);
    }

    public FcmResponse sendBroadcastPlayInfo(BroadcastPlayInfoRequest broadcastPlayInfoRequest) {
        Broadcast broadcast = broadcastService.getBroadcastById(broadcastPlayInfoRequest.getBroadcastId());
        if (!broadcast.getBroadcastStatus().isAccessibleBroadcast()) {
            throw new InaccessibleBroadcastException();
        }

        Long viewerCount = broadcastService.getCurrentViewers(broadcastPlayInfoRequest.getBroadcastId());
        String to = generateTopics(broadcastPlayInfoRequest.getBroadcastId(), HashIdType.BROADCAST_ID);
        BroadcastPlayInfoMessage broadcastPlayInfoMessage = BroadcastPlayInfoMessage.builder()
                                                                                    .broadcastStatus(broadcast.getBroadcastStatus())
                                                                                    .viewerCount(viewerCount)
                                                                                    .pushType(PushType.BROADCAST_PLAY_INFO)
                                                                                    .build();

        return postForMessage(to, broadcastPlayInfoMessage);
    }

    private String generateTopics(Long id, HashIdType hashIdType) {
        return String.format("%s%s", TO_PREFIX, HashIdUtils.encryptId(hashIdType, id));
    }

    private FcmResponse postForMessage(String to, FcmMessage message) {
        FcmContainer fcmContainer = new FcmContainer(to, message);
        printFcmContainer(fcmContainer);

        return fcmRestTemplate.postForMessage(fcmContainer, FcmResponse.class);
    }

    private void printFcmContainer(FcmContainer fcmContainer) {
        String responseBody = null;
        try {
            responseBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(fcmContainer);
            if (StringUtils.isNotBlank(responseBody)) {
                String[] lines = COMPILE.split(responseBody);
                log.info("<< FcmContainer <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                for (String line : lines) {
                    log.info("<< PAYLOAD << {}", line);
                }
                log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            }

        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException : {}", e);
            throw new RuntimeException(e);
        }


    }

}
