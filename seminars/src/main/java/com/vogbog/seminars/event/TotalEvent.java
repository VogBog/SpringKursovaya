package com.vogbog.seminars.event;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class TotalEvent implements Serializable {
    private Event event;
    private String location;
    private String owner;
}
