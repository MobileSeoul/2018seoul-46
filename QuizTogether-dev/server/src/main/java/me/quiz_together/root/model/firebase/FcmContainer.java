package me.quiz_together.root.model.firebase;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FcmContainer {
    private String to;
    private FcmMessage data;
}
