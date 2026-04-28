package com.ankit.ektaPadelHeroes.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MatchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NonNull
    private String player1;
    @NonNull
    private String player2;
    @NonNull
    private String player3;
    @NonNull
    private String player4;
    @NonNull
    private int gamesWon;
    @NonNull
    private int gamesLost;
    private LocalDateTime datePlayed;
    private boolean isWinnerTeam1;
}
