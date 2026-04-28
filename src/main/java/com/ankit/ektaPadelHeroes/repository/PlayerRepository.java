package com.ankit.ektaPadelHeroes.repository;

import com.ankit.ektaPadelHeroes.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    @Query("SELECT p FROM Player p WHERE p.name LIKE %:name% OR p.displayName LIKE %:name%")
    List<Player> getPlayerByNameOrDisplayName(@Param("name") String name);
}

