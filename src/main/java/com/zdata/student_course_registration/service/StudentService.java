package com.zdata.student_course_registration.service;

import com.zdata.student_course_registration.dto.StudentCreateRequest;
import com.zdata.student_course_registration.dto.StudentResponse;
import com.zdata.student_course_registration.exception.ConflictException;
import com.zdata.student_course_registration.exception.ResourceNotFoundException;
import com.zdata.student_course_registration.model.Student;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class StudentService {
    // Key: Student ID, Value: Student object
    private final Map<Integer, Student> students = new ConcurrentHashMap<>();
    // For generating unique student IDs
    private final AtomicInteger studentIdCounter = new AtomicInteger(1);

    /**
     * Registers a new student.
     * Throws ConflictException if a student with the same email already exists.
     * @param request Student creation details.
     * @return StudentResponse of the newly registered student.
     */

    public StudentResponse registerStudent(StudentCreateRequest request) {
        // Business Rule 2: email must be unique for each student.
        boolean emailExists = students.values().stream()
                .anyMatch(student -> student.getEmail().equalsIgnoreCase(request.getEmail()));
        if (emailExists) {
            throw new ConflictException("Student with email '" + request.getEmail() + "' already exists.");
        }

        Integer newId = studentIdCounter.getAndIncrement();
        Student student = new Student(newId, request.getName(), request.getEmail());
        students.put(newId, student);
        return new StudentResponse(student.getId(), student.getName(), student.getEmail());
    }

    /**
     * Retrieves a student by ID.
     * Throws ResourceNotFoundException if the student is not found.
     * @param studentId ID of the student.
     * @return Student object.
     */

    public Student getStudentById(Integer studentId) {
        return students.get(studentId);
    }

    /**
     * Retrieves all registered students.
     * @return List of StudentResponse objects.
     */
    public Map<Integer, Student> getAllStudents() {
        // Returns a copy of the map to prevent external modification
        return new ConcurrentHashMap<>(students);
    }

    // You can add more helper methods here if needed, e.g., to check student existence by ID
    public boolean studentExists(Integer studentId) {
        return students.containsKey(studentId);
    }

}
