package org.project.server.repository;

import org.project.server.model.Course;
import org.project.server.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{
    List<Task> findByCourseId(Long courseId);
    List<Task> findByCourse(Course course);
}
