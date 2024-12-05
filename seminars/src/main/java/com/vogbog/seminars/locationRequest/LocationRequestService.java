package com.vogbog.seminars.locationRequest;

import com.vogbog.seminars.locations.Location;
import com.vogbog.seminars.locations.LocationsRepository;
import com.vogbog.seminars.locations.LocationsService;
import com.vogbog.seminars.users.Role;
import com.vogbog.seminars.users.UserAccount;
import com.vogbog.seminars.users.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Permission;
import java.security.Principal;
import java.util.List;

@Service
@AllArgsConstructor
public class LocationRequestService {
    private final LocationRequestRepository repository;
    private final UsersRepository usersRepository;
    private final LocationsRepository locationsRepository;

    private final LocationsService locationsService;

    public Boolean remove(Long id) {
        if(repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public Boolean remove(Long id, Principal principal) {
        LocationRequest request = repository.findById(id).orElseThrow();
        UserAccount user = usersRepository.findByLogin(principal.getName()).orElseThrow();
        if(!request.getOwner().equals(user.getId()) && user.getMyRole() == Role.User) {
            return false;
        }
        return remove(id);
    }

    public boolean trySendRequest(LocationRequest request, Principal principal) {
        request.setApproved(false);
        if(!usersRepository.findById(request.getOwner()).orElseThrow().getLogin().equals(principal.getName())) {
            return false;
        }
        Location location = locationsRepository.findById(request.getLocationId()).orElseThrow();
        if(request.getStartTime().isBefore(location.getOpenTime()) ||
            request.getEndTime().isAfter(location.getCloseTime()) ||
            request.getOwner().equals(location.getOwner())) {
            return false;
        }
        if(locationsService.isFreeThisTime(
                location,
                request.getDateTime(),
                request.getStartTime(),
                request.getEndTime()))
        {
            repository.save(request);
            return true;
        }
        return false;
    }

    public Boolean approveRequest(Long id, Principal principal) {
        LocationRequest request = repository.findById(id).orElseThrow();
        Location location = locationsRepository.findById(request.getLocationId()).orElseThrow();
        if(!usersRepository.findById(location.getOwner()).orElseThrow().getLogin().equals(principal.getName())) {
            return false;
        }
        if(!locationsService.isFreeThisTime(location, request.getDateTime(), request.getStartTime(), request.getEndTime())) {
            return false;
        }
        if(!repository.existsById(request.getId())) {
            return false;
        }
        repository.deleteById(request.getId());
        request.setApproved(true);
        repository.save(request);
        return true;
    }

    public Boolean denyRequest(Long id, Principal principal) {
        LocationRequest request = repository.findById(id).orElseThrow();
        Location location = locationsRepository.findById(request.getLocationId()).orElseThrow();
        if(!usersRepository.findById(location.getOwner()).orElseThrow().getLogin().equals(principal.getName())) {
            return false;
        }
        if(!repository.existsById(request.getId())) {
            return false;
        }
        repository.deleteById(request.getId());
        return true;
    }

    public List<LocationRequest> getMyRequests(boolean approved, Principal principal) {
        if(principal == null) {
            return null;
        }
        UserAccount user = usersRepository.findByLogin(principal.getName()).orElseThrow();
        return repository.findByOwnerAndApproved(user.getId(), approved);
    }

    public List<LocationRequest> getAllMyRequests(Principal principal) {
        List<LocationRequest> left = getMyRequests(true, principal);
        List<LocationRequest> right = getMyRequests(false, principal);
        if(left != null && right != null) {
            left.addAll(right);
            return left;
        }
        else if(left != null) {
            return left;
        }
        else {
            return right;
        }
    }

    public List<LocationRequest> getRequestsToApprove(Principal principal) {
        if(principal == null) {
            return null;
        }
        UserAccount user = usersRepository.findByLogin(principal.getName()).orElseThrow();
        return repository.findByLocationOwnerAndApproved(user.getId(), false);
    }

    public LocationRequest get(Long id, Principal principal) {
        UserAccount user = usersRepository.findByLogin(principal.getName()).orElseThrow();
        LocationRequest request = repository.findById(id).orElseThrow();
        if(!user.getId().equals(request.getOwner()) && user.getMyRole() == Role.User) {
            return null;
        }
        return request;
    }
}
