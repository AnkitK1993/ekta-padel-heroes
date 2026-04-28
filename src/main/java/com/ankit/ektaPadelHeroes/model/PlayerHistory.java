package com.ankit.ektaPadelHeroes.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PlayerHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private int matchesPlayed;
    private double gamesWon;
    private double gamesLost;
    private double winRate;
    private double skillRating;
    private LocalDateTime createdOn;
    private LocalDateTime lastUpdatedOn;
}
