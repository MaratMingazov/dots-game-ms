package com.dotsgamems.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Document(collection = "board")
public class MongoBoard {

    @Id
    String id;

    @NonNull
    private Integer boardSize;

    @NonNull
    private String board;

    @NonNull
    List<MongoMove> firstPlayerMoves;

    @NonNull
    List<MongoMove> secondPlayerMoves;

    @Transient
    boolean isChanged = false;

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
