package com.numbergame.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameRoundTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    /**
     * Class under test:
     * {@link GameRound}
     * Test the constraints of the GameRound class
     */
    @Test
    void testValidGameRound() {
        GameRound round = new GameRound(50, 10);

        Set<ConstraintViolation<GameRound>> violations = validator.validate(round);
        assertTrue(violations.isEmpty());
    }

    /**
     * Class under test:
     * {@link GameRound}
     * Test the constraints of the GameRound class
     */
    @Test
    void testInvalidPlacedBet() {
        GameRound round = new GameRound(50, 0.5f);

        Set<ConstraintViolation<GameRound>> violations = validator.validate(round);
        assertEquals(1, violations.size());

        ConstraintViolation<GameRound> violation = violations.iterator().next();
        assertEquals("Bet must be at least 1", violation.getMessage());
    }

    /**
     * Class under test:
     * {@link GameRound}
     * Test the constraints of the GameRound class
     */
    @Test
    void testInvalidSelectedNumberLow() {
        GameRound round = new GameRound(0, 10);

        Set<ConstraintViolation<GameRound>> violations = validator.validate(round);
        assertEquals(1, violations.size());

        ConstraintViolation<GameRound> violation = violations.iterator().next();
        assertEquals("Number must be at least 1", violation.getMessage());
    }

    /**
     * Class under test:
     * {@link GameRound}
     * Test the constraints of the GameRound class
     */
    @Test
    void testInvalidSelectedNumberHigh() {
        GameRound round = new GameRound(150, 10);

        Set<ConstraintViolation<GameRound>> violations = validator.validate(round);
        assertEquals(1, violations.size());

        ConstraintViolation<GameRound> violation = violations.iterator().next();
        assertEquals("Number must be at most 100", violation.getMessage());
    }
}
