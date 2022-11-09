package com.dotsgamems.mongo;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class MongoService {

    private final MongoTemplate mongoTemplate;

    private List<MongoBoard> mongoBoards = new ArrayList<>();

    private void getMongoBoards(@NonNull Integer boardSize) {
        Query query = new Query();
        query.addCriteria(Criteria.where("boardSize").is(boardSize));

        val mongoBoards = mongoTemplate.find(query, MongoBoard.class);
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
