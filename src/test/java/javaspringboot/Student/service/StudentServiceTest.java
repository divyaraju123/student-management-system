
package javaspringboot.Student.service;

import javaspringboot.Student.dto.StudentRequest;
import javaspringboot.Student.model.Student;
import javaspringboot.Student.repository.StudentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student;
    private StudentRequest request;

    @BeforeEach
    void setUp() {

        student = new Student();
        student.setName("Rahul");
        student.setDepartment("CSE");

        request = new StudentRequest();
        request.setName("Rahul");
        request.setDepartment("CSE");
    }

    // Test 1: Add student
    @Test
    void testAddStudent() {

        when(studentRepository.save(any(Student.class)))
                .thenReturn(student);

        Student result = studentService.addStudent(request);

        assertNotNull(result);
        assertEquals("Rahul", result.getName());
        assertEquals("CSE", result.getDepartment());

        verify(studentRepository, times(1))
                .save(any(Student.class));
    }

    // Test 2: Get student by ID
    @Test
    void testGetStudentById() {

        when(studentRepository.findById(1))
                .thenReturn(Optional.of(student));

        Optional<Student> result =
                studentService.getStudentById(1);

        assertTrue(result.isPresent());
        assertEquals("Rahul", result.get().getName());
    }

    // Test 3: Update student
    @Test
    void testUpdateStudent() {

        when(studentRepository.findById(1))
                .thenReturn(Optional.of(student));

        when(studentRepository.save(any(Student.class)))
                .thenReturn(student);

        StudentRequest updateRequest = new StudentRequest();
        updateRequest.setName("Arun");
        updateRequest.setDepartment("ECE");

        Optional<Student> result =
                studentService.updateStudent(1, updateRequest);

        assertTrue(result.isPresent());
        assertEquals("Arun", result.get().getName());
        assertEquals("ECE", result.get().getDepartment());
    }

    // Test 4: Delete student
    @Test
    void testDeleteStudent() {

        when(studentRepository.existsById(1))
                .thenReturn(true);

        boolean result = studentService.deleteStudent(1);

        assertTrue(result);

        verify(studentRepository, times(1))
                .deleteById(1);
    }

    // Test 5: Search by name
    @Test
    void testSearchByName() {

        when(studentRepository.findByNameContainingIgnoreCase("Rahul"))
                .thenReturn(List.of(student));

        List<Student> result =
                studentService.searchByName("Rahul");

        assertEquals(1, result.size());
        assertEquals("Rahul", result.get(0).getName());
    }

    // Test 6: Search by department
    @Test
    void testSearchByDepartment() {

        when(studentRepository.findByDepartmentContainingIgnoreCase("CSE"))
                .thenReturn(List.of(student));

        List<Student> result =
                studentService.searchByDepartment("CSE");

        assertEquals(1, result.size());
        assertEquals("CSE", result.get(0).getDepartment());
    }

    // Test 7: Student not found
    @Test
    void testGetStudentByIdNotFound() {

        when(studentRepository.findById(99))
                .thenReturn(Optional.empty());

        Optional<Student> result =
                studentService.getStudentById(99);

        assertTrue(result.isEmpty());
    }

    // Test 8: Delete student that does not exist
    @Test
    void testDeleteStudentNotFound() {

        when(studentRepository.existsById(99))
                .thenReturn(false);

        boolean result = studentService.deleteStudent(99);

        assertFalse(result);

        verify(studentRepository, never())
                .deleteById(99);
    }

    // Test 9: Search returns no results
    @Test
    void testSearchByNameNotFound() {

        when(studentRepository.findByNameContainingIgnoreCase("Unknown"))
                .thenReturn(List.of());

        List<Student> result =
                studentService.searchByName("Unknown");

        assertTrue(result.isEmpty());
    }

    // Test 10: Update student that does not exist
    @Test
    void testUpdateStudentNotFound() {

        when(studentRepository.findById(99))
                .thenReturn(Optional.empty());

        Optional<Student> result =
                studentService.updateStudent(99, request);

        assertTrue(result.isEmpty());

        verify(studentRepository, never())
                .save(any(Student.class));
    }
}
