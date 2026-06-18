package org.project.server.controller;

import org.project.server.dto.CourseRequestDTO;
import org.project.server.dto.CourseResponseDTO;
import org.project.server.mapper.CourseMapper;
import org.project.server.model.Course;
import org.project.server.model.User;
import org.project.server.service.CourseService;
import org.project.server.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final UserService userService;

    public CourseController(CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<CourseResponseDTO> create(@RequestBody CourseRequestDTO dto, Authentication authentication) {
        Course course = courseService.createCourse(CourseMapper.toEntity(dto), authentication.getName());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CourseMapper.toDTO(course));
    }

    @GetMapping
    public ResponseEntity<List<CourseResponseDTO>> getMyCourses(Authentication authentication) {
        return ResponseEntity.ok(
                courseService.getMyCourses(authentication.getName())
                        .stream()
                        .map(CourseMapper::toDTO)
                        .toList()
        );
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CourseResponseDTO>> getAll() {
        return ResponseEntity.ok(
                courseService.getAllCourses()
                        .stream()
                        .map(CourseMapper::toDTO)
                        .toList()
        );
    }

    @GetMapping("/templates")
    public ResponseEntity<List<CourseResponseDTO>> getTemplates(Authentication authentication) {
        User user = userService.getByUsername(authentication.getName());
        return ResponseEntity.ok(
                courseService.getTemplates(user)
                        .stream()
                        .map(CourseMapper::toDTO)
                        .toList()
        );
    }

    @PostMapping("/{courseId}/enroll")
    public ResponseEntity<CourseResponseDTO> enroll(
            @PathVariable Long courseId,
            Authentication authentication
    ) {
        Course course = courseService.enroll(courseId, authentication.getName());

        return ResponseEntity.ok(CourseMapper.toDTO(course));
    }

    /*@GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                CourseMapper.toDTO(courseService.getById(id))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> update(@PathVariable Long id,
                                                  @RequestBody CourseRequestDTO dto) {

        Course updated = courseService.updateCourse(id, dto);

        return ResponseEntity.ok(CourseMapper.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }*/
}