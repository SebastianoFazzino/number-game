package com.numbergame.services;

import com.numbergame.models.GameResult;
import com.numbergame.models.GameRound;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class GameServiceTest {

    private GameService gameService;

    @BeforeEach
    void setUp() {
        gameService = new GameService() {
            @Override
            protected int generateRandomNumber() {
                return 30;
            }
        };
    }

    /**
     * Method under test:
     * {@link GameService#computeResult(GameRound)}
     * Test the win result
     */
    @Test
    void shouldReturnWinResult() {

        GameRound gameRound = new GameRound(50, 100);

        GameResult result = gameService.computeResult(gameRound);

        assertTrue(result.isWin());
        assertEquals(30, result.getGeneratedNumber());
        assertEquals(198.0f, result.getWonAmount(), 0.01);
    }

    /**
     * Method under test:
     * {@link GameService#computeResult(GameRound)}
     * Test the lose result
     */
    @Test
    void shouldReturnLoseResult() {

        GameRound gameRound = new GameRound(20, 100);

        GameResult result = gameService.computeResult(gameRound);

        assertFalse(result.isWin());
        assertEquals(30, result.getGeneratedNumber());
        assertEquals(0.0f, result.getWonAmount(), 0.01);
    }

    /**
     * Method under test:
     * {@link GameService#computeResult(GameRound)}
     * Test the win result with max selected number
     */
    @Test
    void shouldReturnWinResultWithMaxSelectedNumber() {

        GameRound gameRound = new GameRound(100, 100);

        GameResult result = gameService.computeResult(gameRound);

        assertTrue(result.isWin());
        assertEquals(30, result.getGeneratedNumber());
        assertEquals(99.0f, result.getWonAmount(), 0.01);
    }

    /**
     * Method under test:
     * {@link GameService#computeResult(GameRound)}
     * Test the game logic for a million rounds
     * Player number is randomly generated between 1 and 100
     */
    @Test
    void testRTPForMillionRoundsAndPlayerNumberRandomGenerated() {

        int numberOfRounds = 1_000_000;
        float singleRoundBetAmount = 1.0f; // Assuming bet amount is 1.0 for each round
        int numberOfThreads = 24;

        GameService gameService = new GameService();
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        List<Callable<Float>> tasks = new ArrayList<>();

        for (int i = 0; i < numberOfRounds; i++) {

            tasks.add(() -> {

                int selectedNumber = gameService.generateRandomNumber();
                GameRound gameRound = new GameRound(selectedNumber, singleRoundBetAmount);

                GameResult result = gameService.computeResult(gameRound);
                return result.getWonAmount();
            });
        }

        float totalSpent = numberOfRounds * singleRoundBetAmount; // Total spent is 1 million bets of 1.0 each
        float totalWon = 0.0f;

        try {

            List<Future<Float>> results = executorService.invokeAll(tasks);
            for (Future<Float> result : results) {
                totalWon += result.get();
            }

        } catch (InterruptedException | ExecutionException e) {
            log.error("Error occurred while executing tasks: '{}'", e.getMessage());

        } finally {
            executorService.shutdown();
        }

        // Calculate Return to Player (RTP)
        float rtp = (totalWon / totalSpent) * 100;
        String formattedRTP = String.format("%.2f", rtp);

        log.info("Total Spent: ${}, Total Won: ${}, RTP: {}%", totalSpent, totalWon, formattedRTP);
    }

    /**
     * Method under test:
     * {@link GameService#computeResult(GameRound)}
     * Test the lose result with min selected number
     */
    @Test
    void shouldReturnLoseResultWithMinSelectedNumber() {

        GameRound gameRound = new GameRound(1, 100);

        GameResult result = gameService.computeResult(gameRound);

        assertFalse(result.isWin());
        assertEquals(30, result.getGeneratedNumber());
        assertEquals(0.0f, result.getWonAmount(), 0.01);
    }

    /**
     * Method under test:
     * {@link GameService#generateRandomNumber}
     * Test the boundary values of the random number generator
     */
    @Test
    void testGenerateRandomNumberBoundary() {

        // Create a new instance of GameService for testing random number generation
        GameService gameService = new GameService();

        int numberOfTasks = 100_000;
        int numberOfThreads = 24;

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        List<Callable<Boolean>> tasks = new ArrayList<>();

        for (int i = 0; i < numberOfTasks; i++) {
            tasks.add(() -> {
                int generatedNumber = gameService.generateRandomNumber();
                return generatedNumber >= 1 && generatedNumber <= 100;
            });
        }

        try {
            List<Future<Boolean>> results = executorService.invokeAll(tasks);

            for (Future<Boolean> result : results) {
                assertTrue(result.get());
            }

        } catch (InterruptedException | ExecutionException e) {
            log.error("Error occurred while executing tasks: '{}'", e.getMessage());

        } finally {
            executorService.shutdown();
        }
    }

}