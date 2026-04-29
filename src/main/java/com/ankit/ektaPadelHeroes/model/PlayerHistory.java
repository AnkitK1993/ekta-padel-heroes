package com.ankit.ektaPadelHeroes.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Data
public class PlayerHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private double matchesPlayed;
    private double matchesWon;
    @Column(name = "win_rate", precision = 5, scale = 2)
    private BigDecimal winRate;
    private double gamesWon;
    private double gamesLost;
    @Column(name = "skill_rating", precision = 5, scale = 2)
    private BigDecimal skillRating;
    private LocalDateTime createdOn;
    private LocalDateTime lastUpdatedOn;

    public PlayerHistory(String name, double gamesWon, double gamesLost) {
        this.name = name;
        this.gamesWon = gamesWon;
        this.gamesLost = gamesLost;
    }
}
