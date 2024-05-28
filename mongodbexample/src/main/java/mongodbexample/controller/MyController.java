package mongodbexample.controller;

import mongodbexample.models.student;
import mongodbexample.repo.studentRepository;
import mongodbexample.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/student")
public class MyController {

    private static final Logger logger = LoggerFactory.getLogger(MyController.class);
    private final studentRepository studentRepository;

    @Autowired
    public MyController(studentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @PostMapping("/addstudent")
    public ResponseEntity<ApiResponse<student>> addstudent(@RequestBody student student1) {
        logger.info("Received request to add student: {}", student1);
        try {
            student savedStudent = this.studentRepository.save(student1);
            logger.info("Student saved successfully: {}", savedStudent);
            ApiResponse<student> response = new ApiResponse<>(HttpStatus.OK.value(), "Student saved successfully", savedStudent, null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while saving student: {}", student1, e);
            ApiResponse<student> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred", null, e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getstudents")
    public ResponseEntity<ApiResponse<Iterable<student>>> getstudents() {
        logger.info("Received request to get all students");
        try {
            Iterable<student> students = this.studentRepository.findAll();
            logger.info("Students retrieved successfully");
            ApiResponse<Iterable<student>> response = new ApiResponse<>(HttpStatus.OK.value(), "Students retrieved successfully", students, null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while retrieving students", e);
            ApiResponse<Iterable<student>> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred", null, e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Newly added endpoints
    @GetMapping("/getstudent/{id}")
    public ResponseEntity<ApiResponse<student>> getStudentById(@PathVariable int id) {
        logger.info("Received request to get student by ID: {}", id);
        try {
            Optional<student> optionalStudent = this.studentRepository.findById(id());
            if (optionalStudent.isPresent()) {
                student student = optionalStudent.get();
                logger.info("Student retrieved successfully: {}", student);
                ApiResponse<student> response = new ApiResponse<>(HttpStatus.OK.value(), "Student retrieved successfully", student, null);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                logger.info("Student with ID {} not found", id);
                ApiResponse<student> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Student not found", null, null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Error occurred while retrieving student with ID: {}", id, e);
            ApiResponse<student> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred", null, e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private int id() {
        return 0;
    }

    @PutMapping("/updatestudent/{id}")
    public ResponseEntity<ApiResponse<student>> updateStudent(@PathVariable String id, @RequestBody student student1) {
        logger.info("Received request to update student with ID {}: {}", id, student1);
        try {
            Optional<student> optionalStudent = this.studentRepository.findById(getStudentById(student1.getId()));
            if (optionalStudent.isPresent()) {
                student existingStudent = optionalStudent.get();
                student1.setId(existingStudent.getId()); // Ensure the ID from the URL is set in the student object
                student updatedStudent = this.studentRepository.save(student1);
                logger.info("Student updated successfully: {}", updatedStudent);
                ApiResponse<student> response = new ApiResponse<>(HttpStatus.OK.value(), "Student updated successfully", updatedStudent, null);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                logger.info("Student with ID {} not found", id);
                ApiResponse<student> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Student not found", null, null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Error occurred while updating student with ID: {}", id, e);
            ApiResponse<student> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred", null, e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deletestudent/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable String id) {
        logger.info("Received request to delete student with ID: {}", id);
        try {
            this.studentRepository.deleteById(Integer.parseInt(id));
            logger.info("Student deleted successfully with ID: {}", id);
            ApiResponse<Void> response = new ApiResponse<>(HttpStatus.OK.value(), "Student deleted successfully", null, null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while deleting student with ID: {}", id, e);
            ApiResponse<Void> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred", null, e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
