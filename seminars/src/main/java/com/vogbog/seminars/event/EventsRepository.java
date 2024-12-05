package com.vogbog.seminars.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventsRepository extends JpaRepository<Event, Long> {
    List<Event> findByOwner(Long owner);
    @Query("SELECT e FROM Event e WHERE owner != :owner")
    List<Event> findByNotOwner(Long owner);

    @Query("SELECT e FROM Event e " +
            "LEFT JOIN UserEvent ue ON e.id = ue.eventId " +
            "WHERE ue.userId = :userId AND e.owner != :userId")
    List<Event> findConnected(Long userId);

    List<Event> findByDateTime(LocalDate date);
    List<Event> findByDateTimeAndLocationId(LocalDate date, Long locationId);
}
