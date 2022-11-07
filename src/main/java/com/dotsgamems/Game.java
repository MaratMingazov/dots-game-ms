package com.dotsgamems;

import lombok.Data;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import lombok.val;


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
        return result.toString();
    }

    public void findLoop(@NonNull Players player) {
        val boardWithoutTails = gameUtils.removeTailDots(player, computerBoard);
    }

}
