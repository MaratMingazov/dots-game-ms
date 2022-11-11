package com.dotsgamems.game;

import com.dotsgamems.mongo.MongoService;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;


@Log4j2
@Data
public class Game {

    private final String[][] board;
    private final String[][] computerBoard;

    private Point firstPlayerLastPoint = new Point(-1, -1);
    private Point secondPlayerLastPoint = new Point(-1, -1);

    private final MongoService mongoService;

    private Map<String, Point> firstPlayerHistory = new HashMap<>();
    private Map<String, Point> secondPlayerHistory = new HashMap<>();

    public Game(@NonNull Integer boardSize, @NonNull MongoService mongoService) {

        AnsiOutput.setEnabled(AnsiOutput.Enabled.ALWAYS);
        if (boardSize < 5) {
            throw new IllegalArgumentException("The board size should be at least 5");
        }

        this.mongoService = mongoService;

        this.board = GameUtils.createEmptyBoard(boardSize);
        this.computerBoard = GameUtils.createEmptyBoard(boardSize);

        int middlePoint = boardSize/2;
        board[middlePoint][middlePoint - 1] = Players.FIRST.getDotLabel();
        board[middlePoint - 1][middlePoint] = Players.SECOND.getDotLabel();
        computerBoard[middlePoint][middlePoint - 1] = Players.FIRST.getDotLabel();
        computerBoard[middlePoint - 1][middlePoint] = Players.SECOND.getDotLabel();

    }

    public String printBoard() {
        return print(board);
    }

    public String printComputerBoard() {
        return print(computerBoard);
    }

    public void setDot(@NonNull Players player, int x, int y) {
        GameUtils.setDot(player, board, computerBoard, x, y);
        if (player == Players.FIRST) {
            firstPlayerLastPoint.x = x;
            firstPlayerLastPoint.y = y;
        } else {
            secondPlayerLastPoint.x = x;
            secondPlayerLastPoint.y = y;
        }
    }

    private String print(@NonNull String[][] printBoard) {
        StringBuilder result = new StringBuilder();

        result.append("   ");
        for (int i = 0; i < board.length; i++) {
            String value = i < 10 ? "0" + i : Integer.toString(i);
            result.append(value).append(" ");
        }
        result.append("\n");

        for (int i = 0; i < board.length; i++) {
            String value = i < 10 ? "0" + i : Integer.toString(i);
            result.append(value).append(" ");
            for (int j = 0; j < board.length; j++) {
                String dotValue = printBoard[i][j];
                if (dotValue.equals("0")) {
                    dotValue = ".";
                }
                if (dotValue.equals(Players.FIRST.getDotLabel())) {
                    dotValue = AnsiOutput.toString(AnsiColor.CYAN, "*", AnsiColor.DEFAULT);
                    if(i == firstPlayerLastPoint.x && j == firstPlayerLastPoint.y) {
                        dotValue = AnsiOutput.toString(AnsiColor.BRIGHT_CYAN, "*", AnsiColor.DEFAULT);
                    }
                }
                if (dotValue.equals(Players.SECOND.getDotLabel())) {
                    dotValue = AnsiOutput.toString(AnsiColor.MAGENTA, "*", AnsiColor.DEFAULT);
                    if(i == secondPlayerLastPoint.x && j == secondPlayerLastPoint.y) {
                        dotValue = AnsiOutput.toString(AnsiColor.BRIGHT_MAGENTA, "*", AnsiColor.DEFAULT);
                    }
                }
                result.append(dotValue).append("  ");
            }
            result.append("\n");
        }
        result.append("\n");
        val score = calculateScore();
        result.append(score.get(Players.FIRST)).append(" : ").append(score.get(Players.SECOND)).append("\n");

        if (isGameFinished()) {
            result.append("Game over!").append("\n");
        }

        return result.toString();
    }

    public void makeMove(@NonNull Players player, int x, int y) {
        val oppositePlayer = Players.getOppositeById(player.getId());

        val boardString = GameUtils.transformBoardToString(computerBoard);
        if (player == Players.FIRST) {
            firstPlayerHistory.put(boardString, new Point(x,y));
        } else {
            secondPlayerHistory.put(boardString, new Point(x,y));
        }

        setDot(player, x, y);
        GameUtils.updateCapturedDots(player, computerBoard);
        GameUtils.updateCapturedDots(oppositePlayer, computerBoard);
    }

    public boolean isGameFinished() {
        return GameUtils.isGameFinished(computerBoard);
    }

    public Map<Players,Integer> calculateScore() {
        return GameUtils.calculateScore(board, computerBoard);
    }

    public Point calculateNextMove(@NonNull Players player) {
        return mongoService.getProbabilityMove(player, computerBoard);
    }

    public void updateStatistics(int index, int epoch) {
        val score = calculateScore();
        val firstPlayerScore = score.get(Players.FIRST);
        val secondPlayerScore = score.get(Players.SECOND);

        if (firstPlayerScore > secondPlayerScore) {
            mongoService.increaseProbabilities(Players.FIRST, firstPlayerHistory);
            mongoService.decreaseProbabilities(Players.SECOND, secondPlayerHistory);
        } else if (firstPlayerScore < secondPlayerScore) {
            mongoService.decreaseProbabilities(Players.FIRST, firstPlayerHistory);
            mongoService.increaseProbabilities(Players.SECOND, secondPlayerHistory);
        }
    }

}
