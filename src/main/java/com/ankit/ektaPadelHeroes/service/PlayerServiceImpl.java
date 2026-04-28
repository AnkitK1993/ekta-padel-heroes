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
        Optional<Player> player = playerRepository.findById(id);
        if (player.isPresent()) {
            Player existingPlayer = player.get();
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
        }
        return null;
    }

    @Override
    public boolean deletePlayer(long id) {
        Optional<Player> player = playerRepository.findById(id);
        if (player.isPresent()) {
            playerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

