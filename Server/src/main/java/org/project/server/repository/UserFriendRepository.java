package org.project.server.repository;

import org.project.server.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Repository
public interface UserFriendRepository extends org.springframework.data.repository.Repository<User, Long> {
    @Query(value = "SELECT last_fight FROM user_friends WHERE user_id = :userId AND friend_id = :friendId", nativeQuery = true)
    LocalDate getLastFight(@Param("userId") Long userId, @Param("friendId") Long friendId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user_friends SET last_fight = :date WHERE user_id = :userId AND friend_id = :friendId", nativeQuery = true)
    void updateLastFight(@Param("userId") Long userId, @Param("friendId") Long friendId, @Param("date") LocalDate date);
}
