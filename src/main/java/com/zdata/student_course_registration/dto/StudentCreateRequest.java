package com.zdata.student_course_registration.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class StudentCreateRequest {
    @NotBlank(message = "Student name cannot be blank")
    private String name;

    @NotBlank(message = "Student email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;


}
