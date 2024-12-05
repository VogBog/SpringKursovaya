package com.vogbog.seminars.event;

import com.vogbog.seminars.locationRequest.LocationRequest;
import com.vogbog.seminars.locationRequest.LocationRequestRepository;
import com.vogbog.seminars.locationRequest.LocationRequestService;
import com.vogbog.seminars.locations.Location;
import com.vogbog.seminars.locations.LocationsRepository;
import com.vogbog.seminars.userEvent.UserEvent;
import com.vogbog.seminars.userEvent.UserEventRepository;
import com.vogbog.seminars.users.UserAccount;
import com.vogbog.seminars.users.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class EventsService {
    private final EventsRepository repository;
    private final UsersRepository usersRepository;
    private final LocationsRepository locationsRepository;
    private final LocationRequestRepository locationRequestRepository;
    private final UserEventRepository userEventRepository;

    private final LocationRequestService locationRequestService;

    public Event getEvent(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public List<Event> getEvents() {
        return repository.findAll();
    }

    public List<TotalEvent> total(List<Event> events) {
        List<TotalEvent> result = new ArrayList<>();
        for(Event event : events) {
            String location = locationsRepository.findById(event.getLocationId()).orElseThrow().getName();
            String owner = usersRepository.findById(event.getOwner()).orElseThrow().getName();
            result.add(new TotalEvent(event, location, owner));
        }
        return result;
    }

    public List<Event> getByOwner(Long ownerId) {
        return repository.findByOwner(ownerId);
    }

    public Boolean save(Event event, Principal principal) {
        UserAccount user = usersRepository.findByLogin(principal.getName()).orElseThrow();
        if(user.getMyRole().value == 0) {
            if(!Objects.equals(event.getOwner(),locationsRepository.findById(event.getLocationId()).orElseThrow().getOwner())) {
                return false;
            }
            if(!principal.getName().equals(usersRepository.findById(event.getOwner()).orElseThrow().getLogin())) {
                return false;
            }
        }

        repository.save(event);
        return true;
    }

    public Boolean delete(Long id, Principal principal) {
        UserAccount user = usersRepository.findByLogin(principal.getName()).orElseThrow();
        if(user.getMyRole().value == 0) {
            if(!usersRepository.findByLogin(principal.getName()).orElseThrow().getId().equals(
                    repository.findById(id).orElseThrow().getOwner()
            )) {
                return false;
            }
        }

        repository.deleteById(id);
        return true;
    }

    public List<Event> findNotOwner(Long ownerId) {
        return repository.findByNotOwner(ownerId);
    }

    public List<Event> findNotConnected(Long userId) {
        List<Event> connected = repository.findConnected(userId);
        List<Event> all = repository.findByNotOwner(userId);
        all.removeAll(connected);
        return all;
    }

    public List<Event> findSubscribed(Long userId) {
        return repository.findConnected(userId);
    }

    public Boolean saveFromRequest(Event event, Long requestId, Principal principal) {
        UserAccount user = usersRepository.findByLogin(principal.getName()).orElseThrow();
        if(!event.getOwner().equals(user.getId())) {
            return false;
        }
        LocationRequest request = locationRequestRepository.findById(requestId).orElseThrow();
        if(!event.getOwner().equals(request.getOwner()) ||
        !event.getStartTime().equals(request.getStartTime()) ||
        !event.getEndTime().equals(request.getEndTime()) ||
        !event.getLocationId().equals(request.getLocationId()) ||
        !event.getDateTime().equals(request.getDateTime())) {
            return false;
        }

        if(!request.getApproved()) {
            return false;
        }

        if(locationRequestService.remove(request.getId())) {
            repository.save(event);
            return true;
        }
        return false;
    }

    public Boolean subscribe(Long id, Principal principal) {
        UserAccount user = usersRepository.findByLogin(principal.getName()).orElseThrow();
        Event event = repository.findById(id).orElseThrow();
        Location location = locationsRepository.findById(event.getLocationId()).orElseThrow();
        if(event.getPeoplesCount() < location.getMaxPeople()) {
            event.setPeoplesCount(event.getPeoplesCount() + 1);
            UserEvent userEvent = new UserEvent(user.getId(), event.getId());
            userEventRepository.save(userEvent);
            repository.save(event);
            return true;
        }
        return false;
    }

    public void unsubscribe(Long userId, Long eventId, Principal principal) {
        UserAccount user = usersRepository.findByLogin(principal.getName()).orElseThrow();
        if(!userId.equals(user.getId())) {
            return;
        }
        UserEvent userEvent = userEventRepository.findByUserIdAndEventId(userId, eventId).orElseThrow();
        Event event = repository.findById(eventId).orElseThrow();
        event.setPeoplesCount(event.getPeoplesCount() - 1);
        userEventRepository.delete(userEvent);
        repository.save(event);
    }
}
