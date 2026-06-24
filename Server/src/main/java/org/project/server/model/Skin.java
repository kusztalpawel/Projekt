package org.project.server.model;

import jakarta.persistence.*;

@Entity
@Table(name = "skins")
public class Skin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String url;

    private int requirement;

    @Enumerated(EnumType.STRING)
    private AchievementMetric metric;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getRequirement() {
        return requirement;
    }

    public void setRequirement(int requirement) {
        this.requirement = requirement;
    }

    public AchievementMetric getMetric() {
        return metric;
    }

    public void setMetric(AchievementMetric metric) {
        this.metric = metric;
    }
}
