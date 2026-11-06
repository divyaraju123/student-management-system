
package javaspringboot.Student.repository;

import javaspringboot.Student.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    List<Student> findByNameContainingIgnoreCase(String name);

    List<Student> findByDepartmentContainingIgnoreCase(String department);
}
