package com.ankit.ektaPadelHeroes.service;

import com.ankit.ektaPadelHeroes.model.Player;
import com.ankit.ektaPadelHeroes.model.PlayerHistory;
import com.ankit.ektaPadelHeroes.repository.PlayerHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerHistoryServiceImpl implements PlayerHistoryService {

    @Autowired
    private PlayerHistoryRepository playerHistoryRepository;

    @Override
    public PlayerHistory createPlayerHistory(PlayerHistory playerHistory) {
        playerHistory.setLastUpdatedOn(LocalDate.now());
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
        if (playerHistoryRepository.existsById(id)) {
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
    public void updateOrCreatePlayerHistory(Player player, PlayerHistory pHistory, boolean won) {
        Optional<PlayerHistory> existingPlayerOpt = playerHistoryRepository.findByName(player.getName());
        PlayerHistory playerHistory;

        if (existingPlayerOpt.isPresent()) {
            playerHistory = existingPlayerOpt.get();
            playerHistory.setMatchesPlayed(playerHistory.getMatchesPlayed() + 1);
            playerHistory.setMatchesWon(playerHistory.getMatchesWon() + (won ? 1 : 0));
            playerHistory.setGamesWon(playerHistory.getGamesWon() + pHistory.getGamesWon());
            playerHistory.setGamesLost(playerHistory.getGamesLost() + pHistory.getGamesLost());
        } else {
            playerHistory = new PlayerHistory();
            playerHistory.setName(player.getName());
            playerHistory.setMatchesPlayed(1);
            playerHistory.setMatchesWon(won ? 1 : 0);
            playerHistory.setGamesWon(pHistory.getGamesWon());
            playerHistory.setGamesLost(pHistory.getGamesLost());
            playerHistory.setCreatedOn(LocalDate.now());
        }

        // Calculate rating metrics with proper precision
        updatePlayerRatings(playerHistory);
        playerHistory.setLastUpdatedOn(LocalDate.now());
        playerHistoryRepository.save(playerHistory);
    }

    private void updatePlayerRatings(PlayerHistory playerHistory) {
        double totalGames = playerHistory.getGamesWon() + playerHistory.getGamesLost();

        if (totalGames > 0) {
            // Win rate: (matches won / total matches) * 100
            BigDecimal winRate = BigDecimal.valueOf(playerHistory.getMatchesWon())
                    .divide(BigDecimal.valueOf(playerHistory.getMatchesPlayed()), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP);
            playerHistory.setWinRate(winRate);

            // Skill rating: (games won / total games) * 10
            BigDecimal skillRating = BigDecimal.valueOf(playerHistory.getGamesWon())
                    .divide(BigDecimal.valueOf(totalGames), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(10))
                    .setScale(2, RoundingMode.HALF_UP);
            playerHistory.setSkillRating(skillRating);
        }
    }
}

