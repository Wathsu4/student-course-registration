package com.zdata.student_course_registration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CourseResponse {
    private Integer id;
    private String code;
    private String title;
    private String instructor;
}
