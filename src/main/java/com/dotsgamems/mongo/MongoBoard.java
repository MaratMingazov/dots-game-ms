package com.dotsgamems.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

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




}
