package org.project.server.repository;

import org.project.server.model.Course;
import org.project.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>{
    List<Course> findByUserId(Long userId);
    List<Course> findByUserIsNull();
    boolean existsByUserAndName(User user, String name);
    Course findByUserAndName(User user, String name);
    boolean existsByUserIsNullAndName(String name);
}
