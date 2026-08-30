package in.niyati.practical6.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.niyati.practical6.entity.Course;
import in.niyati.practical6.entity.Student;
import in.niyati.practical6.repository.CourseRepository;
import in.niyati.practical6.repository.StudentRepository;

@RestController
@RequestMapping("/api/students")
public class StudentRestController {

    private StudentRepository studentRepository;
    private CourseRepository courseRepository;

    public StudentRestController(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    // POST - Create a Student with a set of EXISTING course ids
    // The incoming JSON sends courses like [{"courseId":1}], but we must
    // look up the REAL managed Course entities from the DB before saving -
    // otherwise JPA would try to INSERT new course rows instead of linking to existing ones.
    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Set<Course> resolvedCourses = new HashSet<>();

        if (student.getCourses() != null) {
            for (Course c : student.getCourses()) {
                Optional<Course> existingCourse = courseRepository.findById(c.getCourseId());
                existingCourse.ifPresent(resolvedCourses::add);
            }
        }

        student.setCourses(resolvedCourses);
        Student saved = studentRepository.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // POST - Enroll an existing student into an additional course
    @PostMapping("/{id}/enroll/{courseId}")
    public ResponseEntity<Student> enrollInCourse(@PathVariable int id, @PathVariable int courseId) {
        Optional<Student> studentOpt = studentRepository.findById(id);
        Optional<Course> courseOpt = courseRepository.findById(courseId);

        if (studentOpt.isPresent() && courseOpt.isPresent()) {
            Student student = studentOpt.get();
            Course course = courseOpt.get();

            if (student.getCourses() == null) {
                student.setCourses(new HashSet<>());
            }
            student.getCourses().add(course);

            Student saved = studentRepository.save(student);
            return ResponseEntity.ok(saved);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // GET - Fetch all students
    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // GET - View a student with their enrolled courses
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable int id) {
        Optional<Student> student = studentRepository.findById(id);
        return student.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // PUT - Replace a student's entire course set
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable int id, @RequestBody Student updatedStudent) {
        Optional<Student> existing = studentRepository.findById(id);
        if (existing.isPresent()) {
            Student student = existing.get();
            student.setStudentName(updatedStudent.getStudentName());

            if (updatedStudent.getCourses() != null) {
                Set<Course> resolvedCourses = new HashSet<>();
                for (Course c : updatedStudent.getCourses()) {
                    Optional<Course> existingCourse = courseRepository.findById(c.getCourseId());
                    existingCourse.ifPresent(resolvedCourses::add);
                }
                student.setCourses(resolvedCourses); // completely replaces the old set
            }

            Student saved = studentRepository.save(student);
            return ResponseEntity.ok(saved);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // DELETE - Remove a single student-course link from the join table
    // (without deleting either the Student or the Course entity)
    @DeleteMapping("/{studentId}/unenroll/{courseId}")
    public ResponseEntity<Void> unenrollFromCourse(@PathVariable int studentId, @PathVariable int courseId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        Optional<Course> courseOpt = courseRepository.findById(courseId);

        if (studentOpt.isPresent() && courseOpt.isPresent()) {
            Student student = studentOpt.get();
            Course course = courseOpt.get();

            student.getCourses().remove(course);
            studentRepository.save(student);

            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}