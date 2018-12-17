package me.quiz_together.root.model.firebase;

import lombok.Data;

@Data
public class FcmResponse implements FcmMessage {
    private String from;
    private String message_id;
    private String message_type;
    private String error;
    private String error_description;
}
