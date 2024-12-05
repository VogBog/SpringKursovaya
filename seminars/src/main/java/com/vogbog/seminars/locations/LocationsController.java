package com.vogbog.seminars.locations;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("secured/locations")
@AllArgsConstructor
public class LocationsController {
    private final LocationsService service;

    @GetMapping("/{id}")
    Location getLocationById(@PathVariable Long id) {
        return service.getLocationById(id);
    }

    @GetMapping("")
    List<Location> getLocations() {
        return service.getLocations();
    }

    @GetMapping("/count")
    Long getCount() {
        return service.getCount();
    }

    @GetMapping("/ofUser/{id}")
    List<Location> findByUser(@PathVariable Long id) {
        return service.findByUser(id);
    }

    @PostMapping("/add")
    ResponseEntity<?> addLocation(@Valid @RequestBody Location location, Principal principal) {
        if(service.save(location, principal)) {
            return ResponseEntity.ok("Success");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Permission denied");
    }

    @PutMapping("/edit")
    ResponseEntity<?> editLocation(@Valid @RequestBody Location location, Principal principal) {
        if(service.save(location, principal)) {
            return ResponseEntity.ok().body("Success");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Permission denied");
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<?> removeLocation(@PathVariable Long id, Principal principal) {
        if(service.remove(id, principal)) {
            return ResponseEntity.ok("Success");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Permission denied");
    }
}
