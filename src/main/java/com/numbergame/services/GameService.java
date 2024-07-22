package com.numbergame.services;

import com.numbergame.models.GameResult;
import com.numbergame.models.GameRound;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class GameService {

    private static final int MIN_NUMBER = 1;
    private static final int MAX_NUMBER = 100;

    public GameResult computeResult(GameRound round) {

        int generatedNumber = this.generateRandomNumber();
        int selectedNumber = round.getSelectedNumber();

        boolean isWin = selectedNumber > generatedNumber;

        float wonAmount = isWin ? round.getPlacedBet() * (99f / selectedNumber) : 0;

        return new GameResult(generatedNumber, isWin, wonAmount);
    }

    protected int generateRandomNumber() {
        return new Random().nextInt(MAX_NUMBER - MIN_NUMBER + 1) + MIN_NUMBER;
    }

}
