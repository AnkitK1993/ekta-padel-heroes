package com.ankit.ektaPadelHeroes.controller;

import com.ankit.ektaPadelHeroes.dto.LeaderboardDTO;
import com.ankit.ektaPadelHeroes.service.MatchHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leaderboards")
public class LeaderboardController {

    @Autowired
    private MatchHistoryService matchHistoryService;

    @GetMapping("/period")
    public ResponseEntity<List<LeaderboardDTO>> getLeaderboardByPeriod(@RequestParam(value = "period", defaultValue = "daily") String period) {
        List<LeaderboardDTO> leaderboardDTOS = matchHistoryService.getLeaderboardByPeriod(period);
        return new ResponseEntity<>(leaderboardDTOS, HttpStatus.OK);
    }
}

