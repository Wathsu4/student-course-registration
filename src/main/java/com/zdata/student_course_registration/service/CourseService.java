package com.zdata.student_course_registration.service;

import com.zdata.student_course_registration.dto.CourseCreateRequest;
import com.zdata.student_course_registration.exception.ConflictException;
import com.zdata.student_course_registration.exception.ResourceNotFoundException;
import com.zdata.student_course_registration.dto.CourseResponse;
import com.zdata.student_course_registration.model.Course;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Service
public class CourseService {

    // In-memory storage for courses
    // Key: Course ID, Value: Course object
    private final Map<Integer, Course> courses = new ConcurrentHashMap<>();
    // For generating unique course IDs
    private final AtomicInteger courseIdCounter = new AtomicInteger(1);

    /**
     * Adds a new course.
     * Throws ConflictException if a course with the same code already exists.
     * @param request Course creation details.
     * @return CourseResponse of the newly added course.
     */
    public CourseResponse addCourse(CourseCreateRequest request) {
        // Business Rule 1: code must be unique for each course.
        boolean codeExists = courses.values().stream()
                .anyMatch(course -> course.getCode().equalsIgnoreCase(request.getCode()));
        if (codeExists) {
            throw new ConflictException("Course with code '" + request.getCode() + "' already exists.");
        }

        Integer newId = courseIdCounter.getAndIncrement();
        Course course = new Course(newId, request.getCode(), request.getTitle(), request.getInstructor());
        courses.put(newId, course);
        return new CourseResponse(course.getId(), course.getCode(), course.getTitle(), course.getInstructor());
    }

    /**
     * Retrieves a course by ID.
     * Throws ResourceNotFoundException if the course is not found.
     * @param courseId ID of the course.
     * @return Course object.
     */
    public Course getCourseById(Integer courseId) {
        return courses.get(courseId);
    }

    /**
     * Lists all available courses.
     * @return List of CourseResponse objects.
     */
    public List<CourseResponse> listAllCourses() {
        return courses.values().stream()
                .map(course -> new CourseResponse(course.getId(), course.getCode(), course.getTitle(), course.getInstructor()))
                .collect(Collectors.toList());
    }


    public boolean courseExists(Integer courseId) {
        return courses.containsKey(courseId);
    }
}