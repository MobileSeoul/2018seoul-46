package me.quiz_together.root.service.broadcast;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.quiz_together.root.exceptions.AbusingUserException;
import me.quiz_together.root.exceptions.CreatedBroadcastMaxException;
import me.quiz_together.root.exceptions.InaccessibleBroadcastException;
import me.quiz_together.root.exceptions.InvalidCreateException;
import me.quiz_together.root.exceptions.InvalidUpdateException;
import me.quiz_together.root.exceptions.NotFoundUserException;
import me.quiz_together.root.exceptions.PastScheduleTimeThanCurrentTimeException;
import me.quiz_together.root.model.broadcast.Broadcast;
import me.quiz_together.root.model.broadcast.BroadcastStatus;
import me.quiz_together.root.model.question.Question;
import me.quiz_together.root.model.request.broadcast.BroadcastRequest;
import me.quiz_together.root.model.request.broadcast.BroadcastUpdateRequest;
import me.quiz_together.root.model.request.broadcast.DeleteBroadcastRequest;
import me.quiz_together.root.model.request.broadcast.EndBroadcastRequest;
import me.quiz_together.root.model.request.broadcast.LeaveBroadcastRequest;
import me.quiz_together.root.model.request.broadcast.SendAnswerRequest;
import me.quiz_together.root.model.request.broadcast.SendHeartRequest;
import me.quiz_together.root.model.request.broadcast.StartBroadcastRequest;
import me.quiz_together.root.model.request.broadcast.UpdateBroadcastStatusRequest;
import me.quiz_together.root.model.request.question.QuestionRequest;
import me.quiz_together.root.model.response.broadcast.BroadcastForUpdateView;
import me.quiz_together.root.model.response.broadcast.BroadcastView;
import me.quiz_together.root.model.response.broadcast.JoinBroadcastView;
import me.quiz_together.root.model.response.broadcast.PagingBroadcastListView;
import me.quiz_together.root.model.response.broadcast.StartBroadcastView;
import me.quiz_together.root.model.response.question.QuestionView;
import me.quiz_together.root.model.response.user.UserInfoView;
import me.quiz_together.root.model.response.user.UserProfileView;
import me.quiz_together.root.model.user.PlayUserStatus;
import me.quiz_together.root.model.user.User;
import me.quiz_together.root.model.user.UserInventory;
import me.quiz_together.root.service.FcmService;
import me.quiz_together.root.service.question.QuestionService;
import me.quiz_together.root.service.user.UserInventoryService;
import me.quiz_together.root.service.user.UserService;
import me.quiz_together.root.service.user.view.UserViewService;
import me.quiz_together.root.support.HashIdUtils;
import me.quiz_together.root.support.HashIdUtils.HashIdType;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BroadcastViewService {
    private final BroadcastService broadcastService;
    private final QuestionService questionService;
    private final UserService userService;
    private final FcmService fcmService;
    private final UserViewService userViewService;
    private final UserInventoryService userInventoryService;

    public PagingBroadcastListView getPagingBroadcastList(long next, int limit, Long userId) {
        PagingBroadcastListView pagingBroadcastListView = new PagingBroadcastListView();
        if (Objects.nonNull(userId)) {
            List<Broadcast> broadcastList = broadcastService.getMyBroadcastList(userId);
            pagingBroadcastListView.setMyBroadcastList(buildBroadcastViewList(broadcastList));
        }

        List<Broadcast> broadcastList = broadcastService.getPagingBroadcastList(next, limit, userId);
        pagingBroadcastListView.setCurrentBroadcastList(buildBroadcastViewList(broadcastList));

        return pagingBroadcastListView;
    }

    public BroadcastView getBroadcastById(long broadcastId) {
        Broadcast broadcast = broadcastService.getBroadcastById(broadcastId);
        return buildBroadcastView(broadcast);
    }

    public BroadcastForUpdateView getBroadcastForUpdateById(long broadcastId) {
        Broadcast broadcast = broadcastService.getBroadcastById(broadcastId);

        if (broadcast.getBroadcastStatus() != BroadcastStatus.CREATED) {
            // update fail
            log.error("{} 상태에서는 변경할 수 없습니다.", broadcast.getBroadcastStatus().name());
            throw new InvalidUpdateException(broadcast.getBroadcastStatus().name() + "상태에서는 변경할 수 없습니다.");
        }
        List<Question> questionList = questionService.getQuestionListByBroadcastId(broadcastId);

        return BroadcastForUpdateView.builder()
                                     .broadcastId(broadcast.getId())
                                     .description(broadcast.getDescription())
                                     .giftDescription(broadcast.getGiftDescription())
                                     .giftType(broadcast.getGiftType())
                                     .prize(broadcast.getPrize())
                                     .winnerMessage(broadcast.getWinnerMessage())
                                     .scheduledTime(broadcast.getScheduledTime())
                                     .questionList(questionList.stream().map(this::buildQuestionView)
                                                               .collect(Collectors.toList()))
                                     .build();
    }

    public String updateBroadcast(BroadcastUpdateRequest broadcastUpdateRequest) {
        broadcastService.checkPermissionBroadcast(broadcastUpdateRequest.getBroadcastId(), broadcastUpdateRequest.getUserId());

        Broadcast broadcast = new Broadcast();
        broadcast.setId(broadcastUpdateRequest.getBroadcastId());
        broadcast.setDescription(broadcastUpdateRequest.getDescription());
        broadcast.setGiftDescription(broadcastUpdateRequest.getGiftDescription());
        broadcast.setGiftType(broadcastUpdateRequest.getGiftType());
        broadcast.setPrize(broadcastUpdateRequest.getPrize());
        broadcast.setQuestionCount(broadcastUpdateRequest.getQuestionList().size());
        broadcast.setScheduledTime(broadcastUpdateRequest.getScheduledTime());
        broadcast.setTitle(broadcastUpdateRequest.getTitle());
        broadcast.setWinnerMessage(broadcastUpdateRequest.getWinnerMessage());

        int result = broadcastService.updateBroadcast(broadcast);
        if (result == 0) {
            log.error("broadcast update fail");
            throw new RuntimeException("broadcast update fail");
        }
        List<Question> questionList = convertQuestionList(broadcastUpdateRequest.getQuestionList());
        questionService.updateQuestionListByQuestionId(questionList);

        return HashIdUtils.encryptId(HashIdType.BROADCAST_ID, broadcast.getId());
    }

    public String createBroadcast(BroadcastRequest broadcastRequest) {
        if (broadcastService.getPreparedBroadcastByUserId(broadcastRequest.getUserId())) {
            throw new CreatedBroadcastMaxException();
        }
        // 예약 시간은 현재시간 보다 커야 한다.
        if (!isInstantStartBroadcast(broadcastRequest.getScheduledTime()) && broadcastRequest.getScheduledTime() >= System.currentTimeMillis()) {
            throw new PastScheduleTimeThanCurrentTimeException();
        }

        if (Objects.isNull(broadcastRequest.getQuestionList()) || broadcastRequest.getQuestionList().isEmpty()) {
            throw new InvalidCreateException("QuestionList null 또는 size가 0 입니다.");
        }
        // scheduledTime이 null이면 즉시 시작
        Broadcast broadcast = convertBroadcast(broadcastRequest);

        broadcastService.insertBroadcast(broadcast);

        log.debug("questionList : {}", broadcastRequest);
        log.debug("questionList : {}", broadcastRequest.getQuestionList());
        List<Question> questionList = convertQuestionList(broadcastRequest.getQuestionList(), broadcast);
        questionService.insertQuestionList(questionList);

        return HashIdUtils.encryptId(HashIdType.BROADCAST_ID, broadcast.getId());
    }

    public StartBroadcastView startBroadcast(StartBroadcastRequest startBroadcastRequest) {
        // TODO:stream 등록
        // TODO:chat 생성
        // 해당 방송자인지 권한 체크
        broadcastService.checkPermissionBroadcast(startBroadcastRequest.getBroadcastId(), startBroadcastRequest.getUserId());

        //follower에게 push 발송
        Broadcast broadcast = broadcastService.getBroadcastById(startBroadcastRequest.getBroadcastId());
        fcmService.sendFollowBroadcast(broadcast);
        UserProfileView userProfileView = userViewService.getUserProfileView(broadcast.getUserId());

        return StartBroadcastView.builder()
                                 .broadcastView(buildBroadcastView(broadcast))
                                 .userProfileView(userProfileView)
                                 .build();
    }

    public void endBroadcast(EndBroadcastRequest endBroadcastRequest) {
        broadcastService.checkPermissionBroadcast(endBroadcastRequest.getBroadcastId(), endBroadcastRequest.getUserId());
        // 방송 상태 변경
        broadcastService.updateBroadcastStatus(BroadcastStatus.COMPLETED, endBroadcastRequest.getBroadcastId());
        // 방송 종료 push 발송
        fcmService.sendEndBroadcast(endBroadcastRequest);
        // stream 업데이트
        // chat close
        // TODO redis에서 해당 방송 관련된 정보 지우기
    }

    public JoinBroadcastView getJoinBroadcastView(long broadcastId, long userId) {
        BroadcastView broadcastView = buildBroadcastView(broadcastService.getBroadcastById(broadcastId),
                                                         userService.getUserById(userId));

        //방송 상태 validate
        // wating, openQuation, openAnswer, openWinners에서만 가능
        if (!broadcastView.getBroadcastStatus().isAccessibleBroadcast()) {
            throw new InaccessibleBroadcastException();
        }

        JoinBroadcastView joinBroadcastView = new JoinBroadcastView();
        joinBroadcastView.setBroadcastView(broadcastView);

        //viewer 수 증가
        broadcastService.insertViewer(broadcastId, userId);
        //현재 몇 단계 방송 중인지 확인
        Long currentStep = broadcastService.getCurrentBroadcastStep(broadcastId);
        Question question = questionService.getQuestionByBroadcastIdAndStep(broadcastId,
                                                                            currentStep.intValue());
        switch (broadcastView.getBroadcastStatus()) {
            case CREATED:
            case WATING:
                break;
            case OPEN_QUESTION:
                joinBroadcastView.setQuestionProp(question.getQuestionProp());
                joinBroadcastView.setStep(question.getStep());
                break;
            case OPEN_ANSWER:
                joinBroadcastView.setQuestionProp(question.getQuestionProp());
                joinBroadcastView.setStep(question.getStep());
                joinBroadcastView.setAnswerNo(question.getAnswerNo());
                break;
            case OPEN_WINNER:
            case COMPLETED:
                break;
        }

        // 1번 퀴즈 참가 여부 && 이전 문제 play user이면 play상태
        PlayUserStatus playUserStatus = broadcastService.getPlayUserStatus(broadcastId, userId,
                                                                           currentStep.intValue());
        joinBroadcastView.setPlayUserStatus(playUserStatus);

        //currentViewers
        joinBroadcastView.setViewerCount(broadcastService.getCurrentViewers(broadcastId));
        joinBroadcastView.setUserProfileView(userViewService.getUserProfileView(userId));

        return joinBroadcastView;
    }

    public void leaveBroadcast(LeaveBroadcastRequest leaveBroadcastRequest) {
        if (Objects.isNull(userService.getUserById(leaveBroadcastRequest.getUserId()))) {
            throw new NotFoundUserException();
        }
        broadcastService.deleteViewer(leaveBroadcastRequest.getBroadcastId(), leaveBroadcastRequest.getUserId());
    }

    public void updateBroadcastStatus(UpdateBroadcastStatusRequest updateBroadcastStatusRequest) {
        broadcastService.checkPermissionBroadcast(updateBroadcastStatusRequest.getBroadcastId(),
                                 updateBroadcastStatusRequest.getUserId());
        // TODO : broadcast status 검증 필요 추후 검토

        broadcastService.updateBroadcastStatus(updateBroadcastStatusRequest.getBroadcastStatus(),
                                               updateBroadcastStatusRequest.getBroadcastId());
    }

    public void deleteBroadcast(DeleteBroadcastRequest deleteBroadcastRequest) {
        broadcastService.checkPermissionBroadcast(deleteBroadcastRequest.getBroadcastId(), deleteBroadcastRequest.getUserId());
        broadcastService.deleteBroadcastById(deleteBroadcastRequest.getBroadcastId());
    }

    public void sendAnswer(SendAnswerRequest sendAnswerRequest) {
        //validate
        // 해당 방송의 스탭이랑 맞는지 확인
        broadcastService.validCurrentBroadcastStep(sendAnswerRequest.getBroadcastId(), sendAnswerRequest.getStep());
        // status가 openQuestion인지 확인
        broadcastService.validateBroadcastStatusForAbusingUser(sendAnswerRequest.getBroadcastId(), BroadcastStatus.OPEN_QUESTION);
        // 0. 해당 유저의 이전 문제 제출 정보를 통해 status 가져옴
        PlayUserStatus playUserStatus = broadcastService.getPlayUserStatus(sendAnswerRequest.getBroadcastId(),
                                                                           sendAnswerRequest.getUserId(),
                                                                           sendAnswerRequest.getStep());
        // 1. 유저 정답 등록
        if (!broadcastService.insertPlayUserAnswer(sendAnswerRequest.getBroadcastId(), sendAnswerRequest.getUserId(),
                                              sendAnswerRequest.getStep(), sendAnswerRequest.getAnswerNo())) {
            throw new AbusingUserException("abusing user! 정답 중복 제출 시도");
        }
        // 2. 유저가 낸 답이 정답인지 확인
        Question question = questionService.getQuestionByBroadcastIdAndStep(sendAnswerRequest.getBroadcastId(),
                                                                            sendAnswerRequest.getStep());

        if (question.getAnswerNo().equals(sendAnswerRequest.getAnswerNo())) {
            //정답
            //Player인 경우만 Player 등록
            if (playUserStatus == PlayUserStatus.PLAYER) {
                broadcastService.insertPlayUser(sendAnswerRequest.getBroadcastId(), sendAnswerRequest.getUserId(),
                                                sendAnswerRequest.getStep());
            }
            // TODO : 게이지 증가
            // TODO : 게이지가 100%인 경우 하트 증가
        } else {
            // 이전에 player 인 경우 탈락자 등록
            if (playUserStatus == PlayUserStatus.PLAYER) {
                // send heart validation과 관련 있음
                broadcastService.insertLoserUser(sendAnswerRequest.getBroadcastId(), sendAnswerRequest.getUserId(), sendAnswerRequest.getStep());
            }
        }
        // 3. 퀴즈 통계 등록
        // 이전에 player인 경우 통계 등록
        if (playUserStatus == PlayUserStatus.PLAYER) {
            broadcastService.incrementQuestionAnswerStat(sendAnswerRequest.getBroadcastId(),
                                                         sendAnswerRequest.getStep(),
                                                         sendAnswerRequest.getAnswerNo());
        }

    }

    public void sendHeart(SendHeartRequest sendHeartRequest) {
        // validate
        // 해당 방송의 스탭이랑 맞는지 확인
        broadcastService.validCurrentBroadcastStep(sendHeartRequest.getBroadcastId(), sendHeartRequest.getStep());
        // status가 openAnswer인지 확인
        broadcastService.validateBroadcastStatusForAbusingUser(sendHeartRequest.getBroadcastId(), BroadcastStatus.OPEN_ANSWER);
        // TODO : 마지막 문제 하트 사용 여부에 따라 validation 추가
        // 해당 유저가 이전 step에 정답을 제출해서 탈락한 상태인지 확인
        if (!broadcastService.isLoserUser(sendHeartRequest.getBroadcastId(), sendHeartRequest.getUserId(), sendHeartRequest.getStep())) {
            throw new AbusingUserException("abusing user! 하트로 부활 시도");
        }
        // user 하트 사용 유무 확인
        if (!broadcastService.insertUserHeart(sendHeartRequest.getBroadcastId(), sendHeartRequest.getUserId(), sendHeartRequest.getStep())) {
            throw new AbusingUserException("abusing user! 하트 중복 사용 시도");
        }
        // user 하트 갯수 조회 및 감소
        UserInventory userInventory = userInventoryService.getUserInventoryByUserId(sendHeartRequest.getUserId());
        if (userInventory.getHeartCount() <= 0) {
            throw new AbusingUserException("abusing user! 하트 강제 사용 시도 heart count : " + userInventory.getHeartCount());
        }
        if (userInventoryService.updateUserHeartCount(sendHeartRequest.getUserId(), -1) != 1) {
            log.error("user heart count update fail");
            throw new RuntimeException("user heart count update fail");
        }
        // user 상태 업데이트
        broadcastService.deleteLoserUser(sendHeartRequest.getBroadcastId(), sendHeartRequest.getStep());
        broadcastService.insertPlayUser(sendHeartRequest.getBroadcastId(), sendHeartRequest.getUserId(), sendHeartRequest.getStep());
    }

    public QuestionView buildQuestionView(Question question) {
        return QuestionView.builder()
                           .answerNo(question.getAnswerNo())
                           .step(question.getStep())
                           .category(question.getCategory())
                           .questionProp(question.getQuestionProp())
                           .build();
    }

    private BroadcastView buildBroadcastView(Broadcast broadcast) {
        User user = userService.getUserById(broadcast.getUserId());
        return buildBroadcastView(broadcast, user);
    }

    private List<BroadcastView> buildBroadcastViewList(List<Broadcast> broadcastList) {
        List<Long> userIds = broadcastList.stream().map(Broadcast::getUserId).collect(Collectors.toList());
        Map<Long, User> userList = userService.getUserByIds(userIds);

        return broadcastList.stream()
                            .map(broadcast -> buildBroadcastView(broadcast,
                                                                 userList.get(broadcast.getUserId()))
                            ).collect(Collectors.toList());
    }

    private BroadcastView buildBroadcastView(Broadcast broadcast, User user) {
        return BroadcastView.builder()
                            .broadcastId(broadcast.getId())
                            .title(broadcast.getTitle())
                            .scheduledTime(broadcast.getScheduledTime())
                            .remainingStartSeconds((broadcast.getScheduledTime() - System.currentTimeMillis())/1000)
                            .prize(broadcast.getPrize())
                            .giftDescription(broadcast.getGiftDescription())
                            .giftType(broadcast.getGiftType())
                            .broadcastStatus(broadcast.getBroadcastStatus())
                            .description(broadcast.getDescription())
                            .questionCount(broadcast.getQuestionCount())
                            .userInfoView(UserInfoView.builder()
                                                      .userId(user.getId())
                                                      .name(user.getName())
                                                      .build())
                            .build();
    }

    private Broadcast convertBroadcast(BroadcastRequest broadcastRequest) {
        Broadcast broadcast = new Broadcast();
        broadcast.setUserId(broadcastRequest.getUserId());
        broadcast.setTitle(broadcastRequest.getTitle());
        broadcast.setDescription(broadcastRequest.getDescription());
        broadcast.setBroadcastStatus(BroadcastStatus.CREATED);
        broadcast.setPrize(broadcastRequest.getPrize());
        broadcast.setGiftDescription(broadcastRequest.getGiftDescription());
        broadcast.setGiftType(broadcastRequest.getGiftType());
        broadcast.setWinnerMessage(broadcastRequest.getWinnerMessage());
        broadcast.setQuestionCount(broadcastRequest.getQuestionList().size());
        broadcast.setScheduledTime(
                isInstantStartBroadcast(broadcastRequest.getScheduledTime()) ? System.currentTimeMillis() : broadcastRequest
                .getScheduledTime());

        return broadcast;
    }



    private boolean isInstantStartBroadcast(Long scheduledTime) {
        if (Objects.isNull(scheduledTime)) {
            return true;
        }
        return false;
    }

    private List<Question> convertQuestionList(List<QuestionRequest> questionRequestList, Broadcast broadcast) {
        List<Question> questionList = Lists.newArrayList();
        for (int i = 0; i < questionRequestList.size(); ++i) {
            Question question = new Question();
            question.setAnswerNo(questionRequestList.get(i).getAnswerNo());
            question.setCategory(questionRequestList.get(i).getCategory());
            question.setQuestionProp(questionRequestList.get(i).getQuestionProp());
            question.setStep(i+1);
            question.setBroadcastId(broadcast.getId());
            question.setUserId(broadcast.getUserId());

            questionList.add(question);
        }

        return questionList;
    }

    private List<Question> convertQuestionList(List<QuestionView> questionReqList) {
        List<Question> questionList = Lists.newArrayList();
        for (int i = 0 ; i < questionReqList.size(); ++i) {
            Question question = new Question();
            question.setId(questionReqList.get(i).getQuestionId());
            question.setAnswerNo(questionReqList.get(i).getAnswerNo());
            question.setCategory(questionReqList.get(i).getCategory());
            question.setQuestionProp(questionReqList.get(i).getQuestionProp());
            question.setStep(i+1);

            questionList.add(question);
        }

        return questionList;
    }
}
