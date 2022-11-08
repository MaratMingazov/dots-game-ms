package com.dotsgamems;

import lombok.NonNull;
import lombok.val;

public class GameUtils {

    /**
     * Метод проходится по доске 'board' и удаляет все точки играка 'player' которые не сосетстуют с другими точками данного играка
     * The method go over the 'board' and delete of 'player' dots that have less then 2 neighbors
     * @param player given player
     * @param board given board
     * @return new board with removed tail dots
     */
    @NonNull
    public String[][] removeTailDots(@NonNull Players player,
                                     @NonNull String[][] board) {
        val copyBoard = copyBoard(board);
        boolean needNewIteration;
        do {
            needNewIteration = removeTailDotsIteration(player, copyBoard);
        } while (needNewIteration);
        return copyBoard;
    }

    private boolean removeTailDotsIteration(@NonNull Players player,
                                            @NonNull String[][] board) {
        boolean needNewIteration = false;
        val dotLabel = player.getDotLabel();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                val currentValue = board[i][j];
                if (!currentValue.equals(dotLabel)) {
                    continue;
                }
                int neighborDotsCount = 0;
                if (isPointBelongToBoard(board, i-1, j-1) && board[i-1][j-1].equals(dotLabel)) {
                    neighborDotsCount++;
                }
                if (isPointBelongToBoard(board, i-1, j) && board[i-1][j].equals(dotLabel)) {
                    neighborDotsCount++;
                }
                if (isPointBelongToBoard(board, i-1, j+1) && board[i-1][j+1].equals(dotLabel)) {
                    neighborDotsCount++;
                }
                if (isPointBelongToBoard(board, i, j-1) && board[i][j-1].equals(dotLabel)) {
                    neighborDotsCount++;
                }
                if (isPointBelongToBoard(board, i, j+1) && board[i][j+1].equals(dotLabel)) {
                    neighborDotsCount++;
                }
                if (isPointBelongToBoard(board, i+1, j-1) && board[i+1][j-1].equals(dotLabel)) {
                    neighborDotsCount++;
                }
                if (isPointBelongToBoard(board, i+1, j) && board[i+1][j].equals(dotLabel)) {
                    neighborDotsCount++;
                }
                if (isPointBelongToBoard(board, i+1, j+1) && board[i+1][j+1].equals(dotLabel)) {
                    neighborDotsCount++;
                }
                if (neighborDotsCount < 2) {
                    board[i][j] = Players.getEmptyDotLabel();
                    needNewIteration = true;
                }
            }
        }
        return needNewIteration;
    }

    /**
     * Method checks whether the given point 'x' and 'y' belong to given 'board'
     * @param board given board
     * @param x point first parameter
     * @param y point second parameter
     * @return true in case of given point belong to the board
     */
    public boolean isPointBelongToBoard(@NonNull String[][] board, int x, int y) {
        if (x < 0 || x >= board.length) {
            return false;
        }
        if (y < 0 || y >= board[x].length) {
            return false;
        }
        return true;
    }

    /**
     * Method create the copy of given board
     * @param sourceBoard given board
     * @return new copy of board
     */
    public String[][] copyBoard(@NonNull String[][] sourceBoard) {
        if (sourceBoard.length < 1) {
            throw new IllegalArgumentException("The given board should not be empty.");
        }
        String[][] copyBoard = new String[sourceBoard.length][sourceBoard[0].length];
        for (int i = 0; i < sourceBoard.length; i++) {
            for (int j = 0; j < sourceBoard[i].length; j++) {
                copyBoard[i][j] = sourceBoard[i][j];
            }
        }
        return copyBoard;
    }

    public String[][] createEmptyBoard(int boardSize) {
        String[][] board = new String[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = Players.getEmptyDotLabel();
            }
        }
        return board;
    }
}
