package com.ankit.ektaPadelHeroes.service;

import com.ankit.ektaPadelHeroes.model.Player;
import com.ankit.ektaPadelHeroes.model.PlayerHistory;

import java.util.List;
import java.util.Optional;

public interface PlayerHistoryService {

    PlayerHistory createPlayerHistory(PlayerHistory playerHistory);

    List<PlayerHistory> getAllPlayerHistories();

    Optional<PlayerHistory> getPlayerHistoryById(long id);

    Optional<PlayerHistory> getPlayerHistoryByName(String name);

    boolean deletePlayerHistory(long id);

    void deleteAllPlayerHistory();

    void updateOrCreatePlayerHistory(Player player1, PlayerHistory playerHistory, boolean won);
}

