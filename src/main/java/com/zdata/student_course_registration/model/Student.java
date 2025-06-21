package com.zdata.student_course_registration.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;
@Data // Lombok will generate getters, setters, toString, equals, and hashCode methods
@NoArgsConstructor // Lombok will generate a no-argument constructor
@AllArgsConstructor // Lombok will generate a constructor with all fields

public class Student {
    private Integer id;
    private String name;
    private String email;

}
