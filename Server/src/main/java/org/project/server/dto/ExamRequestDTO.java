package org.project.server.dto;

import jakarta.persistence.*;
import org.project.server.model.User;

import java.time.LocalDate;

public class ExamRequestDTO {

    private String name;
    private LocalDate type;
    private Integer points;
    private Float grade;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getType() {
        return type;
    }

    public void setType(LocalDate type) {
        this.type = type;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Float getGrade() {
        return grade;
    }

    public void setGrade(Float grade) {
        this.grade = grade;
    }
}