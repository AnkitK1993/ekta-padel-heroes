package com.ankit.ektaPadelHeroes.service;

import com.ankit.ektaPadelHeroes.dto.LeaderboardDTO;
import com.ankit.ektaPadelHeroes.model.MatchHistory;
import com.ankit.ektaPadelHeroes.model.Player;
import com.ankit.ektaPadelHeroes.model.PlayerHistory;
import com.ankit.ektaPadelHeroes.repository.MatchHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

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
            if (p1Result.isEmpty() || p2Result.isEmpty() || p3Result.isEmpty() || p4Result.isEmpty()) {
                return;
            }
            Player player1 = p1Result.get(0);
            Player player2 = p2Result.get(0);
            Player player3 = p3Result.get(0);
            Player player4 = p4Result.get(0);
            updatePlayerHistory(match, player1, match.isWinnerTeam1());
            updatePlayerHistory(match, player2, match.isWinnerTeam1());
            updatePlayerHistory(match, player3, !match.isWinnerTeam1());
            updatePlayerHistory(match, player4, !match.isWinnerTeam1());
        } catch (Exception e) {
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

    @Override
    public boolean deleteAllMatchHistories() {
        try {
            matchHistoryRepository.deleteAll();
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting all match histories: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<LeaderboardDTO> getLeaderboardByPeriod(String period) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate;
        switch (period.toLowerCase()) {
            case "weekly":
                startDate = endDate.minusDays(7);
                break;
            case "monthly":
                startDate = endDate.minusDays(30);
                break;
            case "yearly":
                startDate = endDate.minusDays(365);
                break;
            case "life":
                startDate = LocalDate.of(2000, 1, 1);
                break;
            case "daily":
            default:
                startDate = endDate.minusDays(1);
                break;
        }
        List<MatchHistory> matches = matchHistoryRepository.getMatchesByPeriod(startDate, endDate);
        Map<String, LeaderboardData> leaderboardMap = new HashMap<>();
        Map<String, Player> playerMap = new HashMap<>();
        for (MatchHistory match : matches) {
            try {
                List<Player> p1Result = playerService.getPlayerByNameOrDisplayName(match.getPlayer1());
                List<Player> p2Result = playerService.getPlayerByNameOrDisplayName(match.getPlayer2());
                List<Player> p3Result = playerService.getPlayerByNameOrDisplayName(match.getPlayer3());
                List<Player> p4Result = playerService.getPlayerByNameOrDisplayName(match.getPlayer4());
                if (p1Result.isEmpty() || p2Result.isEmpty() || p3Result.isEmpty() || p4Result.isEmpty()) {
                    continue;
                }
                Player player1 = p1Result.get(0);
                Player player2 = p2Result.get(0);
                Player player3 = p3Result.get(0);
                Player player4 = p4Result.get(0);
                aggregateMatchToLeaderboard(leaderboardMap, playerMap, match, player1, match.isWinnerTeam1());
                aggregateMatchToLeaderboard(leaderboardMap, playerMap, match, player2, match.isWinnerTeam1());
                aggregateMatchToLeaderboard(leaderboardMap, playerMap, match, player3, !match.isWinnerTeam1());
                aggregateMatchToLeaderboard(leaderboardMap, playerMap, match, player4, !match.isWinnerTeam1());
            } catch (Exception e) {
                System.err.println("Error processing match for leaderboard: " + e.getMessage());
            }
        }
        List<LeaderboardDTO> leaderboardDTOs = new ArrayList<>();
        for (String playerName : leaderboardMap.keySet()) {
            LeaderboardData data = leaderboardMap.get(playerName);
            Player player = playerMap.get(playerName);
            if (player != null && data != null) {
                LeaderboardDTO leaderboardDTO = new LeaderboardDTO();
                leaderboardDTO.setPlayer(player);
                leaderboardDTO.setWins(data.wins);
                leaderboardDTO.setLosses(data.losses);
                leaderboardDTO.setGamesWon(data.gamesWon);
                leaderboardDTO.setGamesLost(data.gamesLost);
                leaderboardDTO.setGamesDifference(data.gamesWon - data.gamesLost);
                leaderboardDTO.setSkillRating(calculateSkillRating(data.gamesWon, data.gamesLost));
                leaderboardDTO.setWinRate(calculateWinRate(data.wins, data.losses));
                leaderboardDTOs.add(leaderboardDTO);
            }
        }
        leaderboardDTOs.sort((l1, l2) -> {
            int skillRatingCompare = Double.compare(l2.getSkillRating(), l1.getSkillRating());
            if (skillRatingCompare != 0)
                return skillRatingCompare;
            return Double.compare(l2.getWinRate(), l1.getWinRate());
        });
        return leaderboardDTOs;
    }

    private void aggregateMatchToLeaderboard(Map<String, LeaderboardData> leaderboardMap, Map<String, Player> playerMap, MatchHistory match, Player player, boolean won) {
        String playerKey = player.getName();
        LeaderboardData data = leaderboardMap.computeIfAbsent(playerKey, k -> new LeaderboardData());
        playerMap.put(playerKey, player);
        if (won) {
            data.wins++;
            data.gamesWon += Math.max(match.getGamesWon(), match.getGamesLost());
            data.gamesLost += Math.min(match.getGamesLost(), match.getGamesWon());
        } else {
            data.losses++;
            data.gamesWon += Math.min(match.getGamesWon(), match.getGamesLost());
            data.gamesLost += Math.max(match.getGamesLost(), match.getGamesWon());
        }
    }

    private double calculateWinRate(double wins, double losses) {
        if (wins + losses == 0) return 0.0;
        BigDecimal winsBD = new BigDecimal(wins);
        BigDecimal totalBD = new BigDecimal(wins + losses);
        return winsBD.divide(totalBD, 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    private double calculateSkillRating(double gamesWon, double gamesLost) {
        if (gamesWon + gamesLost == 0) return 0.0;
        BigDecimal winsBD = new BigDecimal(gamesWon);
        BigDecimal totalBD = new BigDecimal(gamesWon + gamesLost);
        return winsBD.divide(totalBD, 4, RoundingMode.HALF_UP).multiply(new BigDecimal(10)).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    private static class LeaderboardData {
        double wins = 0;
        double losses = 0;
        int gamesWon = 0;
        int gamesLost = 0;
    }
}
