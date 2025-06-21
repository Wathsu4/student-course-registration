package com.zdata.student_course_registration.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Course {
    private Integer id;
    private String code;
    private String title;
    private String instructor;


}
