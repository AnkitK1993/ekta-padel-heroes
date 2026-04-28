package com.ankit.ektaPadelHeroes.controller;

import com.ankit.ektaPadelHeroes.model.Player;
import com.ankit.ektaPadelHeroes.service.PlayerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    // CREATE - POST
    @PostMapping
    public ResponseEntity<Player> createPlayer(@Valid @RequestBody Player player) {
        Player savedPlayer = playerService.createPlayer(player);
        return new ResponseEntity<>(savedPlayer, HttpStatus.CREATED);
    }

    // CREATE BULK - POST
    @PostMapping("/bulk-add")
    public ResponseEntity<List<Player>> createBulkPlayers(@Valid @RequestBody List<Player> players) {
        List<Player> savedPlayers = playerService.addMultiplePlayers(players);
        return new ResponseEntity<>(savedPlayers, HttpStatus.CREATED);
    }

    // READ - GET all
    @GetMapping
    public ResponseEntity<List<Player>> getAllPlayers() {
        List<Player> players = playerService.getAllPlayers();
        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    // READ - GET by ID
    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable long id) {
        Optional<Player> player = playerService.getPlayerById(id);
        return player.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // READ - GET by Name or DisplayName
    @GetMapping("/search")
    public ResponseEntity<List<Player>> getPlayerByNameOrDisplayName(@RequestParam String name) {
        List<Player> players = playerService.getPlayerByNameOrDisplayName(name);
        if (players.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    // UPDATE - PUT
    @PutMapping("/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable long id, @Valid @RequestBody Player playerDetails) {
        Player updatedPlayer = playerService.updatePlayer(id, playerDetails);
        if (updatedPlayer != null) {
            return new ResponseEntity<>(updatedPlayer, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable long id) {
        boolean deleted = playerService.deletePlayer(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
