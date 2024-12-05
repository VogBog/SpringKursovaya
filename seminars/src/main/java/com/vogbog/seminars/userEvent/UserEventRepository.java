package com.vogbog.seminars.userEvent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEventRepository extends JpaRepository<UserEvent, Long> {
    Optional<UserEvent> findByUserIdAndEventId(Long userId, Long eventId);
}
