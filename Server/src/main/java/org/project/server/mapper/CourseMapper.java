package org.project.server.mapper;

import org.project.server.dto.CourseProgressDTO;
import org.project.server.dto.CourseRequestDTO;
import org.project.server.dto.CourseResponseDTO;
import org.project.server.model.Course;

public class CourseMapper {
    private CourseMapper() {}

    public static Course toEntity(CourseRequestDTO dto) {
        Course course = new Course();
        course.setName(dto.name());
        return course;
    }

    public static CourseResponseDTO toDTO(Course course) {
        return new CourseResponseDTO(course.getId(), course.getName(),course.getLevel(), course.getExperience());
    }

    public static CourseProgressDTO progressToDTO(Course course) {
        CourseProgressDTO dto = new CourseProgressDTO();

        dto.setLevel(course.getLevel());
        dto.setExperience(course.getExperience());

        return dto;
    }
}
