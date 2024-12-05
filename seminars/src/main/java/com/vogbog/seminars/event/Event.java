package com.vogbog.seminars.event;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name="event")
@Data
public class Event {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull @Column private String name;
    @NotNull @Column private LocalDate dateTime;
    @Column private LocalTime startTime;
    @Column private LocalTime endTime;
    @NotNull @Column private Integer ticketCost;
    @Column private Integer peoplesCount;
    @NotNull @Column private Long locationId;
    @NotNull @Column private Long owner;
}
