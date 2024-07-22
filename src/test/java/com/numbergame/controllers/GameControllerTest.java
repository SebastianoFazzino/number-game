package com.numbergame.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.numbergame.models.GameResult;
import com.numbergame.models.GameRound;
import com.numbergame.services.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameController.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String ENDPOINT = "/v1/number-game/play-round";

    /**
     * Method under test:
     * {@link GameController#playRound(GameRound)}
     * Test the play-round endpoint given a valid GameRound
     */
    @Test
    void shouldReturnGameResultWhenPlayRound() throws Exception {

        GameRound gameRound = new GameRound(50, 100);
        GameResult expectedGameResult = new GameResult(30, true, 198.0f);

        when(gameService.computeResult(gameRound)).thenReturn(expectedGameResult);

        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gameRound)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedGameResult)));
    }

    /**
     * Method under test:
     * {@link GameController#playRound(GameRound)}
     * Test the play-round endpoint given an invalid GameRound
     */
    @Test
    void shouldReturnBadRequestWhenInvalidSelectedNumber() throws Exception {

        GameRound invalidGameRound = new GameRound(122, 30);

        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidGameRound)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Number must be at most 100")));
    }

    /**
     * Method under test:
     * {@link GameController#playRound(GameRound)}
     * Test the play-round endpoint given an invalid GameRound
     */
    @Test
    void shouldReturnBadRequestWhenInvalidBet() throws Exception {

        GameRound invalidGameRound = new GameRound(32, 0);

        mockMvc.perform(post("/v1/number-game/play-round")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidGameRound)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Bet must be at least 1")));
    }

}
