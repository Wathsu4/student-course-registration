package com.zdata.student_course_registration.service;

import com.zdata.student_course_registration.exception.BadRequestException;
import com.zdata.student_course_registration.exception.ConflictException;
import com.zdata.student_course_registration.exception.ResourceNotFoundException;
import com.zdata.student_course_registration.model.Course;
import com.zdata.student_course_registration.model.Registration;
import com.zdata.student_course_registration.model.Student;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class RegistrationService {

    private final StudentService studentService;
    private final CourseService courseService;

    // In-memory storage for registrations
    // Key: studentId, Value: Set of Course IDs the student is registered for
    private final Map<Integer, Set<Integer>> studentRegistrations = new ConcurrentHashMap<>();

    public RegistrationService(StudentService studentService, CourseService courseService) {
        this.studentService = studentService;
        this.courseService = courseService;
    }

    /**
     * Registers a student for a course.
     * Business Rules:
     * 3. A student cannot register for the same course more than once.
     * 4. A student can drop only courses they are registered in (checked by dropCourse).
     * @param studentId ID of the student.
     * @param courseId ID of the course.
     * @return true if registration is successful.
     * @throws ResourceNotFoundException if student or course is not found.
     * @throws ConflictException if student is already registered for the course.
     */
    public Registration registerForCourse(Integer studentId, Integer courseId) {
        Student student = studentService.getStudentById(studentId);
        if (student == null) {
            throw new ResourceNotFoundException("Student with ID " + studentId + " not found.");
        }

        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            throw new ResourceNotFoundException("Course with ID " + courseId + " not found.");
        }

        // Business Rule 3: A student cannot register for the same course more than once.
        Set<Integer> registeredCourses = studentRegistrations.computeIfAbsent(studentId, k -> ConcurrentHashMap.newKeySet());
        if (!registeredCourses.add(courseId)) {
            throw new ConflictException("Student " + studentId + " is already registered for course " + courseId + ".");
        }

        // Return a Registration object for clarity, though not strictly stored as objects in this service
        return new Registration(studentId, courseId, LocalDateTime.now());
    }

    /**
     * Drops a student from a course.
     * Business Rule 4: A student can drop only courses they are registered in.
     * @param studentId ID of the student.
     * @param courseId ID of the course.
     * @return true if course was successfully dropped.
     * @throws ResourceNotFoundException if student or course is not found.
     * @throws BadRequestException if student is not registered for the course.
     */
    public boolean dropCourse(Integer studentId, Integer courseId) {
        Student student = studentService.getStudentById(studentId);
        if (student == null) {
            throw new ResourceNotFoundException("Student with ID " + studentId + " not found.");
        }

        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            throw new ResourceNotFoundException("Course with ID " + courseId + " not found.");
        }

        Set<Integer> registeredCourses = studentRegistrations.get(studentId);
        if (registeredCourses == null || !registeredCourses.remove(courseId)) {
            // Business Rule 4: A student can drop only courses they are registered in.
            throw new BadRequestException("Student " + studentId + " is not registered for course " + courseId + ".");
        }
        return true;
    }

    /**
     * Lists all courses a student is registered for.
     * @param studentId ID of the student.
     * @return List of CourseResponse objects.
     * @throws ResourceNotFoundException if student is not found.
     */
    public List<Course> getRegisteredCourses(Integer studentId) {
        Student student = studentService.getStudentById(studentId);
        if (student == null) {
            throw new ResourceNotFoundException("Student with ID " + studentId + " not found.");
        }

        Set<Integer> registeredCourseIds = studentRegistrations.getOrDefault(studentId, ConcurrentHashMap.newKeySet());

        return registeredCourseIds.stream()
                .map(courseService::getCourseById)
                .filter(java.util.Objects::nonNull) // Filter out any courses that might have been deleted (edge case)
                .collect(Collectors.toList());
    }
}
