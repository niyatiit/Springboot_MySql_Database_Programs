package in.niyati.practical6.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.niyati.practical6.entity.Course;
import in.niyati.practical6.entity.Student;
import in.niyati.practical6.repository.CourseRepository;

@RestController
@RequestMapping("/api/courses")
public class CourseRestController {

    private CourseRepository courseRepository;

    public CourseRestController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    // POST - Create a new course
    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        Course saved = courseRepository.save(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // GET - Fetch all courses
    @GetMapping
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // GET - Fetch one course by id
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable int id) {
        Optional<Course> course = courseRepository.findById(id);
        return course.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // GET - Fetch all students enrolled in a specific course
    // (Course is the inverse side, but we can still navigate to its students in Java,
    // even though @JsonBackReference hides "students" when Course itself is serialized elsewhere)
    @GetMapping("/{id}/students")
    public ResponseEntity<java.util.Set<Student>> getStudentsInCourse(@PathVariable int id) {
        Optional<Course> course = courseRepository.findById(id);
        if (course.isPresent()) {
            return ResponseEntity.ok(course.get().getStudents());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // PUT - Update a course's details (join-table links unaffected)
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable int id, @RequestBody Course updatedCourse) {
        Optional<Course> existing = courseRepository.findById(id);
        if (existing.isPresent()) {
            Course course = existing.get();
            course.setCourseName(updatedCourse.getCourseName());
            Course saved = courseRepository.save(course);
            return ResponseEntity.ok(saved);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // DELETE - Delete a course entirely (removes its join-table rows first)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable int id) {
        Optional<Course> existing = courseRepository.findById(id);
        if (existing.isPresent()) {
            Course course = existing.get();

            // Remove this course from every student's course set first,
            // so the join-table rows referencing it are cleared before deleting the course itself.
            if (course.getStudents() != null) {
                for (Student student : course.getStudents()) {
                    student.getCourses().remove(course);
                }
            }

            courseRepository.delete(course);
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}