package com.ankit.ektaPadelHeroes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LeaderBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;
    private double wins;
    private double losses;
    private int gamesWon;
    private int gamesLost;
    private int gamesDifference;
    private double winRate;
    private LocalDateTime datePlayed;

}
