package com.dotsgamems;

import lombok.Data;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import lombok.val;

import java.util.Map;


@Log4j2
@Data
public class Game {

    private final String[][] board;
    private final String[][] computerBoard;
    private final GameUtils gameUtils;

    public Game(@NonNull Integer boardSize) {

        if (boardSize < 5) {
            throw new IllegalArgumentException("The board size should be at least 5");
        }

        this.gameUtils = new GameUtils();
        this.board = gameUtils.createEmptyBoard(boardSize);
        this.computerBoard = gameUtils.createEmptyBoard(boardSize);

        int middlePoint = boardSize/2;
        board[middlePoint][middlePoint] = Players.FIRST.getDotLabel();
        board[middlePoint - 1][middlePoint + 1] = Players.SECOND.getDotLabel();
        computerBoard[middlePoint][middlePoint] = Players.FIRST.getDotLabel();
        computerBoard[middlePoint - 1][middlePoint + 1] = Players.SECOND.getDotLabel();

    }

    public String printBoard() {
        return print(board);
    }

    public String printComputerBoard() {
        return print(computerBoard);
    }

    public void setDot(@NonNull Players player, int x, int y) {
        if(x < 0 || x >= board.length || y < 0 || y >= board.length) {
            throw new IllegalArgumentException("The given point is out of boarder: " + x + "," + y);
        }
        if (!computerBoard[x][y].equals(Players.getEmptyDotLabel())) {
            throw new IllegalArgumentException("The board already have dot at this point: " + x + "," + y);
        }
        board[x][y] = player.getDotLabel();
        computerBoard[x][y] = player.getDotLabel();
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
                if (dotValue.equals("1")) {
                    dotValue = "1";
                }
                if (dotValue.equals("2")) {
                    dotValue = "*";
                }
                result.append(dotValue).append("  ");
            }
            result.append("\n");
        }
        result.append("\n");
        val score = calculateScore();
        result.append(score.get(Players.FIRST)).append(" : ").append(score.get(Players.SECOND)).append("\n");
        return result.toString();
    }

    public void updateCapturedDots(@NonNull Players player) {
        val capturedDots = gameUtils.findCapturedDots(player, computerBoard);
        val capturedEmptyDots = gameUtils.findCapturedEmptyDots(computerBoard, capturedDots);
        capturedDots.forEach(dot -> computerBoard[dot.x][dot.y] = player.getDotLabel());
        capturedEmptyDots.forEach(dot -> computerBoard[dot.x][dot.y] = player.getDotLabel());
    }

    public boolean isGameFinished() {
        for (int i = 0; i < computerBoard.length; i++) {
            for (int j = 0; j < computerBoard[i].length; j++) {
                if (computerBoard[i][j].equals(Players.getEmptyDotLabel())) {
                    return false;
                }
            }
        }
        return true;
    }

    public Map<Players,Integer> calculateScore() {
        int firstPlayerScore = 0;
        int secondPlayerScore = 0;
        for (int i = 0; i < computerBoard.length; i++) {
            for (int j = 0; j < computerBoard[i].length; j++) {
                if (computerBoard[i][j].equals(Players.FIRST.getDotLabel()) && board[i][j].equals(Players.SECOND.getDotLabel())) {
                    firstPlayerScore++;
                } else if (computerBoard[i][j].equals(Players.SECOND.getDotLabel()) && board[i][j].equals(Players.FIRST.getDotLabel())) {
                    secondPlayerScore++;
                }
            }
        }
        return Map.of(Players.FIRST, firstPlayerScore, Players.SECOND, secondPlayerScore);
    }

}
