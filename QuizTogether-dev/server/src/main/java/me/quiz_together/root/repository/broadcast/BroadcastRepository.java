package me.quiz_together.root.repository.broadcast;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import me.quiz_together.root.model.broadcast.Broadcast;
import me.quiz_together.root.model.broadcast.BroadcastStatus;

@Repository
public class BroadcastRepository {
    @Autowired
    private BroadcastMapper broadcastMapper;

    public Broadcast selectBroadcastById(long broadcastId) {
        return broadcastMapper.selectBroadcastById(broadcastId);
    }

    public List<Broadcast> selectPagingBroadcastList(long next, int limit, Long userId) {
        return broadcastMapper.selectPagingBroadcastList(next, limit, userId);
    }

    public List<Broadcast> selectMyBroadcastList(Long userId) {
        return broadcastMapper.selectMyBroadcastList(userId);
    }

    public int selectPreparedBroadcastByUserId(Long userId) {
        return broadcastMapper.selectPreparedBroadcastByUserId(userId);
    }

    public void insertBroadcast(Broadcast broadcast) {
        broadcastMapper.insertBroadcast(broadcast);
    }

    public int updateBroadcast(Broadcast broadcast) {
        return broadcastMapper.updateBroadcast(broadcast);
    }

    public int updateBroadcastStatus(BroadcastStatus broadcastStatus, long broadcastId) {
        return broadcastMapper.updateBroadcastStatus(broadcastStatus, broadcastId);
    }

    public int deleteBroadcastById(long broadcastId) {
        return broadcastMapper.deleteBroadcastById(broadcastId);
    }
}
