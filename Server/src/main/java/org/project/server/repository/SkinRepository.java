package org.project.server.repository;

import org.project.server.model.Skin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkinRepository  extends JpaRepository<Skin, Long> {
    Optional<Skin> findByUrl(String skinUrl);
}
