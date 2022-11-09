package com.dotsgamems;

import com.dotsgamems.game.Players;
import com.dotsgamems.mongo.MongoService;
import lombok.NonNull;
import lombok.val;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Import(MongoService.class)
@ExtendWith(SpringExtension.class)
public class MongoServiceTest {

    @Autowired
    MongoService mongoService;

    @MockBean
    MongoTemplate mongoTemplate;

    @ParameterizedTest
    @MethodSource("provideInputsForRTransformBoardToString")
    void testTransformBoardToString(@NonNull String[][] board, @NonNull String expectedBoard ) {
        val actualBoard = mongoService.transformBoardToString(board);
        assertEquals(actualBoard, expectedBoard);
    }

    @ParameterizedTest
    @MethodSource("provideInputsForRTransformBoardToArray")
    void testTransformBoardToArray(@NonNull Integer boardSize, @NonNull String boardString, @NonNull String[][] expectedBoard ) {
        val actualBoard = mongoService.transformBoardToArray(boardSize, boardString);
        assertTrue(boardsAreEqual(actualBoard, expectedBoard));
    }

    private static Stream<Arguments> provideInputsForRTransformBoardToArray() {

        /**
         * 0110
         * 1201
         * 1200
         * 0111
         */
        Integer boardSize1 = 4;
        val board1 = "0110120112000111";
        val expectedBoard1 = createBoard(4);
        expectedBoard1[0][1] = Players.FIRST.getDotLabel();
        expectedBoard1[0][2] = Players.FIRST.getDotLabel();
        expectedBoard1[1][0] = Players.FIRST.getDotLabel();
        expectedBoard1[1][1] = Players.SECOND.getDotLabel();
        expectedBoard1[1][3] = Players.FIRST.getDotLabel();
        expectedBoard1[2][0] = Players.FIRST.getDotLabel();
        expectedBoard1[2][1] = Players.SECOND.getDotLabel();
        expectedBoard1[3][1] = Players.FIRST.getDotLabel();
        expectedBoard1[3][2] = Players.FIRST.getDotLabel();
        expectedBoard1[3][3] = Players.FIRST.getDotLabel();

        return Stream.of(
                Arguments.of(boardSize1, board1, expectedBoard1)
        );
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

    private static String[][] createBoard(int boardSize) {
        String[][] board = new String[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = Players.getEmptyDotLabel();
            }
        }
        return board;
    }

    private boolean boardsAreEqual(@NonNull String[][] first, @NonNull String[][] second) {
        for (int i = 0; i < first.length; i++) {
            for (int j = 0; j < first[i].length; j++) {
                if(!first[i][j].equals(second[i][j])) {
                    return false;
                }
            }
        }
        return true;
    }
}
