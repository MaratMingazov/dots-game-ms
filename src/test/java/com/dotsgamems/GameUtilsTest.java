package com.dotsgamems;

import com.dotsgamems.game.GameUtils;
import com.dotsgamems.game.Players;
import lombok.NonNull;
import lombok.val;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(SpringExtension.class)
public class GameUtilsTest {

    @ParameterizedTest
    @MethodSource("provideInputsForRemoveTailDots")
    void testRemoveTailDots(@NonNull Players player, @NonNull String[][] givenBoard, @NonNull String[][] expectedBoard ) {
        val gameUtils = new GameUtils();
        val actualBoard = gameUtils.removeTailDots(Players.FIRST, givenBoard);

        assertTrue(isEqual(actualBoard, expectedBoard));
    }

    @ParameterizedTest
    @MethodSource("provideInputsForFindCapturedDots")
    void testFindCapturedDots(@NonNull Players player, @NonNull String[][] givenBoard, @NonNull List<Point> expectedDots ) {
        val gameUtils = new GameUtils();
        val actualDots = gameUtils.findCapturedDots(Players.FIRST, givenBoard);

        assertTrue(isEqualDots(actualDots, expectedDots));
    }

    @ParameterizedTest
    @MethodSource("provideInputsForFindCapturedEmptyDots")
    void testFindCapturedEmptyDots(@NonNull String[][] board, @NonNull List<Point> capturedDots, @NonNull List<Point> expectedDots ) {
        val gameUtils = new GameUtils();
        val actualDots = gameUtils.findCapturedEmptyDots(board, capturedDots);

        assertTrue(isEqualDots(actualDots, expectedDots));
    }

    @ParameterizedTest
    @MethodSource("provideInputsForRTransformBoardToString")
    void testTransformBoardToString(@NonNull String[][] board, @NonNull String expectedBoard ) {
        val actualBoard = GameUtils.transformBoardToString(board);
        assertEquals(actualBoard, expectedBoard);
    }

    private static Stream<Arguments> provideInputsForRTransformBoardToString() {

        /**
         * 0110
         * 1201
         * 1200
         * 0111
         */
        val board1 = createBoard(4);
        board1[0][1] = Players.FIRST.getDotLabel();
        board1[0][2] = Players.FIRST.getDotLabel();
        board1[1][0] = Players.FIRST.getDotLabel();
        board1[1][1] = Players.SECOND.getDotLabel();
        board1[1][3] = Players.FIRST.getDotLabel();
        board1[2][0] = Players.FIRST.getDotLabel();
        board1[2][1] = Players.SECOND.getDotLabel();
        board1[3][1] = Players.FIRST.getDotLabel();
        board1[3][2] = Players.FIRST.getDotLabel();
        board1[3][3] = Players.FIRST.getDotLabel();
        val expectedBoard1 = "0110120112000111";


        return Stream.of(
                Arguments.of(board1, expectedBoard1)
        );
    }

    private static Stream<Arguments> provideInputsForFindCapturedEmptyDots() {

        /**
         * 0110
         * 1201
         * 1201
         * 0110
         */
        val board1 = createBoard(10);
        board1[1][2] = Players.FIRST.getDotLabel();
        board1[1][3] = Players.FIRST.getDotLabel();
        board1[2][4] = Players.FIRST.getDotLabel();
        board1[3][4] = Players.FIRST.getDotLabel();
        board1[4][3] = Players.FIRST.getDotLabel();
        board1[4][2] = Players.FIRST.getDotLabel();
        board1[3][1] = Players.FIRST.getDotLabel();
        board1[2][1] = Players.FIRST.getDotLabel();
        board1[2][2] = Players.SECOND.getDotLabel();
        board1[3][2] = Players.SECOND.getDotLabel();
        List<Point> capturedDots1 = new ArrayList<>();
        capturedDots1.add(new Point(2,2));
        capturedDots1.add(new Point(3,2));
        List<Point> expectedDots1 = new ArrayList<>();
        expectedDots1.add(new Point(2,3));
        expectedDots1.add(new Point(3,3));

        return Stream.of(
                Arguments.of(board1, capturedDots1, expectedDots1)
        );
    }

