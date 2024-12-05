package com.vogbog.seminars.locationRequest;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/secured/locationRequest/")
@AllArgsConstructor
public class LocationRequestController{
    private final LocationRequestService service;

    @PostMapping
    ResponseEntity<?> trySendRequest(@Valid @RequestBody LocationRequest request, Principal principal) {
        if(service.trySendRequest(request, principal))
            return ResponseEntity.ok("Success");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't send this request");
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> tryRemoveRequest(@PathVariable Long id, Principal principal) {
        if(service.remove(id, principal)) {
            return ResponseEntity.ok("Success");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Permission denied");
    }

    @PostMapping("/approve/{id}")
    ResponseEntity<?> approveRequest(@PathVariable Long id, Principal principal) {
        if(!service.approveRequest(id, principal)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't approve");
        }
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/deny/{id}")
    ResponseEntity<?> denyRequest(@PathVariable Long id, Principal principal) {
        if(!service.denyRequest(id, principal)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't deny");
        }
        return ResponseEntity.ok("Success");
    }

    @GetMapping("/{id}")
    LocationRequest get(@PathVariable Long id, Principal principal) {
        return service.get(id, principal);
    }

    @GetMapping("/ofUserAwait")
    List<LocationRequest> getMyRequests(Principal principal) {
        return service.getMyRequests(false, principal);
    }

    @GetMapping("/ofUserApproved")
    List<LocationRequest> getMyRequestsApproved(Principal principal) {
        return service.getMyRequests(true, principal);
    }

    @GetMapping("/ofUserAll")
    List<LocationRequest> getMyRequestsAll(Principal principal) {
        return service.getAllMyRequests(principal);
    }

    @GetMapping("/toMeForApprove")
    List<LocationRequest> getRequestsToApprove(Principal principal) {
        return service.getRequestsToApprove(principal);
    }
}
