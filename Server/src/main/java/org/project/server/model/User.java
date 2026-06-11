package org.project.server.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private Integer points;

    @ManyToMany
    @JoinTable(name = "user_achievements", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "achievement_id"))
    private List<Achievement> achievements =  new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "user_friends", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "friend_id"))
    private List<User> friends = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    private Character character;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Achievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<Achievement> achievements) {
        this.achievements = achievements;
    }

    public List<User> getFriends() {
        return friends;
    }

    public List<Long> getFriendsIds() {
        return friends.stream().map(User::getId).toList();
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Character getCharacter() {
        return character;
    }
}