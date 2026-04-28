package com.ankit.ektaPadelHeroes.service;

import com.ankit.ektaPadelHeroes.model.Player;

import java.util.List;
import java.util.Optional;

public interface PlayerService {

    Player createPlayer(Player player);

    List<Player> addMultiplePlayers(List<Player> players);

    List<Player> getAllPlayers();

    Optional<Player> getPlayerById(long id);

    Player updatePlayer(long id, Player playerDetails);

    boolean deletePlayer(long id);
}
