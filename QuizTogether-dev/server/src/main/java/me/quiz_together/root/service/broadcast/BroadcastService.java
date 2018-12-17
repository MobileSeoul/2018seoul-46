package me.quiz_together.root.service.broadcast;

import static me.quiz_together.root.model.broadcast.BroadcastStatus.COMPLETED;
import static me.quiz_together.root.model.broadcast.BroadcastStatus.OPEN_ANSWER;
import static me.quiz_together.root.model.broadcast.BroadcastStatus.OPEN_QUESTION;
import static me.quiz_together.root.model.broadcast.BroadcastStatus.OPEN_WINNER;
import static me.quiz_together.root.model.broadcast.BroadcastStatus.WATING;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.quiz_together.root.exceptions.AbusingUserException;
import me.quiz_together.root.exceptions.InaccessiblePermissionException;
import me.quiz_together.root.exceptions.InvalidUpdateException;
import me.quiz_together.root.exceptions.NotMatchException;
import me.quiz_together.root.model.broadcast.Broadcast;
import me.quiz_together.root.model.broadcast.BroadcastStatus;
import me.quiz_together.root.model.user.PlayUserStatus;
import me.quiz_together.root.repository.broadcast.BroadcastRedisRepository;
import me.quiz_together.root.repository.broadcast.BroadcastRepository;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BroadcastService {
    private static final Integer MAX_BROADCAST = 1;
    private final BroadcastRepository broadcastRepository;
    private final BroadcastRedisRepository broadcastRedisRepository;

    public Broadcast getBroadcastById(long broadcastId) {
        return broadcastRepository.selectBroadcastById(broadcastId);
    }

    public List<Broadcast> getPagingBroadcastList(long next, int limit, Long userId) {
        return broadcastRepository.selectPagingBroadcastList(next, limit, userId);
    }

    public List<Broadcast> getMyBroadcastList(Long userId) {
        return broadcastRepository.selectMyBroadcastList(userId);
    }

    public boolean getPreparedBroadcastByUserId(Long userId) {
        return broadcastRepository.selectPreparedBroadcastByUserId(userId) >= MAX_BROADCAST;
    }

    public void insertBroadcast(Broadcast broadcast) {
        broadcastRepository.insertBroadcast(broadcast);
    }

    public int updateBroadcast(Broadcast broadcast) {
        return broadcastRepository.updateBroadcast(broadcast);
    }

    public int updateBroadcastStatus(BroadcastStatus broadcastStatus, long broadcastId) {
        return broadcastRepository.updateBroadcastStatus(broadcastStatus, broadcastId);
    }

    public int deleteBroadcastById(long broadcastId) {
        return broadcastRepository.deleteBroadcastById(broadcastId);
    }

    public Map<Integer, Integer> getQuestionAnswerStat(long broadcastId, int step) {
        Map<String, Integer> questionAnswerStat = broadcastRedisRepository.selectQuestionAnswerStat(broadcastId,
                                                                                                    step);
        return questionAnswerStat.entrySet().stream().collect(Collectors.toMap(e -> Integer.valueOf(e.getKey()), e-> e.getValue()));
    }

    public void incrementQuestionAnswerStat(long broadcastId, int step, int answerNo) {
        broadcastRedisRepository.incrementQuestionAnswerStat(broadcastId, step, answerNo);
    }

    public boolean insertPlayUserAnswer(long broadcastId, long userId, int step, int answerNo) {
        return broadcastRedisRepository.insertPlayUserAnswer(broadcastId, userId, step, answerNo);
    }

    public Set<Long> getPlayUserIds(long broadcastId, int lastStep) {
        return broadcastRedisRepository.selectPlayUserIds(broadcastId, lastStep);
    }

    public void insertPlayUser(long broadcastId, long userId, int step) {
        broadcastRedisRepository.insertPlayUser(broadcastId, step, userId);
    }

    public boolean isPlayUser(long broadcastId, long userId, int step) {
        return broadcastRedisRepository.isPlayUser(broadcastId, step, userId);
    }

    public void insertLoserUser(long broadcastId, long userId, int step) {
        broadcastRedisRepository.insertLoserUser(broadcastId, step, userId);
    }

    public boolean isLoserUser(long broadcastId, long userId, int step) {
        return broadcastRedisRepository.isLoserUser(broadcastId, step, userId);
    }

    public void deleteLoserUser(long broadcastId, int step) {
        broadcastRedisRepository.deleteLoserUser(broadcastId, step);
    }

    public PlayUserStatus getPlayUserStatus(long broadcastId, long userId, int step) {
        // TODO user status 체크
        // 문제가 뜨고 답을 체크 안하고 재접속 하는 유저인 경우
        // 문제가 뜨고 답 체크 후 재접속 하는 유저의 경우
        // 문제가 사라지고 재접속 하는 유저의 경우
        // 이전 문제에서의 user 상태 값을 가져온다
        // TODO 해커톤용으로 생성
        return PlayUserStatus.PLAYER;
//        boolean isPlayUser = broadcastRedisRepository.isPlayUser(broadcastId, step - 1, userId);
//        boolean isLoserUser = broadcastRedisRepository.isLoserUser(broadcastId, step - 1, userId);
//        if (step == 0) {
//            // 문제 시작 전에는 모두 PLAYER상 태
//            // TODO : 만약 늦게 들어오는 경우 어떻게 처리 할지
//            // 일단 탈락 처리
//            broadcastRedisRepository.insertPlayUser(broadcastId, step, userId);
//            return PlayUserStatus.PLAYER;
//        } else if (isPlayUser) {
//            return PlayUserStatus.PLAYER;
//        } else if (isLoserUser) {
//            return PlayUserStatus.LOSER;
//        }
//
//        return PlayUserStatus.VIEWER;
    }


    public void insertBroadcastStep(long broadcastId, int step) {
        broadcastRedisRepository.insertBroadcastStep(broadcastId, step);
    }

    public Long getCurrentBroadcastStep(long broadcastId) {
        return broadcastRedisRepository.getCurrentBroadcastStep(broadcastId);
    }

    public void insertViewer(long broadcastId, long userId) {
        broadcastRedisRepository.insertViewer(broadcastId, userId);
    }

    public void deleteViewer(long broadcastId, long userId) {
        broadcastRedisRepository.deleteViewer(broadcastId, userId);
    }

    public Long getCurrentViewers(long broadcastId) {
        return broadcastRedisRepository.getCurrentViewers(broadcastId);
    }

    public boolean insertUserHeart(long broadcastId, long userId, int step) {
        return broadcastRedisRepository.insertUserHeart(broadcastId, userId, step);
    }

    public void checkPermissionBroadcast(long broadcastId, long userId) {
        Broadcast broadcast = getBroadcastById(broadcastId);
        if (broadcast.getUserId() != userId) {
            log.error("해당 유저는 권한이 없습니다. broadcastId : " + broadcastId + " userId : " + userId + "!!");
            throw new InaccessiblePermissionException();
        }
    }

    public void validCurrentBroadcastStep(long broadcastId, int sendStep) {
        Long currentStep = getCurrentBroadcastStep(broadcastId);
        if (currentStep.intValue() != sendStep) {
            log.error("step 불일치!! currentStep :" + currentStep + " sendStep : " + sendStep);
            throw new NotMatchException("현재 step 불일치!!");
        }
    }

    public void validNextBroadcastStatusAndUpdateBroadcastStatus(BroadcastStatus currentBroadcastStatus, BroadcastStatus nextBroadcastStatus, long broadcastId) {
        validateNextBroadcastStatus(currentBroadcastStatus, nextBroadcastStatus);
        //방송 상태 변경
        updateBroadcastStatus(nextBroadcastStatus, broadcastId);
    }

    public void validateNextBroadcastStatus(BroadcastStatus currentBroadcastStatus, BroadcastStatus nextBroadcastStatus) {
        switch (currentBroadcastStatus) {
            case CREATED:
                if (nextBroadcastStatus == WATING) {
                    return;
                }
                break;
            case WATING:
                if (nextBroadcastStatus == OPEN_ANSWER || nextBroadcastStatus == OPEN_QUESTION || nextBroadcastStatus == OPEN_WINNER) {
                    return;
                }
                break;
            case OPEN_QUESTION:
                if (nextBroadcastStatus == WATING) {
                    return;
                }
                break;
            case OPEN_ANSWER:
                if (nextBroadcastStatus == WATING) {
                    return;
                }
                break;
            case OPEN_WINNER:
                if (nextBroadcastStatus == COMPLETED) {
                    return;
                }
                break;
        }

        throw new InvalidUpdateException("해당 status 변경 할 수 없습니다! currentStatus : " + currentBroadcastStatus.name() + " / nextStatus : " + nextBroadcastStatus.name());
    }

    public void validateBroadcastStatusForAbusingUser(long broadcastId, BroadcastStatus broadcastStatus) {
        Broadcast broadcast = getBroadcastById(broadcastId);
        if (broadcast.getBroadcastStatus() != broadcastStatus) {
            throw new AbusingUserException("abusing user! 강제 정답 제출 시도 broadcast status :" + broadcast.getBroadcastStatus());
        }

    }
}
