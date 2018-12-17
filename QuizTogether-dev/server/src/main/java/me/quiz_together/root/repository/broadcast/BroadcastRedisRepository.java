package me.quiz_together.root.repository.broadcast;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import me.quiz_together.root.support.RedisKeyFormatter;

@Repository
public class BroadcastRedisRepository {
    private static final String STEP_KEY = "step:";
    private static final String HEART_KEY = "heart";
    @Autowired
    private RedisTemplate<String, Long> longRedisTemplate;
    @Autowired
    private RedisTemplate<String, Integer> integerRedisTemplate;

    /////////////////////////
    // currentViewers
    /////////////////////////
    public void insertViewer(long broadcastId, long userId) {
        longRedisTemplate.opsForSet().add(RedisKeyFormatter.getCurrentViewers(broadcastId), userId);
    }

    public void deleteViewer(long broadcastId, long userId) {
        longRedisTemplate.opsForSet().remove(RedisKeyFormatter.getCurrentBroadcastStep(broadcastId), userId);
    }

    public Long getCurrentViewers(long broadcastId) {
        return longRedisTemplate.opsForSet().size(RedisKeyFormatter.getCurrentViewers(broadcastId));
    }

    public void deleteViewers(long broadcastId) {
        longRedisTemplate.delete(RedisKeyFormatter.getCurrentViewers(broadcastId));
    }

    /////////////////////////
    // playUser
    /////////////////////////
    public void insertPlayUser(long broadcastId, int step, long userId) {
        longRedisTemplate.opsForSet().add(RedisKeyFormatter.getPlayUser(broadcastId, step), userId);
    }

    public boolean isPlayUser(long broadcastId, int step, long userId) {
        return longRedisTemplate.opsForSet().isMember(RedisKeyFormatter.getPlayUser(broadcastId, step), userId);
    }

    public Set<Long> selectPlayUserIds(long broadcastId, int step) {
        return longRedisTemplate.opsForSet().members(RedisKeyFormatter.getPlayUser(broadcastId, step));
    }

    public void deletePlayUser(long broadcastId, int step) {
        longRedisTemplate.delete(RedisKeyFormatter.getPlayUser(broadcastId, step));
    }

    /////////////////////////
    // LoserUser
    /////////////////////////
    public void insertLoserUser(long broadcastId, int step, long userId) {
        longRedisTemplate.opsForSet().add(RedisKeyFormatter.getLoserUser(broadcastId, step), userId);
    }

    public boolean isLoserUser(long broadcastId, int step, long userId) {
        return longRedisTemplate.opsForSet().isMember(RedisKeyFormatter.getLoserUser(broadcastId, step), userId);
    }

    public Set<Long> selectLoserUserIds(long broadcastId, int step) {
        return longRedisTemplate.opsForSet().members(RedisKeyFormatter.getLoserUser(broadcastId, step));
    }

    public void deleteLoserUser(long broadcastId, int step) {
        longRedisTemplate.delete(RedisKeyFormatter.getLoserUser(broadcastId, step));
    }

    /////////////////////////
    // playUserAnswer
    /////////////////////////
    public boolean insertPlayUserAnswer(long broadcastId, long userId, int step, int answerNo) {
        return integerRedisTemplate.opsForHash().putIfAbsent(RedisKeyFormatter.getPlayUserAnswer(broadcastId, userId), HEART_KEY
                                                                                                        + step,
                                              answerNo);
    }

    public Integer selectPlayUserAnswer(long broadcastId, long userId, int step) {
        return (Integer) integerRedisTemplate.opsForHash().get(RedisKeyFormatter.getPlayUserAnswer(broadcastId, userId),
                                              STEP_KEY + step);
    }

    public void deletePlayUserAnswer(long broadcastId, long userId) {
        integerRedisTemplate.delete(RedisKeyFormatter.getPlayUserAnswer(broadcastId, userId));
    }

    /////////////////////////
    // UserHeart
    /////////////////////////
    public boolean insertUserHeart(long broadcastId, long userId, int step) {
        return integerRedisTemplate.opsForHash().putIfAbsent(RedisKeyFormatter.getUserHeart(broadcastId, userId), HEART_KEY, step);
    }

    public Integer selectUserHeart(long broadcastId, long userId) {
        return (Integer) integerRedisTemplate.opsForHash().get(RedisKeyFormatter.getUserHeart(broadcastId, userId), HEART_KEY);
    }

    public void deleteUserHeart(long broadcastId, long userId) {
        integerRedisTemplate.delete(RedisKeyFormatter.getUserHeart(broadcastId, userId));
    }


    /////////////////////////
    // questionAnswerStat
    /////////////////////////

    public void incrementQuestionAnswerStat(long broadcastId, int step, int answerNo) {
        integerRedisTemplate.opsForHash().increment(RedisKeyFormatter.getQuestionAnswerStat(broadcastId, step), String.valueOf(answerNo), 1);
    }

    public Map selectQuestionAnswerStat(long broadcastId, int step) {
        return integerRedisTemplate.opsForHash().entries(RedisKeyFormatter.getQuestionAnswerStat(broadcastId, step));
    }

    public void deleteQuestionAnswerStat(long broadcastId, int step) {
        integerRedisTemplate.delete(RedisKeyFormatter.getQuestionAnswerStat(broadcastId, step));
    }

    /////////////////////////
    // broadcastStep
    /////////////////////////

    public void insertBroadcastStep(long broadcastId, int step) {
        integerRedisTemplate.opsForSet().add(RedisKeyFormatter.getCurrentBroadcastStep(broadcastId), step);
    }

    public boolean isBroadcastStep(long broadcastId, int step) {
        return integerRedisTemplate.opsForSet().isMember(RedisKeyFormatter.getCurrentBroadcastStep(broadcastId), step);
    }

    public Long getCurrentBroadcastStep(long broadcastId) {
        // 해당 key에 들어가 있는 value의 갯수
        return integerRedisTemplate.opsForSet().size(RedisKeyFormatter.getCurrentBroadcastStep(broadcastId));
    }

    public void deleteBroadcastStep(long broadcastId) {
        integerRedisTemplate.delete(RedisKeyFormatter.getCurrentBroadcastStep(broadcastId));
    }

}
