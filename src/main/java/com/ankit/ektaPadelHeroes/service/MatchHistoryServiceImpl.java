package com.ankit.ektaPadelHeroes.service;

import com.ankit.ektaPadelHeroes.model.MatchHistory;
import com.ankit.ektaPadelHeroes.model.Player;
import com.ankit.ektaPadelHeroes.model.PlayerHistory;
import com.ankit.ektaPadelHeroes.repository.MatchHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MatchHistoryServiceImpl implements MatchHistoryService {

    @Autowired
    private MatchHistoryRepository matchHistoryRepository;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PlayerHistoryService playerHistoryService;

    @Override
    public MatchHistory createMatchHistory(MatchHistory matchHistory) {
        MatchHistory match = matchHistoryRepository.save(matchHistory);
        updatePlayerHistoriesForMatch(match);
        return match;
    }

    @Override
    public List<MatchHistory> addMultipleMatchRecords(List<MatchHistory> matchHistories) {
        List<MatchHistory> matches = matchHistoryRepository.saveAll(matchHistories);
        matches.forEach(this::updatePlayerHistoriesForMatch);
        return matches;
    }

    private void updatePlayerHistoriesForMatch(MatchHistory match) {
        try {
            List<Player> p1Result = playerService.getPlayerByNameOrDisplayName(match.getPlayer1());
            List<Player> p2Result = playerService.getPlayerByNameOrDisplayName(match.getPlayer2());
            List<Player> p3Result = playerService.getPlayerByNameOrDisplayName(match.getPlayer3());
            List<Player> p4Result = playerService.getPlayerByNameOrDisplayName(match.getPlayer4());

            // Check if players were found
            if (p1Result.isEmpty() || p2Result.isEmpty() || p3Result.isEmpty() || p4Result.isEmpty()) {
                return; // Skip if any player not found
            }

            Player player1 = p1Result.get(0);
            Player player2 = p2Result.get(0);
            Player player3 = p3Result.get(0);
            Player player4 = p4Result.get(0);

            // Update player histories (Team 1 vs Team 2)
            updatePlayerHistory(match, player1, match.isWinnerTeam1());
            updatePlayerHistory(match, player2, match.isWinnerTeam1());
            updatePlayerHistory(match, player3, !match.isWinnerTeam1());
            updatePlayerHistory(match, player4, !match.isWinnerTeam1());
        } catch (Exception e) {
            // Log error but don't fail the match creation
            System.err.println("Error updating player histories: " + e.getMessage());
        }
    }

    private void updatePlayerHistory(MatchHistory match, Player player, boolean won) {
        int gamesWon, gamesLost;

        if (won) {
            gamesWon = Math.max(match.getGamesWon(), match.getGamesLost());
            gamesLost = Math.min(match.getGamesLost(), match.getGamesWon());
        } else {
            gamesWon = Math.min(match.getGamesWon(), match.getGamesLost());
            gamesLost = Math.max(match.getGamesLost(), match.getGamesWon());
        }

        PlayerHistory playerHistory = new PlayerHistory(player.getName(), gamesWon, gamesLost);
        playerHistoryService.updateOrCreatePlayerHistory(player, playerHistory, won);
    }

    @Override
    public List<MatchHistory> getAllMatchHistories() {
        return matchHistoryRepository.findAll();
    }

    @Override
    public Optional<MatchHistory> getMatchHistoryById(long id) {
        return matchHistoryRepository.findById(id);
    }

    @Override
    public MatchHistory updateMatchHistory(long id, MatchHistory matchHistoryDetails) {
        return matchHistoryRepository.findById(id).map(existingMatchHistory -> {
            if (matchHistoryDetails.getPlayer1() != null) {
                existingMatchHistory.setPlayer1(matchHistoryDetails.getPlayer1());
            }
            if (matchHistoryDetails.getPlayer2() != null) {
                existingMatchHistory.setPlayer2(matchHistoryDetails.getPlayer2());
            }
            if (matchHistoryDetails.getPlayer3() != null) {
                existingMatchHistory.setPlayer3(matchHistoryDetails.getPlayer3());
            }
            if (matchHistoryDetails.getPlayer4() != null) {
                existingMatchHistory.setPlayer4(matchHistoryDetails.getPlayer4());
            }
            existingMatchHistory.setGamesWon(matchHistoryDetails.getGamesWon());
            existingMatchHistory.setGamesLost(matchHistoryDetails.getGamesLost());
            if (matchHistoryDetails.getDatePlayed() != null) {
                existingMatchHistory.setDatePlayed(matchHistoryDetails.getDatePlayed());
            }
            existingMatchHistory.setWinnerTeam1(matchHistoryDetails.isWinnerTeam1());
            return matchHistoryRepository.save(existingMatchHistory);
        }).orElse(null);
    }

    @Override
    public boolean deleteMatchHistory(long id) {
        if (matchHistoryRepository.existsById(id)) {
            matchHistoryRepository.deleteById(id);
            return true;
        }
        return false;
    }
}


