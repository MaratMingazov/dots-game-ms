package com.dotsgamems.mongo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Document(collection = "board")
public class MongoBoard {

    @Id
    String id;

    @Field("boardSize")
    private Integer boardSize;


    @Field("board")
    private String board;


    @Field("firstPlayerMoves")
    List<MongoMove> firstPlayerMoves;


    @Field("secondPlayerMoves")
    List<MongoMove> secondPlayerMoves;

    @Transient
    boolean isChanged = false;


    public MongoBoard(String id, Integer boardSize, String board, List<MongoMove> firstPlayerMoves, List<MongoMove> secondPlayerMoves) {
        this.id = id;
        this.boardSize = boardSize;
        this.board = board;
        this.firstPlayerMoves = firstPlayerMoves;
        this.secondPlayerMoves = secondPlayerMoves;
    }

    public MongoBoard (@NonNull String board, @NonNull Integer boardSize, @NonNull List<Point> availableMoves) {
        this.boardSize = boardSize;
        this.board = board;
        this.firstPlayerMoves = new ArrayList<>();
        this.secondPlayerMoves = new ArrayList<>();
        availableMoves.forEach(point -> {
            this.firstPlayerMoves.add(new MongoMove(point.x, point.y, 100));
            this.secondPlayerMoves.add(new MongoMove(point.x, point.y, 100));
        });
        this.isChanged = true;
    }




}
