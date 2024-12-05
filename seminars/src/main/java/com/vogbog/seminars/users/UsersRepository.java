package com.vogbog.seminars.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByLogin(String login);

    Boolean existsByLogin(String login);

    @Query("SELECT u FROM UserAccount u " +
            "LEFT JOIN UserEvent ue ON u.id = ue.userId " +
            "WHERE ue.eventId = :eventId")
    List<UserAccount> findByEventId(Long eventId);
}
