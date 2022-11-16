package com.dotsgamems.game;

import lombok.Data;
import lombok.NonNull;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.util.Pair;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

@Data
public class MiniMax {

    private static final Integer MAX_DEPTH = 6;
    private static final Integer MAX_ITERATIONS = 1000000;
    private static final Logger logger = LogManager.getLogger(MiniMax.class);
    public static Integer IterationsCount = 0;

    @NonNull
    public static Pair<Point, Map<Players, Integer>> findTheBestMove(@NonNull Players player,
                                                                     @NonNull String humanBoardString,
                                                                     @NonNull String computerBoardString,
                                                                     @NonNull Integer boardSize,
                                                                     @NonNull Integer depth) {


        IterationsCount++;
        val availableMoves = GameUtils.getAvailableMoves(GameUtils.transformBoardToArray(boardSize, computerBoardString));
        if (availableMoves.isEmpty()) {
            throw new IllegalArgumentException("There is no available moves.");
        }
        var bestMove = availableMoves.get(0);
        int bestScore = -1000;
        Map<Players, Integer> bestScoreMap = new HashMap<>();

        for (Point move : availableMoves) {
            val humanBoard = GameUtils.transformBoardToArray(boardSize, humanBoardString);
            val computerBoard = GameUtils.transformBoardToArray(boardSize, computerBoardString);
            val oppositePlayer = Players.getOppositeById(player.getId());
            GameUtils.setDot(player, humanBoard, computerBoard, move.x, move.y);
            GameUtils.updateCapturedDots(player, computerBoard);
            GameUtils.updateCapturedDots(oppositePlayer, computerBoard);


            if (depth == 0) {
                logger.info(" start " + move);
            }

            Map<Players, Integer> scoreMap = new HashMap<>();
            // if (GameUtils.isGameFinished(computerBoard) || depth >= MAX_DEPTH || IterationsCount > MAX_ITERATIONS) {
            //if (GameUtils.isGameFinished(computerBoard) || depth >= MAX_DEPTH) {
            if (GameUtils.isGameFinished(computerBoard)) {
                scoreMap = GameUtils.calculateScore(humanBoard, computerBoard);
            } else {
                val scorePair = findTheBestMove(oppositePlayer, GameUtils.transformBoardToString(humanBoard), GameUtils.transformBoardToString(computerBoard), computerBoard.length, depth + 1);
                scoreMap = scorePair.getSecond();
            }
            if (depth == 0) {
                logger.info(move + " / " + scoreMap);
            }
            val score = player == Players.FIRST ? scoreMap.get(Players.FIRST) - scoreMap.get(Players.SECOND) : scoreMap.get(Players.SECOND) - scoreMap.get(Players.FIRST);
            if (score > bestScore) {
                bestMove = move;
                bestScore = score;
                bestScoreMap = scoreMap;
            }
//            if (score > 0) {
//                return Pair.of(bestMove, bestScoreMap);
//            }

        }
        return Pair.of(bestMove, bestScoreMap);
    }
}
