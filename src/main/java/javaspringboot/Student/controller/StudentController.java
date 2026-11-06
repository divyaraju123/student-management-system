
package javaspringboot.Student.controller;

import javaspringboot.Student.dto.StudentRequest;
import javaspringboot.Student.model.Student;
import javaspringboot.Student.service.StudentService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    // Constructor injection
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // GET: Get all students with Pagination and Sorting
    @GetMapping
    public ResponseEntity<Page<Student>> getAllStudents(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "id") String sortBy,

            @RequestParam(defaultValue = "asc") String direction) {

        // Determine sorting direction
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        // Create pagination request
        Pageable pageable = PageRequest.of(page, size, sort);

        // Get paginated students
        Page<Student> students = studentService.getAllStudents(pageable);

        return ResponseEntity.ok(students);
    }

    // GET: Get student by ID
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable int id) {

        return studentService.getStudentById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST: Add student
    @PostMapping
    public ResponseEntity<Student> addStudent(
            @Valid @RequestBody StudentRequest request) {

        Student savedStudent = studentService.addStudent(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedStudent);
    }

    // PUT: Update student
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(
            @PathVariable int id,
            @Valid @RequestBody StudentRequest request) {

        return studentService.updateStudent(id, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE: Delete student
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable int id) {

        boolean deleted = studentService.deleteStudent(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    // GET: Search students by name
    @GetMapping("/search")
    public ResponseEntity<List<Student>> searchStudentsByName(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String department) {

        if (name != null && !name.isBlank()) {
            return ResponseEntity.ok(
                    studentService.searchByName(name)
            );
        }

        if (department != null && !department.isBlank()) {
            return ResponseEntity.ok(
                    studentService.searchByDepartment(department)
            );
        }

        return ResponseEntity.badRequest().build();
    }
}
