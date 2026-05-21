package org.project.server.mapper;

import org.project.server.dto.CourseRequestDTO;
import org.project.server.dto.CourseResponseDTO;
import org.project.server.model.Course;

public class CourseMapper {
    private CourseMapper() {}

    public static Course toEntity(CourseRequestDTO dto) {
        Course course = new Course();
        course.setName(dto.getName());
        return course;
    }

    public static CourseResponseDTO toDTO(Course course) {
        CourseResponseDTO dto = new CourseResponseDTO();
        dto.setId(course.getId());
        dto.setName(course.getName());
        return dto;
    }
}
