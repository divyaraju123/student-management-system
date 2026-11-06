
package javaspringboot.Student.service;

import javaspringboot.Student.model.Student;
import javaspringboot.Student.dto.StudentRequest;
import javaspringboot.Student.repository.StudentRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    // Constructor Injection
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Get all students with Pagination and Sorting
    public Page<Student> getAllStudents(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    // Get student by ID
    public Optional<Student> getStudentById(int id) {
        return studentRepository.findById(id);
    }

    // Add a new student
    public Student addStudent(StudentRequest request) {

        Student student = new Student();

        student.setName(request.getName());
        student.setDepartment(request.getDepartment());

        return studentRepository.save(student);
    }

    // Update student
    public Optional<Student> updateStudent(
            int id, StudentRequest request) {

        Optional<Student> existingStudent =
                studentRepository.findById(id);

        if (existingStudent.isEmpty()) {
            return Optional.empty();
        }

        Student student = existingStudent.get();

        student.setName(request.getName());
        student.setDepartment(request.getDepartment());

        return Optional.of(studentRepository.save(student));
    }

    // Delete student
    public boolean deleteStudent(int id) {

        if (!studentRepository.existsById(id)) {
            return false;
        }

        studentRepository.deleteById(id);

        return true;
    }

    // Search students by name
    public List<Student> searchByName(String name) {
        return studentRepository.findByNameContainingIgnoreCase(name);
    }

    // Search students by department
    public List<Student> searchByDepartment(String department) {
        return studentRepository.findByDepartmentContainingIgnoreCase(department);
    }
}
