package org.project.server.controller;

import org.project.server.dto.CourseRequestDTO;
import org.project.server.dto.CourseResponseDTO;
import org.project.server.mapper.CourseMapper;
import org.project.server.model.Course;
import org.project.server.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<CourseResponseDTO> create(@RequestBody CourseRequestDTO dto, Authentication authentication) {

        String username = authentication.getName();

        Course course = courseService.createCourse(dto, username);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CourseMapper.toDTO(course));
    }

    @GetMapping
    public ResponseEntity<List<CourseResponseDTO>> getAll(Authentication authentication) {
        return ResponseEntity.ok(
                courseService.getMyCourses(authentication.getName())
                        .stream()
                        .map(CourseMapper::toDTO)
                        .toList()
        );
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