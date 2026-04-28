package com.ankit.ektaPadelHeroes.service;

import com.ankit.ektaPadelHeroes.model.MatchHistory;
import com.ankit.ektaPadelHeroes.repository.MatchHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MatchHistoryServiceImpl implements MatchHistoryService {

    @Autowired
    private MatchHistoryRepository matchHistoryRepository;

    @Override
    public MatchHistory createMatchHistory(MatchHistory matchHistory) {
        return matchHistoryRepository.save(matchHistory);
    }

    @Override
    public List<MatchHistory> addMultipleMatchRecords(List<MatchHistory> matchHistories) {
        return matchHistoryRepository.saveAll(matchHistories);
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
