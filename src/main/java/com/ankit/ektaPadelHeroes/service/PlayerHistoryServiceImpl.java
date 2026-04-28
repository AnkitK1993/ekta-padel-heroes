package com.ankit.ektaPadelHeroes.service;

import com.ankit.ektaPadelHeroes.model.Player;
import com.ankit.ektaPadelHeroes.model.PlayerHistory;
import com.ankit.ektaPadelHeroes.repository.PlayerHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerHistoryServiceImpl implements PlayerHistoryService {

    @Autowired
    private PlayerHistoryRepository playerHistoryRepository;

    @Override
    public PlayerHistory createPlayerHistory(PlayerHistory playerHistory) {
        playerHistory.setLastUpdatedOn(LocalDateTime.now());
        return playerHistoryRepository.save(playerHistory);
    }

    @Override
    public List<PlayerHistory> getAllPlayerHistories() {
        return playerHistoryRepository.findAll();
    }

    @Override
    public Optional<PlayerHistory> getPlayerHistoryById(long id) {
        return playerHistoryRepository.findById(id);
    }

    @Override
    public Optional<PlayerHistory> getPlayerHistoryByName(String name) {
        return playerHistoryRepository.findByName(name);
    }

    @Override
    public boolean deletePlayerHistory(long id) {
        Optional<PlayerHistory> playerHistory = playerHistoryRepository.findById(id);
        if (playerHistory.isPresent()) {
            playerHistoryRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public void deleteAllPlayerHistory() {
       playerHistoryRepository.deleteAll();
    }

    @Override
    public void updateOrCreatePlayerHistory(Player player,  PlayerHistory pHistory, boolean won) {
        Optional<PlayerHistory> existingPlayerOpt = playerHistoryRepository.findByName(player.getName());
        PlayerHistory playerHistory;
        if (existingPlayerOpt.isPresent()) {
            // Update existing player
            playerHistory = existingPlayerOpt.get();
            playerHistory.setMatchesPlayed(playerHistory.getMatchesPlayed() + 1);
            playerHistory.setMatchesWon(playerHistory.getMatchesWon() + (won ? 1 : 0));
            playerHistory.setGamesWon(playerHistory.getGamesWon() + pHistory.getGamesWon());
            playerHistory.setGamesLost(playerHistory.getGamesLost() + pHistory.getGamesLost());
            playerHistory.setSkillRating(BigDecimal.valueOf((playerHistory.getGamesWon()/(playerHistory.getGamesWon() + playerHistory.getGamesLost()))*10));
        } else {
            // Create new player
            playerHistory = new PlayerHistory();
            playerHistory.setName(player.getName());
            playerHistory.setMatchesPlayed(1);
            playerHistory.setMatchesWon(won ? 1 : 0);
            playerHistory.setGamesWon(pHistory.getGamesWon());
            playerHistory.setGamesLost(pHistory.getGamesLost());
            playerHistory.setCreatedOn(LocalDateTime.now());
        }

        // Calculate win rate
        if (playerHistory.getMatchesWon() > 0) {
            BigDecimal winRate = BigDecimal.valueOf((playerHistory.getMatchesWon() / playerHistory.getMatchesPlayed()) * 100L);
            playerHistory.setWinRate(winRate);
        }

        playerHistory.setSkillRating(BigDecimal.valueOf((playerHistory.getGamesWon()/(playerHistory.getGamesWon() + playerHistory.getGamesLost()))*10));
        playerHistory.setLastUpdatedOn(LocalDateTime.now());
        playerHistoryRepository.save(playerHistory);
    }
}

