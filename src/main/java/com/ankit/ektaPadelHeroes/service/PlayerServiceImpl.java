package com.ankit.ektaPadelHeroes.service;

import com.ankit.ektaPadelHeroes.model.Player;
import com.ankit.ektaPadelHeroes.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public Player createPlayer(Player player) {
        return playerRepository.save(player);
    }

    @Override
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    @Override
    public Optional<Player> getPlayerById(long id) {
        return playerRepository.findById(id);
    }

    @Override
    public Player updatePlayer(long id, Player playerDetails) {
        return playerRepository.findById(id).map(existingPlayer -> {
            if (playerDetails.getName() != null) {
                existingPlayer.setName(playerDetails.getName());
            }
            if (playerDetails.getDisplayName() != null) {
                existingPlayer.setDisplayName(playerDetails.getDisplayName());
            }
            if (playerDetails.getDisplayImage() != null) {
                existingPlayer.setDisplayImage(playerDetails.getDisplayImage());
            }
            return playerRepository.save(existingPlayer);
        }).orElse(null);
    }

    @Override
    public boolean deletePlayer(long id) {
        if (playerRepository.existsById(id)) {
            playerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Player> addMultiplePlayers(List<Player> players) {
        return playerRepository.saveAll(players);
    }

    @Override
    public List<Player> getPlayerByNameOrDisplayName(String name) {
        return playerRepository.getPlayerByNameOrDisplayName(name);
    }
}
