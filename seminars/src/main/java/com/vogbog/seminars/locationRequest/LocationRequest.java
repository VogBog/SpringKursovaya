package com.vogbog.seminars.locationRequest;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "location_request")
@Data
public class LocationRequest {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column private Long locationId;
    @Column private LocalDate dateTime;
    @Column private LocalTime startTime;
    @Column private LocalTime endTime;
    @Column private Long owner;
    @Column private Boolean approved;
}
