package com.dotsgamems.mongo;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class MongoService {

    private final MongoTemplate mongoTemplate;

    private void getMongoBoards(@NonNull Integer boardSize) {
        Query query = new Query();
        query.addCriteria(Criteria.where("boardSize").is(boardSize));

        val boards = mongoTemplate.find(query, MongoBoard.class);

    }
}
