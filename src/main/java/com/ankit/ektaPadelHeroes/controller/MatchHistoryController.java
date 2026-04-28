package com.ankit.ektaPadelHeroes.controller;

import com.ankit.ektaPadelHeroes.model.MatchHistory;
import com.ankit.ektaPadelHeroes.service.MatchHistoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/matches")
public class MatchHistoryController {

    @Autowired
    private MatchHistoryService matchHistoryService;

    // CREATE - POST
    @PostMapping
    public ResponseEntity<MatchHistory> createMatchHistory(@Valid @RequestBody MatchHistory matchHistory) {
        MatchHistory savedMatchHistory = matchHistoryService.createMatchHistory(matchHistory);
        return new ResponseEntity<>(savedMatchHistory, HttpStatus.CREATED);
    }

    // CREATE BULK - POST
    @PostMapping("/bulk-add")
    public ResponseEntity<List<MatchHistory>> addMultipleMatchRecords(@Valid @RequestBody List<MatchHistory> matchHistories) {
        List<MatchHistory> savedMatchHistories = matchHistoryService.addMultipleMatchRecords(matchHistories);
        return new ResponseEntity<>(savedMatchHistories, HttpStatus.CREATED);
    }

    // READ - GET all
    @GetMapping
    public ResponseEntity<List<MatchHistory>> getAllMatchHistories() {
        List<MatchHistory> matchHistories = matchHistoryService.getAllMatchHistories();
        return new ResponseEntity<>(matchHistories, HttpStatus.OK);
    }

    // READ - GET by ID
    @GetMapping("/{id}")
    public ResponseEntity<MatchHistory> getMatchHistoryById(@PathVariable long id) {
        Optional<MatchHistory> matchHistory = matchHistoryService.getMatchHistoryById(id);
        return matchHistory.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // UPDATE - PUT
    @PutMapping("/{id}")
    public ResponseEntity<MatchHistory> updateMatchHistory(@PathVariable long id, @Valid @RequestBody MatchHistory matchHistoryDetails) {
        MatchHistory updatedMatchHistory = matchHistoryService.updateMatchHistory(id, matchHistoryDetails);
        if (updatedMatchHistory != null) {
            return new ResponseEntity<>(updatedMatchHistory, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatchHistory(@PathVariable long id) {
        boolean deleted = matchHistoryService.deleteMatchHistory(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
