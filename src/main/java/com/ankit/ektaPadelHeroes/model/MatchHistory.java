package com.ankit.ektaPadelHeroes.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Data
public class MatchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String player1;
    private String player2;
    private String player3;
    private String player4;
    private int gamesWon;
    private int gamesLost;
    private LocalDate datePlayed;
    private boolean isWinnerTeam1;

    // Constructor with required fields
    public MatchHistory(String player1, String player2, String player3, String player4, int gamesWon, int gamesLost, LocalDate datePlayed) {
        this.player1 = player1;
        this.player2 = player2;
        this.player3 = player3;
        this.player4 = player4;
        this.gamesWon = gamesWon;
        this.gamesLost = gamesLost;
        this.datePlayed = datePlayed == null ? LocalDate.now() : datePlayed;
        this.isWinnerTeam1 = gamesWon >= gamesLost;
    }
}
