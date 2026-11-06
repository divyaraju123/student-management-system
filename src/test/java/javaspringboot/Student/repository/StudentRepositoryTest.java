
package javaspringboot.Student.repository;

import javaspringboot.Student.model.Student;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    // Test 1: Save student
    @Test
    void testSaveStudent() {

        Student student = new Student();
        student.setName("Rahul");
        student.setDepartment("CSE");

        Student savedStudent = studentRepository.save(student);

        assertNotNull(savedStudent.getId());
        assertEquals("Rahul", savedStudent.getName());
        assertEquals("CSE", savedStudent.getDepartment());
    }

    // Test 2: Find student by ID
    @Test
    void testFindStudentById() {

        Student student = new Student();
        student.setName("Arun");
        student.setDepartment("ECE");

        Student savedStudent = studentRepository.save(student);

        Optional<Student> result =
                studentRepository.findById(savedStudent.getId());

        assertTrue(result.isPresent());
        assertEquals("Arun", result.get().getName());
    }

    // Test 3: Find all students
    @Test
    void testFindAllStudents() {

        Student student1 = new Student();
        student1.setName("Rahul");
        student1.setDepartment("CSE");

        Student student2 = new Student();
        student2.setName("Priya");
        student2.setDepartment("ECE");

        studentRepository.save(student1);
        studentRepository.save(student2);

        List<Student> students = studentRepository.findAll();

        assertEquals(2, students.size());
    }

    // Test 4: Update student
    @Test
    void testUpdateStudent() {

        Student student = new Student();
        student.setName("Rahul");
        student.setDepartment("CSE");

        Student savedStudent = studentRepository.save(student);

        savedStudent.setName("Rahul Kumar");
        savedStudent.setDepartment("IT");

        Student updatedStudent =
                studentRepository.save(savedStudent);

        assertEquals("Rahul Kumar", updatedStudent.getName());
        assertEquals("IT", updatedStudent.getDepartment());
    }

    // Test 5: Delete student
    @Test
    void testDeleteStudent() {

        Student student = new Student();
        student.setName("Rahul");
        student.setDepartment("CSE");

        Student savedStudent = studentRepository.save(student);

        studentRepository.deleteById(savedStudent.getId());

        assertFalse(
                studentRepository.existsById(savedStudent.getId())
        );
    }

    // Test 6: Student not found
    @Test
    void testStudentNotFound() {

        Optional<Student> result =
                studentRepository.findById(999);

        assertTrue(result.isEmpty());
    }

    // Test 7: Search student by name
    @Test
    void testSearchByName() {

        Student student = new Student();
        student.setName("Rahul Kumar");
        student.setDepartment("CSE");

        studentRepository.save(student);

        List<Student> result =
                studentRepository.findByNameContainingIgnoreCase("rahul");

        assertEquals(1, result.size());
        assertEquals("Rahul Kumar", result.get(0).getName());
    }

    // Test 8: Search student by department
    @Test
    void testSearchByDepartment() {

        Student student = new Student();
        student.setName("Priya");
        student.setDepartment("ECE");

        studentRepository.save(student);

        List<Student> result =
                studentRepository.findByDepartmentContainingIgnoreCase("ece");

        assertEquals(1, result.size());
        assertEquals("ECE", result.get(0).getDepartment());
    }

    // Test 9: Search with no matching student
    @Test
    void testSearchStudentNotFound() {

        List<Student> result =
                studentRepository.findByNameContainingIgnoreCase("Unknown");

        assertTrue(result.isEmpty());
    }

    // Test 10: Check whether student exists
    @Test
    void testStudentExists() {

        Student student = new Student();
        student.setName("Arun");
        student.setDepartment("ECE");

        Student savedStudent = studentRepository.save(student);

        assertTrue(studentRepository.existsById(savedStudent.getId()));
    }
}
