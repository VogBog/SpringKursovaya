package com.vogbog.seminars.locationRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LocationRequestRepository extends JpaRepository<LocationRequest, Long> {
    List<LocationRequest> findByOwnerAndApproved(Long id, Boolean approved);
    @Query("SELECT lr FROM LocationRequest lr " +
            "LEFT JOIN Location l ON lr.locationId = l.id " +
            "WHERE l.owner = :id AND lr.approved = :approved")
    List<LocationRequest> findByLocationOwnerAndApproved(Long id, Boolean approved);

    List<LocationRequest> findByLocationIdAndDateTimeAndApproved(Long id, LocalDate dateTime, Boolean approved);
}
