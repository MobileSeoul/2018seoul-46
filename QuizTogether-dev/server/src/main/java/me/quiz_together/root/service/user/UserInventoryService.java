package me.quiz_together.root.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import me.quiz_together.root.model.user.UserInventory;
import me.quiz_together.root.repository.user.UserInventoryRepository;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserInventoryService {
    private UserInventoryRepository userInventoryRepository;

    public UserInventory getUserInventoryByUserId(Long userId) {
        return userInventoryRepository.selectUserInventoryByUserId(userId);
    }

    public void insertUserInventory(Long userId) {
        userInventoryRepository.insertUserInventory(userId);
    }

    public int updateUserHeartCount(Long userId, int heartCount) {
        return userInventoryRepository.updateUserHeartCount(userId, heartCount);
    }
}
