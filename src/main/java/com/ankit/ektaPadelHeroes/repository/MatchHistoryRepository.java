package com.ankit.ektaPadelHeroes.repository;

import com.ankit.ektaPadelHeroes.model.MatchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MatchHistoryRepository extends JpaRepository<MatchHistory, Long> {

    @Query("SELECT m FROM MatchHistory m WHERE m.datePlayed >= :startDate AND m.datePlayed <= :endDate")
    List<MatchHistory> getMatchesByPeriod(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
