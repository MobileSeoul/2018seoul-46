package me.quiz_together.root.model.response.broadcast;

import java.util.List;

import lombok.Data;

@Data
public class PagingBroadcastListView {
    private List<BroadcastView> myBroadcastList;
    private List<BroadcastView> currentBroadcastList;
}
