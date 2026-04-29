package com.ankit.ektaPadelHeroes.dto;

import com.ankit.ektaPadelHeroes.model.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LeaderboardDTO {
    private Player player;
    private double wins;
    private double losses;
    private int gamesWon;
    private int gamesLost;
    private int gamesDifference;
    private double skillRating;
    private double winRate;
}
