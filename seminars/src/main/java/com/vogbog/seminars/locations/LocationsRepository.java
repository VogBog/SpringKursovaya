package com.vogbog.seminars.locations;

import com.vogbog.seminars.users.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationsRepository extends JpaRepository<Location, Long> {
    List<Location> findByOwner(Long owner);
}
