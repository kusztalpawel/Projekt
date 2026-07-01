package org.project.server.repository;

import org.project.server.model.UserPower;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserPowerRepository extends JpaRepository<UserPower, Long> {
    List<UserPower> findByUserUsername(String username);
    Optional<UserPower> findByUserUsernameAndPowerUpId(String username, Long powerId);
}
