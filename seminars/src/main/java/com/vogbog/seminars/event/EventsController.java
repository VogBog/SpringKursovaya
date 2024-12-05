package com.vogbog.seminars.event;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("secured/events")
@AllArgsConstructor
public class EventsController {
    private final EventsService service;

    @GetMapping("/{id}")
    Event getEvent(@PathVariable Long id) {
        return service.getEvent(id);
    }

    @GetMapping
    List<Event> getEvents() {
        return service.getEvents();
    }

    @GetMapping("/byOwner/{id}")
    List<Event> getByOwner(@PathVariable Long id) {
        return service.getByOwner(id);
    }

    @PostMapping
    ResponseEntity<?> addEvent(@Valid @RequestBody Event event, Principal principal) {
        event.setPeoplesCount(0);
        if(service.save(event, principal)) {
            return ResponseEntity.ok("Success");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong");
    }

    @PutMapping
    ResponseEntity<?> editEvent(@Valid @RequestBody Event event, Principal principal) {
        if(service.save(event, principal)) {
            return ResponseEntity.ok("Success");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong");
    }

    @PostMapping("/total")
    List<TotalEvent> total(@Valid @RequestBody List<Event> events) {
        return service.total(events);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteById(@PathVariable Long id, Principal principal) {
        if(service.delete(id, principal)) {
            return ResponseEntity.ok("Success");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't remove");
    }

    @GetMapping("/notOwner/{id}")
    List<Event> findNotOwner(@PathVariable Long id) {
        return service.findNotOwner(id);
    }

    @GetMapping("/notConnected/{id}")
    List<Event> findNotConnected(@PathVariable Long id) {
        return service.findNotConnected(id);
    }

    @GetMapping("/subscribed/{id}")
    List<Event> findSubscribed(@PathVariable Long id) {
        return service.findSubscribed(id);
    }

    @PostMapping("/uploadFromRequest/{id}")
    ResponseEntity<?> uploadFromRequest(@Valid @RequestBody Event event, @PathVariable Long id, Principal principal) {
        if(!service.saveFromRequest(event, id, principal)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't upload. Event must be equals to Request");
        }
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/subscribe/{id}")
    ResponseEntity<?> subscribe(@PathVariable Long id, Principal principal) {
        if(service.subscribe(id, principal)) {
            return ResponseEntity.ok("Success");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't subscribe");
    }

    @DeleteMapping("/unsubscribe")
    ResponseEntity<?> unsubscribe(@RequestParam Long userId, @RequestParam Long eventId, Principal principal) {
        service.unsubscribe(userId, eventId, principal);
        return ResponseEntity.ok("Success");
    }
}
