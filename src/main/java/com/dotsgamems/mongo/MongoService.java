package com.dotsgamems.mongo;

import com.dotsgamems.game.Players;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;
import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class MongoService {

    private final MongoTemplate mongoTemplate;

    private Map<String, MongoBoard> mongoBoardsMap = new HashMap<>();


    private Random random = new Random();

    private void downloadMongoBoards(@NonNull Integer boardSize) {
        Query query = new Query();
        query.addCriteria(Criteria.where("boardSize").is(boardSize));
        val mongoBoards = mongoTemplate.find(query, MongoBoard.class);
        mongoBoards.forEach(mongoBoard -> mongoBoardsMap.put(mongoBoard.getBoard(), mongoBoard));
    }

    /**
     * Method calculates next move for given 'player' for given 'board'
     * @param player given player
     * @param board given board
     * @return the next move
     */
    @NonNull
    public Point getProbabilityMove(@NonNull Players player, @NonNull String[][] board) {

        if (mongoBoardsMap.isEmpty()) {
            downloadMongoBoards(board.length);
        }

        val boardString = transformBoardToString(board);
        MongoBoard mongoBoard;
        if (mongoBoardsMap.containsKey(boardString)) {
            mongoBoard = mongoBoardsMap.get(boardString);
        } else {
            mongoBoard = new MongoBoard(boardString, board.length, getAvailableMoves(board));
            mongoBoardsMap.put(boardString, mongoBoard);
        }
        val mongoMoves = player == Players.FIRST ? mongoBoard.getFirstPlayerMoves() : mongoBoard.getSecondPlayerMoves();
        List<Point> probabilityMoves = new ArrayList<>();
        for (MongoMove mongoMove : mongoMoves) {
            for (int i = 0; i < mongoMove.getProbability(); i++) {
                probabilityMoves.add(new Point(mongoMove.getX(), mongoMove.getY()));
            }
        }
        int value = random.nextInt(probabilityMoves.size());
        return probabilityMoves.get(value);
    }

    private List<Point> getAvailableMoves(@NonNull String[][] board) {
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


    public String transformBoardToString(@NonNull String[][] board) {
        val result = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                result.append(board[i][j]);
            }
        }
        return result.toString();
    }

    public String[][] transformBoardToArray(@NonNull Integer boardSize, @NonNull String board) {
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
}
