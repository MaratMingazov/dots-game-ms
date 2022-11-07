package com.dotsgamems;

import lombok.Data;
import lombok.NonNull;

import java.awt.*;


@Data
public class Game {

    private final String emptyDot = ".";

    private final String[][] board;
    private final String[][] computerBoard;

    private final Integer boardSize;

    public Game(@NonNull Integer boardSize) {
        this.boardSize = boardSize;
        this.board = new String[boardSize][boardSize];
        this.computerBoard = new String[boardSize][boardSize];

        initBoards();

    }

    private void initBoards() {

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = emptyDot;
                computerBoard[i][j] = emptyDot;
            }
        }

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

    public void setDot(@NonNull Players player, @NonNull Point dot) {
        if(dot.x < 0 || dot.x >= boardSize || dot.y < 0 || dot.y >= boardSize) {
            throw new IllegalArgumentException("The given point is out of boarder: " + dot);
        }
        if (!computerBoard[dot.x][dot.y].equals(emptyDot)) {
            throw new IllegalArgumentException("The board already have dot at this point: " + dot);
        }
        board[dot.x][dot.y] = player.getDotLabel();
        computerBoard[dot.x][dot.y] = player.getDotLabel();
    }

    private String print(@NonNull String[][] printBoard) {
        StringBuilder result = new StringBuilder();

        result.append("   ");
        for (int i = 0; i < boardSize; i++) {
            String value = i < 10 ? "0" + i : Integer.toString(i);
            result.append(value).append(" ");
        }
        result.append("\n");

        for (int i = 0; i < boardSize; i++) {
            String value = i < 10 ? "0" + i : Integer.toString(i);
            result.append(value).append(" ");
            for (int j = 0; j < boardSize; j++) {
                result.append(printBoard[i][j]).append("  ");
            }
            result.append("\n");
        }
        result.append("\n");
        return result.toString();
    }
}
