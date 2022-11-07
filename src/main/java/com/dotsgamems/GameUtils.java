package com.dotsgamems;

import lombok.NonNull;
import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    /**
     * Methos searching all loops inside the board
     * Steps:
     * 1 - trying to find the first loop
     * 2 - if found, trying to find the next loop
     * 3 - searching while find new loop
     * @param player given player
     * @param board given board
     * @return list of found boards
     */
    public List<Node> findLoops(@NonNull Players player,
                                @NonNull String[][] board) {
        List<Node> loops = new ArrayList<>();
        val consideredDots = createEmptyBoard(board.length);

        boolean isLoopFound = false;
        do {
            val loopOptional = findOneLoop(player, board, loops);
            if (loopOptional.isPresent()) {
                isLoopFound = true;
                loops.add(loopOptional.get());
            } else {
                isLoopFound = false;
            }
        } while (isLoopFound);
        return loops;
    }

    /**
     * The method search the fist not analyzed dot and find the biggest loop from this dot
     * @param player given player
     * @param board given board
     * @param foundLoops already found loops
     * @return loop first node if found
     */
    @NonNull
    public Optional<Node> findOneLoop(@NonNull Players player,
                                      @NonNull String[][] board,
                                      @NonNull List<Node> foundLoops) {

        Node firstNode = null;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (isDotBelongLoops(i, j, foundLoops) || isDotInsideLoops(i, j, foundLoops)) {
                    // it means we already analyzed this dot and can skip
                    continue;
                }
                if (board[i][j].equals(player.getDotLabel())) {
                    // we found not analyzed dot. This dot will the loop first element
                    firstNode = new Node(i,j);
                    firstNode.next = findNextLoopNode(firstNode, player, board, i, j);
                }
            }
        }
        return Optional.of(firstNode);
    }

    private Node findNextLoopNode(@NonNull Node firstNode,
                                  @NonNull Players player,
                                  @NonNull String[][] board,
                                  int x, int y) {

        int nextX = -1;
        int nextY = -1;
        if (isPointBelongToBoard(board, x-1, y+1) && board[x-1][y+1].equals(player.getDotLabel())) {
            nextX = x-1;
            nextY = y+1;
        } else if (isPointBelongToBoard(board, x, y+1) && board[x][y+1].equals(player.getDotLabel())) {
            nextX = x;
            nextY = y+1;
        } else if (isPointBelongToBoard(board, x+1, y+1) && board[x+1][y+1].equals(player.getDotLabel())) {
            nextX = x+1;
            nextY = y+1;
        } else if (isPointBelongToBoard(board, x+1, y) && board[x+1][y].equals(player.getDotLabel())) {
            nextX = x+1;
            nextY = y;
        } else if (isPointBelongToBoard(board, x+1, y-1) && board[x+1][y-1].equals(player.getDotLabel())) {
            nextX = x+1;
            nextY = y-1;
        } else if (isPointBelongToBoard(board, x, y-1) && board[x][y-1].equals(player.getDotLabel())) {
            nextX = x;
            nextY = y-1;
        } else if (isPointBelongToBoard(board, x-1, y-1) && board[x-1][y-1].equals(player.getDotLabel())) {
            nextX = x-1;
            nextY = y-1;
        } else if (isPointBelongToBoard(board, x-1, y) && board[x-1][y].equals(player.getDotLabel())) {
            nextX = x-1;
            nextY = y;
        }

        if (nextX == -1) {
            throw new IllegalStateException("The loop has unexpected end");
        }
        if (nextX == firstNode.x && nextY == firstNode.y) {
            return null;
        } else {
            Node node = new Node(nextX, nextY);
            node.next = findNextLoopNode(firstNode, player, board, nextX, nextY);
            return  node;
        }
    }

    public boolean isDotInsideLoop(int x, int y, @NonNull Node loop) {
        return false;
    }

    public boolean isDotInsideLoops(int x, int y, @NonNull List<Node> loops) {
        for (Node loop : loops) {
            if (isDotInsideLoop(x, y, loop)) {
                return true;
            }
        }
        return false;
    }

    public boolean isDotBelongLoop(int x, int y, Node loop) {
        if (loop == null) {
            return false;
        }
        if (x == loop.x && y == loop.y) {
            return true;
        } else {
            return isDotBelongLoop(x, y, loop.next);
        }
    }

    public boolean isDotBelongLoops(int x, int y, @NonNull List<Node> loops) {
        for (Node loop : loops) {
            if (isDotBelongLoop(x, y, loop)) {
                return true;
            }
        }
        return false;
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
