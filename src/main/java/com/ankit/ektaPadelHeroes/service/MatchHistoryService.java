package com.ankit.ektaPadelHeroes.service;

import com.ankit.ektaPadelHeroes.dto.LeaderboardDTO;
import com.ankit.ektaPadelHeroes.model.MatchHistory;

import java.util.List;
import java.util.Optional;

public interface MatchHistoryService {

    MatchHistory createMatchHistory(MatchHistory matchHistory);

    List<MatchHistory> addMultipleMatchRecords(List<MatchHistory> matchHistories);

    List<MatchHistory> getAllMatchHistories();

    Optional<MatchHistory> getMatchHistoryById(long id);

    MatchHistory updateMatchHistory(long id, MatchHistory matchHistoryDetails);

    boolean deleteMatchHistory(long id);

    boolean deleteAllMatchHistories();

    List<LeaderboardDTO> getLeaderboardByPeriod(String period);
}
