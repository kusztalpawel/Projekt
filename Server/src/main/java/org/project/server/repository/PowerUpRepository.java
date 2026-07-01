package org.project.server.repository;

import org.project.server.model.PowerUp;
import org.project.server.model.PowerUpType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PowerUpRepository extends JpaRepository<PowerUp, Long> {
    Optional<PowerUp> findFirstByType(PowerUpType type);
}
