package com.numbergame.controllers;

import com.numbergame.models.GameResult;
import com.numbergame.models.GameRound;
import com.numbergame.services.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping({"/v1/number-game"})
public class GameController {

    private final GameService gameService;

    @PostMapping("/play-round")
    public GameResult playRound(
            @Valid @RequestBody GameRound gameRound
    ) {

        log.info("Playing round with selected number: {} and placed bet: {}",
                gameRound.getSelectedNumber(), gameRound.getPlacedBet());

        return gameService.computeResult(gameRound);
    }

}
