package com.zdata.student_course_registration.controller;


import com.zdata.student_course_registration.dto.CourseResponse;
import com.zdata.student_course_registration.dto.StudentCreateRequest;
import com.zdata.student_course_registration.dto.StudentResponse;
import com.zdata.student_course_registration.model.Course;
import com.zdata.student_course_registration.model.Registration;
import com.zdata.student_course_registration.service.RegistrationService;
import com.zdata.student_course_registration.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private final RegistrationService registrationService;

    public StudentController(StudentService studentService, RegistrationService registrationService) {
        this.studentService = studentService;
        this.registrationService = registrationService;
    }

    /**
     * POST /students – Register a new student
     * @param request StudentCreateRequest DTO
     * @return ResponseEntity with StudentResponse and HTTP Status 201 (Created)
     */
    @PostMapping
    public ResponseEntity<StudentResponse> registerStudent(@Valid @RequestBody StudentCreateRequest request) {
        StudentResponse student = studentService.registerStudent(request);
        return new ResponseEntity<>(student, HttpStatus.CREATED);
    }

    /**
     * POST /students/{studentId}/register/{courseId} – Register for a course
     * @param studentId Path variable for student ID
     * @param courseId Path variable for course ID
     * @return ResponseEntity with Registration object and HTTP Status 200 (OK)
     */
    @PostMapping("/{studentId}/register/{courseId}")
    public ResponseEntity<Registration> registerForCourse(
            @PathVariable Integer studentId,
            @PathVariable Integer courseId) {
        Registration registration = registrationService.registerForCourse(studentId, courseId);
        return ResponseEntity.ok(registration);
    }

    /**
     * DELETE /students/{studentId}/drop/{courseId} – Drop a course
     * @param studentId Path variable for student ID
     * @param courseId Path variable for course ID
     * @return ResponseEntity with HTTP Status 204 (No Content)
     */
    @DeleteMapping("/{studentId}/drop/{courseId}")
    public ResponseEntity<Void> dropCourse(
            @PathVariable Integer studentId,
            @PathVariable Integer courseId) {
        registrationService.dropCourse(studentId, courseId);
        return ResponseEntity.noContent().build(); // 204 No Content for successful deletion
    }

    /**
     * GET /students/{studentId}/courses – List registered courses
     * @param studentId Path variable for student ID
     * @return ResponseEntity with a list of CourseResponse and HTTP Status 200 (OK)
     */
    @GetMapping("/{studentId}/courses")
    public ResponseEntity<List<CourseResponse>> getRegisteredCourses(@PathVariable Integer studentId) {
        List<Course> registeredCourses = registrationService.getRegisteredCourses(studentId);
        List<CourseResponse> courseResponses = registeredCourses.stream()
                .map(course -> new CourseResponse(course.getId(), course.getCode(), course.getTitle(), course.getInstructor()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(courseResponses);
    }
}
