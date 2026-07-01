package org.project.server.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "exam_attempt")
public class ExamAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;

    private boolean finished;

    private int correctAnswers;

    private int totalQuestions;

    private int points;

    private int totalPoints;

    private double percentage;

    private double grade;

    private boolean bonusMinutesUsed;

    private boolean bonusPointsUsed;

    private boolean removeOptionUsed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(LocalDateTime finishedAt) {
        this.finishedAt = finishedAt;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public boolean isBonusMinutesUsed() {
        return bonusMinutesUsed;
    }

    public void setBonusMinutesUsed(boolean bonusMinutesUsed) {
        this.bonusMinutesUsed = bonusMinutesUsed;
    }

    public boolean isBonusPointsUsed() {
        return bonusPointsUsed;
    }

    public void setBonusPointsUsed(boolean bonusPointsUsed) {
        this.bonusPointsUsed = bonusPointsUsed;
    }

    public boolean isRemoveOptionUsed() {
        return removeOptionUsed;
    }

    public void setRemoveOptionUsed(boolean removeOptionUsed) {
        this.removeOptionUsed = removeOptionUsed;
    }
}
