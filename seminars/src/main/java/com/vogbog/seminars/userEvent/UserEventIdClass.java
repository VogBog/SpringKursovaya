package com.vogbog.seminars.userEvent;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserEventIdClass implements Serializable {
    private Long userId;
    private Long eventId;
}
