package com.ankit.ektaPadelHeroes.repository;

import com.ankit.ektaPadelHeroes.model.PlayerHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerHistoryRepository extends JpaRepository<PlayerHistory, Long> {
    Optional<PlayerHistory> findByName(String name);
}

