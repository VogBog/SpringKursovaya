package com.vogbog.seminars.userEvent;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="user_event")
@IdClass(UserEventIdClass.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEvent {
    @Id private Long userId;
    @Id private Long eventId;
}
