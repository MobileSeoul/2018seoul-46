package me.quiz_together.root.model.response.user;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserFollowerViewList {
    private List<UserFollowerView> userFollowerList;
}
