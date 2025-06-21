package com.zdata.student_course_registration.controller;

import com.zdata.student_course_registration.dto.CourseCreateRequest;
import com.zdata.student_course_registration.dto.CourseResponse;
import com.zdata.student_course_registration.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    /**
     * POST /courses – Add a new course
     * @param request CourseCreateRequest DTO
     * @return ResponseEntity with CourseResponse and HTTP Status 201 (Created)
     */
    @PostMapping
    public ResponseEntity<CourseResponse> addCourse(@Valid @RequestBody CourseCreateRequest request) {
        CourseResponse course = courseService.addCourse(request);
        return new ResponseEntity<>(course, HttpStatus.CREATED);
    }

    /**
     * GET /courses – List all courses
     * @return ResponseEntity with a list of CourseResponse and HTTP Status 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<CourseResponse>> listAllCourses() {
        List<CourseResponse> courses = courseService.listAllCourses();
        return ResponseEntity.ok(courses);
    }
}
