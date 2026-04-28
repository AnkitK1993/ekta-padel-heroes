package com.ankit.ektaPadelHeroes.repository;

import com.ankit.ektaPadelHeroes.model.MatchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchHistoryRepository extends JpaRepository<MatchHistory, Long> {
}
