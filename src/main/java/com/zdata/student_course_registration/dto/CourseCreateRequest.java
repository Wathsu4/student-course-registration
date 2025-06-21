package com.zdata.student_course_registration.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CourseCreateRequest {

    @NotBlank(message = "Please provide a course code")
    private String code;

    @NotBlank(message = "Please provide a course title")
    private String title;

    @NotBlank(message = "Please provide an instructor name")
    private String instructor;
}
