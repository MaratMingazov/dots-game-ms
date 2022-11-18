package com.dotsgamems.mongo;

import com.dotsgamems.game.GameUtils;
import com.dotsgamems.game.Players;
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
    @Field("bo")
    private String boardString;

    @Field("si")
    private Integer boardSize;

    @Field("av")
    Integer availableMovesCount;


    @Field("fi")
    String firstPlayerProbabilities;

    @Field("se")
    String secondPlayerProbabilities;

    @Transient
    List<MongoMove> firstPlayerMoves = new ArrayList<>();
    @Transient
    List<MongoMove> secondPlayerMoves = new ArrayList<>();

    @Transient
    boolean isChanged = false;

    public MongoBoard (@NonNull String boardString, @NonNull Integer boardSize) {
        this.boardSize = boardSize;
        this.boardString = boardString;
        this.firstPlayerMoves = new ArrayList<>();
        this.secondPlayerMoves = new ArrayList<>();
        val board = GameUtils.transformBoardToArray(boardSize, boardString);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].equals(Players.getEmptyDotLabel())) {
                    this.firstPlayerMoves.add(new MongoMove(i, j, 100));
                    this.secondPlayerMoves.add(new MongoMove(i, j, 100));
                } else {
                    this.firstPlayerMoves.add(new MongoMove(i, j, 0));
                    this.secondPlayerMoves.add(new MongoMove(i, j, 0));
                }
            }
        }
        generateProbabilitiesString();
        this.availableMovesCount = GameUtils.getAvailableMoves(board).size();
        this.isChanged = true;
    }

    public void generateProbabilitiesString() {
        val firstPlayerProbabilitiesBuilder = new StringBuilder();
        val secondPlayerProbabilitiesBuilder = new StringBuilder();
        for (int i = 0; i < firstPlayerMoves.size(); i++) {
            firstPlayerProbabilitiesBuilder.append(firstPlayerMoves.get(i).probability).append(";");
            secondPlayerProbabilitiesBuilder.append(secondPlayerMoves.get(i).probability).append(";");
        }
        this.firstPlayerProbabilities = firstPlayerProbabilitiesBuilder.toString();
        this.secondPlayerProbabilities = secondPlayerProbabilitiesBuilder.toString();
    }

    public void generateProbabilities() {
        val firstProbabilities = firstPlayerProbabilities.split(";");
        val secondProbabilities = secondPlayerProbabilities.split(";");
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                firstPlayerMoves.add(new MongoMove(i,j, Integer.parseInt(firstProbabilities[i * boardSize + j])));
                secondPlayerMoves.add(new MongoMove(i,j, Integer.parseInt(secondProbabilities[i * boardSize + j])));
            }
        }
        val board = GameUtils.transformBoardToArray(boardSize, boardString);
        this.availableMovesCount = GameUtils.getAvailableMoves(board).size();
    }




}
