package com.numbergame.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameResult {

    private int generatedNumber;

    private boolean isWin;

    private float wonAmount;

}
