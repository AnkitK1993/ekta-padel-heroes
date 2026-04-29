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
        getPlayerAndUpdatePlayerHistory(match);
        return match;
    }

    @Override
    public List<MatchHistory> addMultipleMatchRecords(List<MatchHistory> matchHistories) {
        List<MatchHistory> matches = matchHistoryRepository.saveAll(matchHistories);

        for (MatchHistory match : matches)
            getPlayerAndUpdatePlayerHistory(match);

        return matches;
    }

    private void getPlayerAndUpdatePlayerHistory(MatchHistory match) {
        Player player1 = playerService.getPlayerByNameOrDisplayName(match.getPlayer1()).getFirst();
        Player player2 = playerService.getPlayerByNameOrDisplayName(match.getPlayer2()).getFirst();
        Player player3 = playerService.getPlayerByNameOrDisplayName(match.getPlayer3()).getFirst();
        Player player4 = playerService.getPlayerByNameOrDisplayName(match.getPlayer4()).getFirst();

        handlePlayerHistory(match, player1, match.isWinnerTeam1());
        handlePlayerHistory(match, player2, match.isWinnerTeam1());
        handlePlayerHistory(match, player3, !match.isWinnerTeam1());
        handlePlayerHistory(match, player4, !match.isWinnerTeam1());
    }

    private void handlePlayerHistory(MatchHistory match, Player player, boolean won) {
        int gamesWon = 0;
        int gamesLost = 0;
        if(won){
            gamesWon = Math.max(match.getGamesWon(), match.getGamesLost());
            gamesLost = Math.min(match.getGamesLost(), match.getGamesWon());
        }else{
            gamesWon = Math.min(match.getGamesWon(), match.getGamesLost());
            gamesLost = Math.max(match.getGamesLost(), match.getGamesWon());
        }
        PlayerHistory playerHistory = new PlayerHistory(player.getName(), gamesWon, gamesLost);
        playerHistoryService.updateOrCreatePlayerHistory(player,playerHistory, won);
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
        Optional<MatchHistory> matchHistory = matchHistoryRepository.findById(id);
        if (matchHistory.isPresent()) {
            MatchHistory existingMatchHistory = matchHistory.get();
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
            existingMatchHistory.setDatePlayed(matchHistoryDetails.getDatePlayed());
            existingMatchHistory.setWinnerTeam1(matchHistoryDetails.isWinnerTeam1());
            return matchHistoryRepository.save(existingMatchHistory);
        }
        return null;
    }

    @Override
    public boolean deleteMatchHistory(long id) {
        Optional<MatchHistory> matchHistory = matchHistoryRepository.findById(id);
        if (matchHistory.isPresent()) {
            matchHistoryRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
