package com.iba.iot.datasimulator.session.model.active.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ActiveSessionPayloadMessage implements ActiveSessionMessage {

    /** **/
    private final MessageType type = MessageType.SESSION_PAYLOAD;

    @NotEmpty
    private String sessionId;

    @NotEmpty
    private String message;

    @NotEmpty
    private long timestamp;
}
