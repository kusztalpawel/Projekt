package org.project.server.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
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

    private int friendsCount;
    private int tasksCompleted;
    private int loginStreak;
    private int levelsCompleted;
    private int skillpointsUsed;

    @ManyToMany
    @JoinTable(name = "user_friends", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "friend_id"))
    private List<User> friends = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    private Character character;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    private LocalDateTime lastLogin;

    private int wins;
    private int loses;
    @ManyToOne
    @JoinColumn(name = "skin_id")
    private Skin skin;

    private Integer powerCoins;

    public Skin getSkin() {
        return skin;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }

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

    public void setRole(UserRole role) {
        this.role = role;
    }

    public UserRole getRole() {
        return role;
    }

    public int getFriendsCount() {
        return friendsCount;
    }

    public void setFriendsCount(int friendsCount) {
        this.friendsCount = friendsCount;
    }

    public int getTasksCompleted() {
        return tasksCompleted;
    }

    public void setTasksCompleted(int tasksCompleted) {
        this.tasksCompleted = tasksCompleted;
    }

    public int getLoginStreak() {
        return loginStreak;
    }

    public void setLoginStreak(int loginStreak) {
        this.loginStreak = loginStreak;
    }

    public int getSkillpointsUsed() {
        return skillpointsUsed;
    }

    public void setSkillpointsUsed(int skillpointsUsed) {
        this.skillpointsUsed = skillpointsUsed;
    }

    public int getLevelsCompleted() {
        return levelsCompleted;
    }

    public void setLevelsCompleted(int levelsCompleted) {
        this.levelsCompleted = levelsCompleted;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLoses() {
        return loses;
    }

    public void setLoses(int loses) {
        this.loses = loses;
    }

    public Integer getPowerCoins() {
        return powerCoins;
    }

    public void setPowerCoins(Integer powerCoins) {
        this.powerCoins = powerCoins;
    }
}