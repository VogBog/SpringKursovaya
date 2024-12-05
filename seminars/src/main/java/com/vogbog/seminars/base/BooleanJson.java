package com.vogbog.seminars.base;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class BooleanJson implements Serializable {
    private Boolean value;
}
