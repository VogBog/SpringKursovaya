package com.vogbog.seminars.locations;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalTime;

@Entity
@Table(name="location")
@Data
public class Location {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column private String name;
    @Column private LocalTime openTime;
    @Column private LocalTime closeTime;
    @Column private Integer maxPeople;
    @Column private Long owner;
}