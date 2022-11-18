package com.dotsgamems.game;

import lombok.NonNull;
import lombok.val;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameUtils {

    /**
     * Метод проходится по доске 'board' и удаляет все точки играка 'player' которые не сосетстуют с другими точками данного играка
     * The method go over the 'board' and delete of 'player' dots that have less then 2 neighbors
     * @param player given player
     * @param board given board
     * @return new board with removed tail dots
     */
    @NonNull
    public static String[][] removeTailDots(@NonNull Players player,
                                     @NonNull String[][] board) {
        val copyBoard = copyBoard(board);
        boolean needNewIteration;
        do {
            needNewIteration = removeTailDotsIteration(player, copyBoard);
        } while (needNewIteration);
        return copyBoard;
    }

    private static boolean removeTailDotsIteration(@NonNull Players player,
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
    public static boolean isPointBelongToBoard(@NonNull String[][] board, int x, int y) {
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
    public static String[][] copyBoard(@NonNull String[][] sourceBoard) {
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

    public static String[][] createEmptyBoard(int boardSize) {
        String[][] board = new String[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = Players.getEmptyDotLabel();
            }
        }
        return board;
    }

    /**
     * The method find the captured empty dots for given 'capturedPlayerDots'
     * The idea is to find all empty dots near 'capturedPlayerDots' as they captured as well
     * @param board given board
     * @param capturedPlayerDots given captured player dots
     * @return list of captured empty dots
     */
    public static List<Point> findCapturedEmptyDots(@NonNull String[][] board,
                                             @NonNull List<Point> capturedPlayerDots) {
        List<Point> capturedEmptyDots = new ArrayList<>();
        List<Point> checkedEmptyDots = new ArrayList<>();
        for (Point dot : capturedPlayerDots) {
            capturedEmptyDots.addAll(checkEmptyCapturedDots(board, checkedEmptyDots, new Point(dot.x - 1, dot.y)));
            capturedEmptyDots.addAll(checkEmptyCapturedDots(board, checkedEmptyDots, new Point(dot.x, dot.y + 1)));
            capturedEmptyDots.addAll(checkEmptyCapturedDots(board, checkedEmptyDots, new Point(dot.x + 1, dot.y)));
            capturedEmptyDots.addAll(checkEmptyCapturedDots(board, checkedEmptyDots, new Point(dot.x, dot.y - 1)));
        }
        return capturedEmptyDots;
    }

    private static List<Point> checkEmptyCapturedDots(@NonNull String[][] board,
                                              @NonNull List<Point> checkedEmptyDots,
                                              @NonNull Point dot) {
        List<Point> capturedEmptyDots = new ArrayList<>();

        if (!isPointBelongToBoard(board, dot.x, dot.y)) {
            return new ArrayList<>();
        }
        if (!board[dot.x][dot.y].equals(Players.getEmptyDotLabel())) {
            return new ArrayList<>();
        }
        val isAlreadyChecked = checkedEmptyDots.stream().anyMatch(checkedDot -> checkedDot.x == dot.x && checkedDot.y == dot.y);
        if (isAlreadyChecked) {
            return new ArrayList<>();
        }

        checkedEmptyDots.add(dot);
        capturedEmptyDots.add(dot);
        capturedEmptyDots.addAll(checkEmptyCapturedDots(board, checkedEmptyDots, new Point(dot.x - 1, dot.y)));
        capturedEmptyDots.addAll(checkEmptyCapturedDots(board, checkedEmptyDots, new Point(dot.x, dot.y + 1)));
        capturedEmptyDots.addAll(checkEmptyCapturedDots(board, checkedEmptyDots, new Point(dot.x + 1, dot.y)));
        capturedEmptyDots.addAll(checkEmptyCapturedDots(board, checkedEmptyDots, new Point(dot.x, dot.y - 1)));

        return capturedEmptyDots;
    }
    
    

    /**
     * Method find all dots captured by given player
     * We check every opposite player dots being captured
     * @param player given player who wants to capture dots of opposite player
     * @param board given board
     * @return list of all captured dots of opposite player
     */
    public static List<Point> findCapturedDots(@NonNull Players player,
                                        @NonNull String[][] board){
        List<Point> capturedDots = new ArrayList<>();
        List<Point> notCapturedDots = new ArrayList<>();

        // we should find all opposite player dots
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].equals(player.getDotLabel()) || board[i][j].equals(Players.getEmptyDotLabel())) {
                    // It means the given dot belong to player or empty. It can not be captured
                    continue;
                }
                // from every dot we try to find path to board border
                val dot = new Point(i, j);
                List<Point> visitedDots = new ArrayList<>(); // when we will search path we should know which dots we already analyzed
                if (isDotCaptured(player, board, capturedDots, notCapturedDots, visitedDots, dot)) {
                    capturedDots.add(dot);
                } else {
                    notCapturedDots.add(dot);
                }
            }
        }
        return capturedDots;
    }

    /**
     * The method check whether the given 'dot' is captured by 'player'.
     * We are trying to create path to board borders.
     * If it is possible to create such a path, it means the given dot is not captured
     * Otherwise is captured.
     * @param player given player who wants to capture dots
     * @param board given board
     * @param capturedDots already checked and captured dots
     * @param notCapturedDots already checked and not captured dots
     * @param dot given dot that we need to check being captured. Here we have only opposite player dots
     * @return 'true' if the given dot is captured by player and 'false' otherwise
     */
    private static boolean isDotCaptured(@NonNull Players player,
                                  @NonNull String[][] board,
                                  @NonNull List<Point> capturedDots,
                                  @NonNull List<Point> notCapturedDots,
                                  @NonNull List<Point> visitedDots,
                                  @NonNull Point dot) {
        if (board.length < 3 || board[0].length < 3) {
            return false;
        }
        if (dot.x == 0 || dot.x == board.length-1 || dot.y == 0 || dot.y == board[0].length-1) {
            // it means the given dot located on border and can't be captured
            return false;
        }
        val isCapturedBefore = capturedDots.stream().anyMatch(capturedDot -> capturedDot.x == dot.x && capturedDot.y == dot.y);
        if (isCapturedBefore) {
            // it means we already checked this dot in previous call, and it was captured
            return true;
        }
        val notCapturedBefore = notCapturedDots.stream().anyMatch(capturedDot -> capturedDot.x == dot.x && capturedDot.y == dot.y);
        if (notCapturedBefore) {
            // it means we already checked this dot in previous call, and it was not captured
            return false;
        }

        // now we should check the neighbor nodes.
        val upDot = new Point(dot.x - 1, dot.y);
        if (!isNextDotCaptured(player, board, capturedDots, notCapturedDots, visitedDots, upDot)) {
            return false;
        }

        val leftDot = new Point(dot.x, dot.y - 1);
        if (!isNextDotCaptured(player, board, capturedDots, notCapturedDots, visitedDots, leftDot)) {
            return false;
        }

        val downDot = new Point(dot.x + 1, dot.y);
        if (!isNextDotCaptured(player, board, capturedDots, notCapturedDots, visitedDots, downDot)) {
            return false;
        }

        val rightDot = new Point(dot.x, dot.y + 1);
        if (!isNextDotCaptured(player, board, capturedDots, notCapturedDots, visitedDots, rightDot)) {
            return false;
        }

        return true;
    }

    private static boolean isNextDotCaptured(@NonNull Players player,
                                      @NonNull String[][] board,
                                      @NonNull List<Point> capturedDots,
                                      @NonNull List<Point> notCapturedDots,
                                      @NonNull List<Point> visitedDots,
                                      @NonNull Point nextDot) {

        if (!isPointBelongToBoard(board, nextDot.x, nextDot.y)) {
            return true;
        }


        // we can move there only if it is not player dot, and we do not visit this dot yet
        val isNextDotAlreadyVisited = visitedDots.stream().anyMatch(visitedDot -> visitedDot.x == nextDot.x && visitedDot.y == nextDot.y);
        if (!board[nextDot.x][nextDot.y].equals(player.getDotLabel()) && !isNextDotAlreadyVisited) {
            // we will analyze this dot, so we should update visited dots
            visitedDots.add(nextDot);
            // if upDot is not captured it means that the given dot is also not captured
            if (!isDotCaptured(player, board, capturedDots,notCapturedDots, visitedDots, nextDot)) {
                return false;
            }
        }
        return true;
    }

    public static String transformBoardToString(@NonNull String[][] board) {
        val result = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                result.append(board[i][j]);
            }
        }
        return result.toString();
    }

    public static String[][] transformBoardToArray(@NonNull Integer boardSize, @NonNull String board) {
        if (board.length() != boardSize * boardSize) {
            throw new IllegalArgumentException("Given boardSize is not correct.");
        }
        val result = new String[boardSize][boardSize];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                int beginIndex = i * boardSize + j;
                result[i][j] = board.substring(beginIndex, beginIndex + 1);
            }
        }
        return result;
    }

    public static boolean isGameFinished(@NonNull String[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].equals(Players.getEmptyDotLabel())) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Map<Players,Integer> calculateScore(String[][] humanBoard, String[][]computerBoard) {
        int firstPlayerScore = 0;
        int secondPlayerScore = 0;
        for (int i = 0; i < computerBoard.length; i++) {
            for (int j = 0; j < computerBoard[i].length; j++) {
                if (computerBoard[i][j].equals(Players.FIRST.getDotLabel()) && humanBoard[i][j].equals(Players.SECOND.getDotLabel())) {
                    firstPlayerScore++;
                } else if (computerBoard[i][j].equals(Players.SECOND.getDotLabel()) && humanBoard[i][j].equals(Players.FIRST.getDotLabel())) {
                    secondPlayerScore++;
                }
            }
        }
        return Map.of(Players.FIRST, firstPlayerScore, Players.SECOND, secondPlayerScore);
    }

    public static String[][] updateCapturedDots(@NonNull Players player, @NonNull String[][] board) {
        val capturedDots = findCapturedDots(player, board);
        val capturedEmptyDots = findCapturedEmptyDots(board, capturedDots);
        capturedDots.forEach(dot -> board[dot.x][dot.y] = player.getDotLabel());
        capturedEmptyDots.forEach(dot -> board[dot.x][dot.y] = player.getDotLabel());
        return board;
    }

    public static void setDot(@NonNull Players player,
                              @NonNull String[][] humanBoard,
                              @NonNull String[][] computerBoard,
                              int x, int y) {
        if (!isPointBelongToBoard(humanBoard, x, y)) {
            throw new IllegalArgumentException("The given point is out of boarder: " + x + "," + y);
        }
        if (!computerBoard[x][y].equals(Players.getEmptyDotLabel())) {
            throw new IllegalArgumentException("The board already have dot at this point: " + x + "," + y);
        }
        humanBoard[x][y] = player.getDotLabel();
        computerBoard[x][y] = player.getDotLabel();
    }

    public static List<Point> getAvailableMoves(@NonNull String[][] board) {
        List<Point> result = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if(board[i][j].equals(Players.getEmptyDotLabel())) {
                    result.add(new Point(i,j));
                }
            }
        }
        return result;
    }

    public static List<Point> findBadMoves(@NonNull Players player,
                                           @NonNull Integer boardSize,
                                           @NonNull String humanBoardString,
                                           @NonNull String computerBoardString) {
        List<Point> badMoves = new ArrayList<>();
        var humanBoard = transformBoardToArray(boardSize, humanBoardString);
        var computerBoard = transformBoardToArray(boardSize, computerBoardString);
        var gameScore = calculateScore(humanBoard, computerBoard);
        val oppositePlayer = Players.getOppositeById(player.getId());
        val oppositePlayerScore = gameScore.get(oppositePlayer);

        val availableMoves = getAvailableMoves(computerBoard);
        if (availableMoves.isEmpty()) {
            return badMoves;
        }

        for (Point move : availableMoves) {
            humanBoard = GameUtils.transformBoardToArray(boardSize, humanBoardString);
            computerBoard = GameUtils.transformBoardToArray(boardSize, computerBoardString);
            GameUtils.setDot(player, humanBoard, computerBoard, move.x, move.y);
            GameUtils.updateCapturedDots(player, computerBoard);
            GameUtils.updateCapturedDots(oppositePlayer, computerBoard);

            gameScore = calculateScore(humanBoard, computerBoard);
            val oppositePlayerNewScore = gameScore.get(oppositePlayer);

            if (oppositePlayerNewScore > oppositePlayerScore) {
                badMoves.add(move);
            }
        }


        return badMoves;
    }

}
