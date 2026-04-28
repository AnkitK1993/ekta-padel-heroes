package com.ankit.ektaPadelHeroes.controller;

import com.ankit.ektaPadelHeroes.model.Player;
import com.ankit.ektaPadelHeroes.model.PlayerHistory;
import com.ankit.ektaPadelHeroes.service.PlayerHistoryService;
import com.ankit.ektaPadelHeroes.service.PlayerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/player-histories")
public class PlayerHistoryController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PlayerHistoryService playerHistoryService;

    // READ - GET all
    @GetMapping
    public ResponseEntity<List<PlayerHistory>> getAllPlayerHistories() {
        List<PlayerHistory> playerHistories = playerHistoryService.getAllPlayerHistories();
        return new ResponseEntity<>(playerHistories, HttpStatus.OK);
    }

    // READ - GET by ID
    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable long id) {
        Optional<Player> player = playerService.getPlayerById(id);
        return player.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //DELETE - DELETE All Player Histories
    @DeleteMapping
    public ResponseEntity<List<PlayerHistory>> deleteAllPlayerHistory() {
        playerHistoryService.deleteAllPlayerHistory();
       return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
