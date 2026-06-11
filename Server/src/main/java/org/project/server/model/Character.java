package org.project.server.model;

import jakarta.persistence.*;

@Entity
@Table(name = "characters")
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer attackPoints;
    private Integer defencePoints;
    private Integer agilityPoints;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Float health;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAttackPoints() {
        return attackPoints;
    }

    public void setAttackPoints(Integer attackPoints) {
        this.attackPoints = attackPoints;
    }

    public Integer getDefencePoints() {
        return defencePoints;
    }

    public void setDefencePoints(Integer defencePoints) {
        this.defencePoints = defencePoints;
    }

    public Integer getAgilityPoints() {
        return agilityPoints;
    }

    public void setAgilityPoints(Integer agilityPoints) {
        this.agilityPoints = agilityPoints;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Float getHealth() {
        return health;
    }

    public void setHealth(Float health) {
        this.health = health;
    }
}
