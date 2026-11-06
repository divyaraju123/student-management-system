
package javaspringboot.Student.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import javaspringboot.Student.dto.StudentRequest;
import javaspringboot.Student.exception.GlobalExceptionHandler;
import javaspringboot.Student.model.Student;
import javaspringboot.Student.service.StudentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private LocalValidatorFactoryBean validator;

    private Student student;

    @BeforeEach
    void setUp() {

        objectMapper = new ObjectMapper();

        validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        StudentController studentController =
                new StudentController(studentService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(studentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();

        student = new Student();
        student.setName("Rahul");
        student.setDepartment("CSE");
    }

    // Test 1: Get all students
    @Test
    void testGetAllStudents() throws Exception {

        Page<Student> page = new PageImpl<>(
                List.of(student),
                PageRequest.of(0, 10, Sort.by("id").ascending()),
                1
        );

        when(studentService.getAllStudents(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Rahul"))
                .andExpect(jsonPath("$.content[0].department").value("CSE"));

        verify(studentService, times(1))
                .getAllStudents(any(Pageable.class));
    }

    // Test 2: Get student by ID
    @Test
    void testGetStudentById() throws Exception {

        when(studentService.getStudentById(1))
                .thenReturn(Optional.of(student));

        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Rahul"))
                .andExpect(jsonPath("$.department").value("CSE"));
    }

    // Test 3: Student not found
    @Test
    void testGetStudentByIdNotFound() throws Exception {

        when(studentService.getStudentById(99))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/students/99"))
                .andExpect(status().isNotFound());
    }

    // Test 4: Add student successfully
    @Test
    void testAddStudent() throws Exception {

        when(studentService.addStudent(any(StudentRequest.class)))
                .thenReturn(student);

        String requestJson = """
                {
                    "name": "Rahul",
                    "department": "CSE"
                }
                """;

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Rahul"))
                .andExpect(jsonPath("$.department").value("CSE"));

        verify(studentService, times(1))
                .addStudent(any(StudentRequest.class));
    }

    // Test 5: Add student with invalid input
    @Test
    void testAddStudentWithInvalidInput() throws Exception {

        String requestJson = """
                {
                    "name": "",
                    "department": ""
                }
                """;

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.department").exists());

        verify(studentService, never())
                .addStudent(any(StudentRequest.class));
    }

    // Test 6: Update student successfully
    @Test
    void testUpdateStudent() throws Exception {

        when(studentService.updateStudent(
                eq(1), any(StudentRequest.class)))
                .thenReturn(Optional.of(student));

        String requestJson = """
                {
                    "name": "Rahul",
                    "department": "CSE"
                }
                """;

        mockMvc.perform(put("/api/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Rahul"))
                .andExpect(jsonPath("$.department").value("CSE"));
    }

    // Test 7: Update student that does not exist
    @Test
    void testUpdateStudentNotFound() throws Exception {

        when(studentService.updateStudent(
                eq(99), any(StudentRequest.class)))
                .thenReturn(Optional.empty());

        String requestJson = """
                {
                    "name": "Rahul",
                    "department": "CSE"
                }
                """;

        mockMvc.perform(put("/api/students/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound());
    }

    // Test 8: Update student with invalid input
    @Test
    void testUpdateStudentWithInvalidInput() throws Exception {

        String requestJson = """
                {
                    "name": "",
                    "department": ""
                }
                """;

        mockMvc.perform(put("/api/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());

        verify(studentService, never())
                .updateStudent(eq(1), any(StudentRequest.class));
    }

    // Test 9: Delete student successfully
    @Test
    void testDeleteStudent() throws Exception {

        when(studentService.deleteStudent(1))
                .thenReturn(true);

        mockMvc.perform(delete("/api/students/1"))
                .andExpect(status().isNoContent());

        verify(studentService, times(1))
                .deleteStudent(1);
    }

    // Test 10: Delete student that does not exist
    @Test
    void testDeleteStudentNotFound() throws Exception {

        when(studentService.deleteStudent(99))
                .thenReturn(false);

        mockMvc.perform(delete("/api/students/99"))
                .andExpect(status().isNotFound());
    }

    // Test 11: Search students by name
    @Test
    void testSearchStudentsByName() throws Exception {

        when(studentService.searchByName("Rahul"))
                .thenReturn(List.of(student));

        mockMvc.perform(get("/api/students/search")
                        .param("name", "Rahul"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Rahul"));
    }

    // Test 12: Search students by department
    @Test
    void testSearchStudentsByDepartment() throws Exception {

        when(studentService.searchByDepartment("CSE"))
                .thenReturn(List.of(student));

        mockMvc.perform(get("/api/students/search")
                        .param("department", "CSE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].department").value("CSE"));
    }

    // Test 13: Search without name or department
    @Test
    void testSearchWithoutParameters() throws Exception {

        mockMvc.perform(get("/api/students/search"))
                .andExpect(status().isBadRequest());
    }
}
