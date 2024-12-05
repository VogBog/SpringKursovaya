package com.vogbog.seminars.locations;

import com.vogbog.seminars.event.Event;
import com.vogbog.seminars.event.EventsRepository;
import com.vogbog.seminars.locationRequest.LocationRequest;
import com.vogbog.seminars.locationRequest.LocationRequestRepository;
import com.vogbog.seminars.users.Role;
import com.vogbog.seminars.users.UserAccount;
import com.vogbog.seminars.users.UsersRepository;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@AllArgsConstructor
public class LocationsService {
    private final LocationsRepository repository;
    private final EventsRepository eventsRepository;
    private final LocationRequestRepository locationRequestRepository;
    private final UsersRepository usersRepository;

    public Location getLocationById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public List<Location> getLocations() {
        return repository.findAll();
    }

    public Long getCount() {
        return repository.count();
    }

    public List<Location> findByUser(Long id) {
        return repository.findByOwner(id);
    }

    public boolean save(Location location, Principal principal) {
        UserAccount user = usersRepository.findByLogin(principal.getName()).orElseThrow();
        if(!user.getId().equals(location.getOwner()) && user.getMyRole() == Role.User) {
            return false;
        }
        repository.save(location);
        return true;
    }

    public boolean remove(Long id, Principal principal) {
        UserAccount user = usersRepository.findByLogin(principal.getName()).orElseThrow();
        Location location = repository.findById(id).orElseThrow();
        if(!user.getId().equals(location.getOwner()) && user.getMyRole() == Role.User) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

    public boolean isFreeThisTime(@NotNull Location location,
                                  @NotNull LocalDate date,
                                  @NotNull LocalTime from,
                                  @NotNull LocalTime to) {
        if(location.getOpenTime().isAfter(from) ||
            location.getCloseTime().isBefore(to)) {
            return false;
        }
        List<Event> events = eventsRepository.findByDateTimeAndLocationId(date, location.getId());
        for(Event event : events) {
            if(event.getStartTime().isBefore(from) && event.getEndTime().isAfter(from) ||
            event.getStartTime().isBefore(to) && event.getEndTime().isAfter(to) ||
            event.getStartTime().isAfter(from) && event.getEndTime().isBefore(to)) {
                return false;
            }
        }
        List<LocationRequest> requests = locationRequestRepository.findByLocationIdAndDateTimeAndApproved(
                location.getId(), date, true
        );
        for(LocationRequest request : requests) {
            if(request.getStartTime().isBefore(from) && request.getEndTime().isAfter(from) ||
            request.getStartTime().isBefore(to) && request.getEndTime().isAfter(to) ||
            request.getStartTime().isAfter(from) && request.getEndTime().isBefore(to)) {
                return false;
            }
        }
        return true;
    }
}
