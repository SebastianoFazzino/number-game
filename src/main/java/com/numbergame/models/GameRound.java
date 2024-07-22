package com.numbergame.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameRound {

    @Min(value = 1, message = "Number must be at least 1")
    @Max(value = 100, message = "Number must be at most 100")
    private int selectedNumber;

    @Min(value = 1, message = "Bet must be at least 1")
    private float placedBet;

}
