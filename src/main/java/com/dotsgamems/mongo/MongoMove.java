package com.dotsgamems.mongo;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class MongoMove {

    int x;
    int y;
    int probability;
}