    private static Stream<Arguments> provideInputsForFindCapturedDots() {
        val givenBoard1 = createBoard(10);
        List<Point> expectedDots1 = new ArrayList<>();

        val givenBoard2 = createBoard(10);
        givenBoard2[3][3] = Players.FIRST.getDotLabel();
        givenBoard2[4][4] = Players.SECOND.getDotLabel();
        List<Point> expectedDots2 = new ArrayList<>();

        val givenBoard3 = createBoard(10);
        givenBoard3[2][3] = Players.FIRST.getDotLabel();
        givenBoard3[3][4] = Players.FIRST.getDotLabel();
        givenBoard3[4][3] = Players.FIRST.getDotLabel();
        givenBoard3[3][2] = Players.FIRST.getDotLabel();
        givenBoard3[3][3] = Players.SECOND.getDotLabel();
        List<Point> expectedDots3 = new ArrayList<>();
        expectedDots3.add(new Point(3,3));

        /**
         * 010
         * 121
         * 121
         * 010
         */
        val givenBoard4 = createBoard(10);
        givenBoard4[2][3] = Players.FIRST.getDotLabel();
        givenBoard4[3][4] = Players.FIRST.getDotLabel();
        givenBoard4[4][4] = Players.FIRST.getDotLabel();
        givenBoard4[5][3] = Players.FIRST.getDotLabel();
        givenBoard4[4][2] = Players.FIRST.getDotLabel();
        givenBoard4[3][2] = Players.FIRST.getDotLabel();
        givenBoard4[3][3] = Players.SECOND.getDotLabel();
        givenBoard4[4][3] = Players.SECOND.getDotLabel();
        givenBoard4[5][5] = Players.SECOND.getDotLabel();
        List<Point> expectedDots4 = new ArrayList<>();
        expectedDots4.add(new Point(3,3));
        expectedDots4.add(new Point(4,3));


        /**
         * 00000
         * 00000
         * 00001
         * 00010
         * 00102
         */
        val givenBoard5 = createBoard(5);
        givenBoard5[2][4] = Players.FIRST.getDotLabel();
        givenBoard5[3][3] = Players.FIRST.getDotLabel();
        givenBoard5[4][2] = Players.FIRST.getDotLabel();
        givenBoard5[4][4] = Players.SECOND.getDotLabel();
        List<Point> expectedDots5 = new ArrayList<>();



        return Stream.of(
                Arguments.of(Players.FIRST, givenBoard1, expectedDots1),
                Arguments.of(Players.FIRST, givenBoard2, expectedDots2),
                Arguments.of(Players.FIRST, givenBoard3, expectedDots3),
                Arguments.of(Players.FIRST, givenBoard4, expectedDots4),
                Arguments.of(Players.FIRST, givenBoard5, expectedDots5)
        );
    }

    private static Stream<Arguments> provideInputsForRemoveTailDots() {
        val givenBoard1 = createBoard(10);
        val expectedBoard1 = createBoard(10);

        val givenBoard2 = createBoard(10);
        givenBoard2[0][0] = Players.FIRST.getDotLabel();
        givenBoard2[3][3] = Players.SECOND.getDotLabel();
        val expectedBoard2 = createBoard(10);
        expectedBoard2[3][3] = Players.SECOND.getDotLabel();

        val givenBoard3 = createBoard(10);
        givenBoard3[3][3] = Players.FIRST.getDotLabel();
        val expectedBoard3 = createBoard(10);

        val givenBoard4 = createBoard(10);
        givenBoard4[3][3] = Players.FIRST.getDotLabel();
        givenBoard4[3][4] = Players.FIRST.getDotLabel();
        givenBoard4[4][3] = Players.FIRST.getDotLabel();
        givenBoard4[4][4] = Players.FIRST.getDotLabel();
        givenBoard4[5][5] = Players.FIRST.getDotLabel();
        givenBoard4[5][6] = Players.FIRST.getDotLabel();
        givenBoard4[5][7] = Players.FIRST.getDotLabel();
        val expectedBoard4 = createBoard(10);
        expectedBoard4[3][3] = Players.FIRST.getDotLabel();
        expectedBoard4[3][4] = Players.FIRST.getDotLabel();
        expectedBoard4[4][3] = Players.FIRST.getDotLabel();
        expectedBoard4[4][4] = Players.FIRST.getDotLabel();

        return Stream.of(
//                Arguments.of(Players.FIRST, givenBoard1, expectedBoard1),
//                Arguments.of(Players.FIRST, givenBoard2, expectedBoard2),
//                Arguments.of(Players.FIRST, givenBoard3, expectedBoard3),
                Arguments.of(Players.FIRST, givenBoard4, expectedBoard4)
        );
    }




    private static String[][] createBoard(int boardSize) {
        String[][] board = new String[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = Players.getEmptyDotLabel();
            }
        }
        return board;
    }

    private boolean isEqual(@NonNull String[][] firstBoard, @NonNull String[][] secondBoard) {
        if (firstBoard.length != secondBoard.length) {
            return false;
        }
        for (int i = 0; i < firstBoard.length; i++) {
            if (firstBoard[i].length != secondBoard[i].length) {
                return false;
            }
            for (int j = 0; j < firstBoard[i].length; j++) {
                if(!firstBoard[i][j].equals(secondBoard[i][j])) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isEqualDots(@NonNull List<Point> firstDots, @NonNull List<Point> secondDots) {
        if (firstDots.size() != secondDots.size()) {
            return false;
        }
        for (Point firstDot : firstDots) {
            val equalDotsCount = secondDots.stream().filter(secondDot -> secondDot.x == firstDot.x && secondDot.y == firstDot.y).count();
            if (equalDotsCount != 1) {
                return false;
            }
        }
        return true;
    }
}
